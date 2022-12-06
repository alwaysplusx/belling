package com.harmony.belling.support

interface TemplateEngine {

    fun render(key: String, dataModel: Any = emptyMap<Any, Any>()): String

}
