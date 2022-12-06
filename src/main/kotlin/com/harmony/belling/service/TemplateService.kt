package com.harmony.belling.service

import com.harmony.belling.domain.Template
import com.harmony.belling.model.*
import com.harmony.belling.repository.TemplateRepository
import com.harmony.bitable.oapi.toStream
import com.harmony.lark.LarkClient
import com.harmony.lark.ensureData
import com.harmony.lark.service.docx.listCursor
import com.lark.oapi.service.docx.v1.DocxService
import com.lark.oapi.service.docx.v1.model.ListDocumentBlockReq
import com.lark.oapi.service.wiki.v2.WikiService
import com.lark.oapi.service.wiki.v2.model.GetNodeSpaceReq
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

    val wikiService = larkClient.unwrap(WikiService::class.java)

    fun getTemplate(key: String): Template {
        return templateRepository
            .scan {
                Template::key eq key
            }
            .toStream().first()
    }

    fun getTemplateContent(key: String): String {
        val template = getTemplate(key)
        return getTemplateContent(template)
    }

    private fun getTemplateContent(template: Template): String {
        val address = TypedAddress.fromUriString(template.url!!.link)

        val docxAddress = when (address.type) {
            "wiki" -> getWikiDocAddress(address.asWikiAddress())
            "docx" -> address.asDocxAddress()
            else -> throw IllegalStateException("$address not docx address")
        }

        val request = ListDocumentBlockReq().apply {
            this.documentId = docxAddress.documentId
            this.pageSize = 500
        }

        val block = docxService.documentBlock().listCursor(request).toStream().firstOrNull { it.blockType == 14 }
            ?: throw IllegalStateException("$address, 请在目标配置唯一一个代码块用于表示模板")

        return block.code.elements.map { it.textRun.content }.joinToString("") { it }
    }

    private fun getWikiDocAddress(wikiAddress: WikiAddress): DocxAddress {
        val request = GetNodeSpaceReq().apply {
            this.token = wikiAddress.token
        }
        val data = wikiService.space().getNode(request).ensureData()
        if ("docx" != data.node.objType) {
            throw IllegalStateException("$wikiAddress not docx")
        }
        return DocxAddress(data.node.objToken)
    }

}
