package com.harmony.belling.lark.handler.event.msg

import com.harmony.belling.lark.model.MessageContent

interface CommandHandler {

    fun getPrefix(): String

    fun handle(context: CommandContext): MessageContent?

}

