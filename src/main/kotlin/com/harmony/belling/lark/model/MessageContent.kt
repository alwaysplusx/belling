package com.harmony.belling.lark.model

import com.google.gson.annotations.SerializedName
import com.lark.oapi.core.utils.Jsons

data class MessageContent(
    @SerializedName("content") var content: Any,
    @SerializedName("msg_type") var msgType: String,
) {

    companion object {

        fun ofText(text: String): MessageContent {
            return MessageContent(mapOf("text" to text), "text")
        }

        fun ofCard(content: String): MessageContent {
            return MessageContent(content, "interactive")
        }

    }

    fun getContentAsString(): String = content as? String ?: Jsons.DEFAULT.toJson(content)

}
