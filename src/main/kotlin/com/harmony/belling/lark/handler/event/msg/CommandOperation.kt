package com.harmony.belling.lark.handler.event.msg

import com.harmony.belling.lark.model.MessageContent
import java.lang.reflect.Method

internal data class CommandOperation(
    val method: Method,
    val name: String,
    val description: String,
    val group: Boolean,
    val p2p: Boolean,
    val parameters: List<CommandParameter>,
) {

    fun invoke(bean: Any, context: CommandContext): MessageContent? {
        val arguments: Array<Any?> = parameters.map { context.getParameterValue(it) }.toTypedArray()
        val message = method.invoke(bean, *arguments) ?: return null
        if (message is MessageContent) {
            return message
        }
        return MessageContent.ofText(message.toString())
    }

}
