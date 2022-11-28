package com.harmony.belling.utils

import com.harmony.bitable.model.Option
import org.springframework.util.ReflectionUtils

object OptionUtils {

    fun <T : Option> getOptions(optionType: Class<T>): Map<String, T> {
        if (!optionType.isEnum) {
            throw IllegalStateException("${optionType.simpleName} not enum")
        }
        val method = optionType.getMethod("values")
        val values = ReflectionUtils.invokeMethod(method, optionType) as Array<T>
        return values.associateBy { it.getText() }
    }

}
