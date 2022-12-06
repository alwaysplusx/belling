package com.harmony.belling.controller.callback

import com.harmony.belling.utils.LarkUtils.parseEventReq
import com.harmony.belling.utils.LarkUtils.writeEventResp
import com.lark.oapi.card.CardActionHandler
import com.lark.oapi.event.EventDispatcher
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.NativeWebRequest

@RequestMapping("/callback/lark")
@RestController
class LarkController(
    private val eventDispatcher: EventDispatcher,
    private val cardActionHandler: CardActionHandler,
) {

    @RequestMapping("/event")
    fun onEvent(@RequestBody(required = false) body: String?, webRequest: NativeWebRequest) {

        val eventReq = parseEventReq(body ?: "", webRequest)
        val eventResp = eventDispatcher.handle(eventReq)

        writeEventResp(eventResp, webRequest)
    }

    @RequestMapping("/card")
    fun onCard(@RequestBody(required = false) body: String?, webRequest: NativeWebRequest) {

        val eventReq = parseEventReq(body ?: "", webRequest)
        val eventResp = cardActionHandler.handle(eventReq)

        writeEventResp(eventResp, webRequest)
    }

}
