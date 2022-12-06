package com.harmony.belling.constants

import com.harmony.bitable.model.Option

enum class MemberType(private val text: String) : Option {

    OWNER("负责人"),

    MAINTAINER("维护者"),

    DEVELOPER("开发者"),

    REPORTER("报告者"),

    GUEST("访客");

    override fun getText(): String = text

}
