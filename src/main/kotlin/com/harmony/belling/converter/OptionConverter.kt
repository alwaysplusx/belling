package com.harmony.belling.converter

import com.harmony.belling.utils.OptionUtils
import com.harmony.bitable.convert.DoubleSideBitfieldConverter
import com.harmony.bitable.model.Option
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OptionConverter : DoubleSideBitfieldConverter<Option, String>(Option::class.java, String::class.java) {

    private val log: Logger = LoggerFactory.getLogger(OptionConverter::class.java)

    var ignoreOptionNotFound = true

    override fun convertToSource(target: String?, type: Class<Option>): Option? {
        if (target == null) {
            return null
        }
        val option = OptionUtils.getOptions(type)[target]
        if (option == null && !ignoreOptionNotFound) {
            throw IllegalStateException("$target not found in ${type.simpleName}")
        }
        if (option == null) {
            log.error("{} not found in {}", target, type.simpleName)
        }
        return option
    }

    override fun convertToTarget(source: Option?, type: Class<String>): String? {
        return source?.getText()
    }

}
