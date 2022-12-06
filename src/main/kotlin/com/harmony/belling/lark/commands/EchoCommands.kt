package com.harmony.belling.lark.commands

import com.harmony.belling.lark.LarkFacade
import com.harmony.belling.lark.annotations.LarkCommand
import com.harmony.belling.lark.annotations.Operation
import com.lark.oapi.service.im.v1.model.EventMessage
import com.lark.oapi.service.im.v1.model.MentionEvent

@LarkCommand("/echo")
class EchoCommands(private val larkFacade: LarkFacade) {

    @Operation("user_id")
    fun userId(message: EventMessage): String {
        return extractInfo(message.mentions) { it.id.userId }
    }

    @Operation("open_id")
    fun openId(message: EventMessage): String {
        return extractInfo(message.mentions) { it.id.openId }
    }

    @Operation("union_id")
    fun unionId(message: EventMessage): String {
        return extractInfo(message.mentions) { it.id.unionId }
    }

    @Operation("chat_id")
    fun chatId(message: EventMessage): String {
        return message.chatId
    }

    @Operation("message_id")
    fun messageId(message: EventMessage): String {
        return message.messageId
    }

    @Operation("parent_message_id")
    fun parentMessageId(message: EventMessage): String {
        return message.parentId
    }

    @Operation("root_message_id")
    fun rootMessageId(message: EventMessage): String {
        return message.rootId
    }

    @Operation("parent_message_content")
    fun parentMessageContent(message: EventMessage): String {
        return getMessageContent(message.parentId)
    }

    @Operation("root_message_content")
    fun rootMessageContent(message: EventMessage): String {
        return getMessageContent(message.rootId)
    }

    private fun getMessageContent(messageId: String): String {
        val parentMessage = larkFacade.getMessage(messageId).items[0]
        return parentMessage.body.content
    }

    private fun extractInfo(mentions: Array<MentionEvent>?, getter: (MentionEvent) -> Any): String {
        if (mentions.isNullOrEmpty()) {
            return "请在消息中 @ 相关人员"
        }
        return mentions.joinToString("\n") { "${it.name} = ${getter(it)}" }
    }

}
