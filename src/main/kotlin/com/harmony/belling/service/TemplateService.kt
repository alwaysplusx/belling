package com.harmony.belling.service

import com.harmony.belling.domain.Template
import com.harmony.belling.model.DocxAddress
import com.harmony.belling.repository.TemplateRepository
import com.harmony.bitable.oapi.stream
import com.harmony.lark.LarkClient
import com.harmony.lark.service.docx.listCursor
import com.lark.oapi.service.docx.v1.DocxService
import com.lark.oapi.service.docx.v1.model.ListDocumentBlockReq
import org.springframework.stereotype.Service

/**
 * @author wuxin
 */
@Service
class TemplateService(
    private val templateRepository: TemplateRepository,
    larkClient: LarkClient,
) {

    val docxService = larkClient.unwrap(DocxService::class.java)

    fun getTemplate(key: String): Template {
        return templateRepository
            .scan {
                Template::key eq key
            }
            .stream()
            .first()
    }

    fun getTemplateContent(key: String): String {
        val template = getTemplate(key)
        return getTemplateContent(template)
    }

    private fun getTemplateContent(template: Template): String {
        val address = DocxAddress.fromUri(template.url!!.link)

        val request = ListDocumentBlockReq().apply {
            this.documentId = address.documentId
            this.pageSize = 500
        }

        val block = docxService.documentBlock().listCursor(request).stream().firstOrNull { it.blockType == 14 }
            ?: throw IllegalStateException("$address, 请在目标配置唯一一个代码块用于表示模板")

        return block.code.elements.map { it.textRun.content }.joinToString { it }
    }

}
