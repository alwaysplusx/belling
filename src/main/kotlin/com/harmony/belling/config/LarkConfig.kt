package com.harmony.belling.config

import com.harmony.belling.utils.LarkUtils.addEventHandler
import com.lark.oapi.card.CardActionHandler
import com.lark.oapi.event.EventDispatcher
import com.lark.oapi.event.IEventHandler
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LarkConfig {

    @Value("\${lark.event.verification-token}")
    private lateinit var verificationToken: String

    @Value("\${lark.event.encrypt-key:}")
    private var encryptKey: String? = null

    @Bean
    fun eventDispatcher(eventHandler: ObjectProvider<IEventHandler<*>>): EventDispatcher {
        val builder = EventDispatcher.newBuilder(verificationToken, encryptKey)
        eventHandler.forEach { builder.addEventHandler(it) }
        return builder.build()
    }

    @Bean
    fun cardActionHandler(): CardActionHandler {
        return CardActionHandler.newBuilder(verificationToken, encryptKey, null).build()
    }

}
