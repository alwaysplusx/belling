package com.harmony.belling.support

import com.harmony.belling.service.TemplateService
import freemarker.cache.StringTemplateLoader
import freemarker.cache.TemplateLoader
import freemarker.template.*
import org.springframework.stereotype.Component
import java.io.StringWriter
import java.nio.charset.StandardCharsets

@Component
internal class DocxTemplateEngine(private val templateService: TemplateService) : TemplateEngine {

    companion object {

        fun getTemplateConfiguration(loader: TemplateLoader): Configuration {
            return Configuration(Configuration.VERSION_2_3_30).apply {
                this.defaultEncoding = StandardCharsets.UTF_8.name()
                this.templateLoader = loader
                this.setSharedVariable("larkAt", LarkAtTemplateMethodModelEx())
            }
        }

    }

    override fun render(key: String, dataModel: Any): String {
        val template = getTemplate(key)
        return renderTemplate(template, dataModel)
    }

    private fun renderTemplate(template: Template, dataModel: Any): String {
        val writer = StringWriter()
        template.process(dataModel, writer)
        return writer.toString()
    }

    private fun getTemplate(key: String): Template {
        val content = templateService.getTemplateContent(key)
        val loader = StringTemplateLoader().apply {
            putTemplate(key, content)
        }
        return getTemplateConfiguration(loader).getTemplate(key)
    }

    private class LarkAtTemplateMethodModelEx : TemplateMethodModelEx {

        companion object {

            private const val NORMAL_PATTERN = """<at id="%s"></at>"""
            private const val ESCAPE_PATTERN = """<at id=\"%s\"></at>"""

        }

        override fun exec(arguments: List<Any?>): Any {
            if (arguments.isEmpty() || arguments[0] == null) {
                return ""
            }
            val openId: Any = getOpenId(arguments[0]!!)
            val escape = if (arguments.size > 1) arguments[1] else false
            return getPattern(escape).format(openId)
        }

        private fun getPattern(escape: Any?): String {
            if (escape is TemplateBooleanModel && escape.asBoolean || escape == true) {
                return ESCAPE_PATTERN
            }
            return NORMAL_PATTERN
        }

        private fun getOpenId(u: Any): Any {
            if (u is TemplateHashModelEx) {
                return u["id"]
            }
            return u
        }

    }

}
