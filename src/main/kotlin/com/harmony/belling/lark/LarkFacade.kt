package com.harmony.belling.lark

import com.harmony.belling.lark.model.MessageContent
import com.harmony.belling.lark.model.MessageTemplate
import com.harmony.belling.support.TemplateEngine
import com.harmony.belling.utils.LarkUtils
import com.harmony.lark.LarkClient
import com.harmony.lark.ensureData
import com.lark.oapi.service.im.v1.ImService
import com.lark.oapi.service.im.v1.model.*
import org.springframework.stereotype.Component

@Component
class LarkFacade(
    larkClient: LarkClient,
    private val templateEngine: TemplateEngine,
) {

    private val imService = larkClient.unwrap(ImService::class.java)

    fun sendMessageText(receiveId: String, text: String): CreateMessageRespBody {
        return sendMessage(receiveId, MessageContent.ofText(text))
    }

    fun sendMessageCard(receiveId: String, content: String): CreateMessageRespBody {
        return sendMessage(receiveId, MessageContent.ofCard(content))
    }

    fun sendMessageCard(receiveId: String, template: MessageTemplate): CreateMessageRespBody {
        return sendMessageCard(receiveId, renderTemplate(template))
    }

    fun sendMessage(receiveId: String, msgContent: MessageContent): CreateMessageRespBody {
        val request = CreateMessageReq().apply {
            this.receiveIdType = LarkUtils.toReceiveIdType(receiveId)
            this.createMessageReqBody = CreateMessageReqBody().apply {
                this.receiveId = receiveId
                this.content = msgContent.getContentAsString()
                this.msgType = msgContent.msgType
            }
        }
        return imService.message().create(request).ensureData()
    }

    fun replyMessageText(messageId: String, text: String): ReplyMessageRespBody {
        return replyMessage(messageId, MessageContent.ofText(text))
    }

    fun replyMessageCard(messageId: String, content: String): ReplyMessageRespBody {
        return replyMessage(messageId, MessageContent.ofCard(content))
    }

    fun replyMessageCard(messageId: String, template: MessageTemplate): ReplyMessageRespBody {
        return replyMessageCard(messageId, renderTemplate(template))
    }

    fun replyMessage(messageId: String, msgContent: MessageContent): ReplyMessageRespBody {
        val request = ReplyMessageReq().apply {
            this.messageId = messageId
            this.replyMessageReqBody = ReplyMessageReqBody().apply {
                this.msgType = msgContent.msgType
                this.content = msgContent.getContentAsString()
            }
        }
        return imService.message().reply(request).ensureData()
    }

    fun updateMessageText(messageId: String, text: String) {
        return updateMessage(messageId, MessageContent.ofText(text))
    }

    fun updateMessageCard(messageId: String, content: String) {
        return updateMessage(messageId, MessageContent.ofCard(content))
    }

    fun updateMessageCard(messageId: String, template: MessageTemplate) {
        return updateMessageCard(messageId, renderTemplate(template))
    }

    fun updateMessage(messageId: String, msgContent: MessageContent) {
        val request = PatchMessageReq().apply {
            this.messageId = messageId
            this.patchMessageReqBody = PatchMessageReqBody().apply {
                this.content = msgContent.getContentAsString()
            }
        }
        imService.message().patch(request).ensureData()
    }

    fun getMessage(messageId: String): GetMessageRespBody {
        val request = GetMessageReq().apply {
            this.messageId = messageId
        }
        return imService.message().get(request).ensureData()
    }

    private fun renderTemplate(template: MessageTemplate): String {
        return templateEngine.render(template.key, template.dataModel)
    }

}
