package com.harmony.belling.lark.annotations

import org.springframework.stereotype.Component

@Component
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LarkCommand(
    val name: String,
    val description: String = "",
)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Operation(
    val name: String,
    val description: String = "",
    val group: Boolean = true,
    val p2p: Boolean = true,
)

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Param(
    val name: String = "",
    val defaultValue: String = "",
    val required: Boolean = true,
    val description: String = "",
)
