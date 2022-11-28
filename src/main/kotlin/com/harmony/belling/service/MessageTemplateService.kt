package com.harmony.belling.service

import com.harmony.belling.domain.MessageTemplate
import com.harmony.belling.repository.MessageTemplateRepository
import com.harmony.bitable.oapi.stream
import com.harmony.lark.LarkClient
import com.lark.oapi.service.wiki.v2.WikiService
import org.springframework.stereotype.Service

/**
 * @author wuxin
 */
@Service
class MessageTemplateService(
    private val messageTemplateRepository: MessageTemplateRepository,
    private val larkClient: LarkClient,
) {

    fun getMessageTemplate(key: String): MessageTemplate {
        return messageTemplateRepository
            .scan {
                MessageTemplate::key eq key
            }
            .stream()
            .first()
    }

    fun getMessageTemplateContent(key: String): String {
        val templateUrl: String = getMessageTemplate(key).url!!.link
        val wikiService = larkClient.unwrap(WikiService::class.java)
        return ""
    }

}
