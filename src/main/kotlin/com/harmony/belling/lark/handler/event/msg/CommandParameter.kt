package com.harmony.belling.lark.handler.event.msg

import org.springframework.core.MethodParameter

import org.springframework.core.annotation.AnnotationUtils.findAnnotation
import com.harmony.belling.lark.annotations.Param

data class CommandParameter(
    val name: String,
    val methodParameter: MethodParameter,
    val parameterType: Class<*> = methodParameter.parameterType,
    val defaultValue: String,
    val required: Boolean,
    val description: String,
) {
    companion object {
        fun from(parameter: MethodParameter): CommandParameter {
            val attribute = findAnnotation(parameter.parameter, Param::class.java)
            return CommandParameter(
                name = parameter.parameterName ?: parameter.parameter.name ?: "",
                methodParameter = parameter,
                required = attribute?.required ?: true,
                defaultValue = attribute?.defaultValue ?: "",
                description = attribute?.description ?: ""
            )
        }
    }
}
