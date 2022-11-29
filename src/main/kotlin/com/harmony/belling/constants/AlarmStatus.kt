package com.harmony.belling.constants

import com.fasterxml.jackson.annotation.JsonFormat
import com.harmony.bitable.model.Option

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class AlarmStatus(private val text: String) : Option {

    HANDLING("待处理"),

    RESOLVED("已处理"),

    FINISHED("已完结"),

    IGNORE("忽略");

    override fun getText(): String = text

}
