package com.harmony.belling.lark.handler.event.msg

import com.harmony.belling.lark.LarkFacade
import com.harmony.belling.lark.annotations.LarkCommand
import com.harmony.belling.lark.annotations.Operation
import com.harmony.belling.lark.handler.event.MessageReceiveHandler
import com.harmony.belling.lark.model.MessageContent
import com.harmony.belling.utils.LarkUtils.getEventMessageTextContent
import com.lark.oapi.service.im.v1.model.P2MessageReceiveV1Data
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.core.MethodParameter
import org.springframework.core.StandardReflectionParameterNameDiscoverer
import org.springframework.core.annotation.AnnotationUtils.findAnnotation
import org.springframework.stereotype.Component
import org.springframework.util.ClassUtils
import java.lang.reflect.Method
import java.util.*
import java.util.stream.IntStream

@Component
class CommandMessageHandler(private val larkFacade: LarkFacade) : MessageReceiveHandler.MessageHandler,
    ApplicationContextAware, InitializingBean {

    private lateinit var applicationContext: ApplicationContext

    val handlerMap: MutableMap<String, CommandHandler> = mutableMapOf()

    private val log = LoggerFactory.getLogger(CommandMessageHandler::class.java)

    fun addCommandHandler(handler: CommandHandler) {
        log.info("Add CommandHandler for {}", handler.getPrefix())
        handlerMap[handler.getPrefix()] = handler
    }

    override fun canHandle(data: P2MessageReceiveV1Data): Boolean {
        if (data.message.messageType != "text") {
            return false
        }
        val content = getEventMessageTextContent(data.message, true)
        return handlerMap.keys.any { content.startsWith(it) }
    }

    override fun handle(data: P2MessageReceiveV1Data) {
        val content = getEventMessageTextContent(data.message, true)
        val command = parseCommand(content) ?: throw IllegalStateException("unrealized command $content")

        val handler = handlerMap.entries.first { content.startsWith(it.key) }.value

        val context = CommandContext(
            message = data.message,
            sender = data.sender,
            prefix = handler.getPrefix(),
            action = command.action,
            arguments = parseArgumentMap(content)
        )

        val message = handler.handle(context)
        if (message != null) {
            larkFacade.replyMessage(data.message.messageId, message)
        }
    }

    private fun parseCommand(content: String): Command? {
        val st = StringTokenizer(content, " ")
        if (!st.hasMoreTokens()) {
            return null
        }
        return Command(
            prefix = st.nextToken(),
            action = if (st.hasMoreTokens()) st.nextToken() else "",
            arguments = (if (st.hasMoreTokens()) st.nextToken("") else "").trim()
        )
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    override fun afterPropertiesSet() {
        applicationContext
            .getBeansWithAnnotation(LarkCommand::class.java)
            .entries.forEach {
                val handler = buildAsCommandHandler(it.value)
                log.debug("Convert Bean {} to CommandHandler for {}", it.key, handler.getPrefix())
                addCommandHandler(handler)
            }
    }

    private fun buildAsCommandHandler(bean: Any): CommandHandler {
        val handlerClass = ClassUtils.getUserClass(bean)
        val commandAttribute = findAnnotation(handlerClass, LarkCommand::class.java)
            ?: throw IllegalStateException("$bean not command handler")
        return InternalCommandHandler(
            bean = bean,
            prefix = commandAttribute.name,
            operations = handlerClass.methods.filter { it.declaringClass != Any::class.java }
                .map { buildCommandOperation(it) }
        )
    }

    private fun buildCommandOperation(method: Method): CommandOperation {
        val attribute = findAnnotation(method, Operation::class.java)
        return CommandOperation(
            method = method,
            name = attribute?.name ?: method.name,
            description = attribute?.description ?: "",
            group = attribute?.group ?: true,
            p2p = attribute?.p2p ?: true,
            parameters = getMethodCommandParameters(method)
        )
    }

    private fun getMethodCommandParameters(method: Method): List<CommandParameter> {
        return IntStream.range(0, method.parameterCount)
            .mapToObj {
                MethodParameter(method, it).apply {
                    this.initParameterNameDiscovery(StandardReflectionParameterNameDiscoverer())
                }
            }
            .map { CommandParameter.from(it) }
            .toList()
    }

    private fun parseArgumentMap(argument: String): Map<String, String> {
        val regex = "-(\\w*)=".toRegex()
        val values = argument.split(regex)

        if (values.size == 1) {
            return emptyMap()
        }

        return regex.findAll(argument)
            .mapIndexed { i, r ->
                r.groupValues[1] to values[i + 1]
            }
            .toMap()
    }

    private class InternalCommandHandler(
        private val bean: Any,
        private val prefix: String,
        operations: List<CommandOperation>,
    ) : CommandHandler {

        private val operations = operations.associateBy { it.name }

        override fun getPrefix(): String = prefix

        override fun handle(context: CommandContext): MessageContent? {
            val op = operations[context.action]
                ?: throw IllegalStateException("$prefix ${context.action} command operation not found")

            if (context.p2p && !op.p2p) {
                throw IllegalStateException("$op not support p2p message")
            }

            return op.invoke(bean, context)
        }

    }

    private data class Command(
        val prefix: String,
        val action: String,
        val arguments: String,
    )

}
