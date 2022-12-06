package com.harmony.belling.lark.handler.event

import com.lark.oapi.service.im.v1.ImService
import com.lark.oapi.service.im.v1.model.P2MessageReceiveV1
import com.lark.oapi.service.im.v1.model.P2MessageReceiveV1Data
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.stereotype.Component

@Component
class MessageReceiveHandler(private val messageHandler: ObjectProvider<MessageHandler>) :
    ImService.P2MessageReceiveV1Handler() {

    var throwExceptionWhenHandlerNotFound: Boolean = true

    private val log = LoggerFactory.getLogger(MessageReceiveHandler::class.java)

    override fun handle(event: P2MessageReceiveV1) {
        log.info("receive lark message: {}", event.event)

        val handler = messageHandler.firstOrNull { it.canHandle(event.event) }
        if (handler == null && throwExceptionWhenHandlerNotFound) {
            throw IllegalStateException("message handler not found")
        }

        if (handler == null) {
            log.warn("message handler not found")
        }

        handler?.handle(event.event)
    }

    interface MessageHandler {

        fun canHandle(data: P2MessageReceiveV1Data): Boolean

        fun handle(data: P2MessageReceiveV1Data)

    }

}
