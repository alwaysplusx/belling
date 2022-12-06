package com.harmony.belling.lark.handler.event.msg

import com.lark.oapi.service.im.v1.model.EventMessage
import com.lark.oapi.service.im.v1.model.EventSender
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.support.DefaultConversionService

data class CommandContext(
    val message: EventMessage,
    val sender: EventSender,
    val prefix: String,
    val action: String,
    val arguments: Map<String, String?>,
    val group: Boolean = (message.chatType == "group"),
    val p2p: Boolean = (message.chatType == "p2p"),
    val conversionService: ConversionService = DefaultConversionService(),
) {

    fun getParameterValue(p: CommandParameter): Any? {
        val parameterType: Class<*> = p.parameterType
        if (parameterType == EventSender::class.java) {
            return sender
        }
        if (parameterType == EventMessage::class.java) {
            return message
        }
        val value = arguments[p.name]
        if (value.isNullOrBlank() && p.methodParameter.parameterIndex == 0 && arguments.size == 1) {
            val firstArgumentValue = arguments.values.iterator().next()
            return conversionService.convert(firstArgumentValue, parameterType)
        }
        return if (value.isNullOrBlank()) {
            getParameterDefaultValue(p)
        } else try {
            conversionService.convert(value, parameterType)
        } catch (e: Exception) {
            throw IllegalArgumentException("convert parameter ${p.name} failed")
        }
    }

    private fun getParameterDefaultValue(parameter: CommandParameter): Any? {
        val defaultValue: String = parameter.defaultValue
        if (defaultValue.isBlank()) {
            return null
        }
        return try {
            conversionService.convert(defaultValue, parameter.parameterType)
        } catch (e: java.lang.Exception) {
            throw IllegalArgumentException("convert parameter ${parameter.name} failed")
        }
    }

}
