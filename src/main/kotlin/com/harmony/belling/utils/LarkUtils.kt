package com.harmony.belling.utils

import com.google.gson.JsonObject
import com.lark.oapi.core.request.EventReq
import com.lark.oapi.core.response.EventResp
import com.lark.oapi.core.utils.Jsons
import com.lark.oapi.event.EventDispatcher
import com.lark.oapi.event.IEventHandler
import com.lark.oapi.service.im.v1.model.EventMessage
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.springframework.web.context.request.NativeWebRequest
import java.lang.reflect.Method
import java.nio.charset.StandardCharsets

object LarkUtils {

    private val EVENT_HANDLER_SETTER_MAPPING: Map<Class<*>, Method> =
        EventDispatcher.Builder::class.java.methods
            .filter {
                it.name.startsWith("on")
                        && it.parameterCount == 1
                        && IEventHandler::class.java.isAssignableFrom(it.parameterTypes[0])
            }.associateBy { it.parameterTypes[0] }

    private val EMAIL_REGEX = "\\w[-\\w.+]*@([A-Za-z\\d][-A-Za-z\\d]+\\.)+[A-Za-z]{2,14}".toRegex()

    private fun findApplyMethod(eventType: Class<*>): Method? {
        val method = EVENT_HANDLER_SETTER_MAPPING[eventType]
        if (method != null) {
            return method
        }
        return EVENT_HANDLER_SETTER_MAPPING.entries.firstOrNull { it.key.isAssignableFrom(eventType) }?.value
    }

    fun toUserIdType(id: String): String {
        if (id.startsWith("on_")) {
            return "union_id"
        } else if (id.startsWith("ou_")) {
            return "open_id"
        } else if (StringUtils.isAlphanumeric(id)) {
            return "user_id"
        }
        throw IllegalArgumentException("unknown lark user id type")
    }

    fun toReceiveIdType(id: String): String {
        if (id.startsWith("oc_")) {
            return "chat_id";
        } else if (EMAIL_REGEX.matches(id)) {
            return "email";
        }
        return toUserIdType(id);
    }

    fun parseEventReq(body: String, webRequest: NativeWebRequest): EventReq {
        val headers = mutableMapOf<String, List<String>?>()
        for (name in webRequest.headerNames) {
            headers[name] = webRequest.getHeaderValues(name)?.toList()
        }
        return EventReq().apply {
            this.headers = headers
            this.body = body.toByteArray()
            this.httpPath = webRequest.contextPath
        }
    }

    fun writeEventResp(eventResp: EventResp, webRequest: NativeWebRequest) {
        val httpResponse = webRequest.getNativeResponse(HttpServletResponse::class.java)!!
        for (header in eventResp.headers) {
            header.value.forEach {
                httpResponse.addHeader(header.key, it)
            }
        }
        httpResponse.status = eventResp.statusCode
        httpResponse.writer.write(String(eventResp.body, StandardCharsets.UTF_8))
    }

    fun getEventMessageTextContent(message: EventMessage, excludeMentions: Boolean = false): String {
        if (message.messageType != "text") {
            throw IllegalStateException("not text message")
        }

        val content = Jsons.DEFAULT.fromJson(message.content, JsonObject::class.java)
        var text = content["text"].asString
        if (excludeMentions && message.mentions != null && message.mentions.isNotEmpty()) {
            for (mention in message.mentions) {
                text = text.replace(mention.key, "")
            }
        }
        return text.trim()
    }

    fun EventDispatcher.Builder.addEventHandler(eventHandler: IEventHandler<*>): EventDispatcher.Builder {
        val method = findApplyMethod(eventHandler.javaClass)
            ?: throw IllegalStateException("unknown event handler type ${eventHandler.javaClass}")
        method.invoke(this, eventHandler)
        return this
    }

}
