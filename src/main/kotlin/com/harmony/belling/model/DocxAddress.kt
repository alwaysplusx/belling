package com.harmony.belling.model

import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

data class DocxAddress(val documentId: String) {

    companion object {

        fun fromUri(uri: URI): DocxAddress {
            val segments = UriComponentsBuilder.fromUri(uri).build().pathSegments
            if (segments.size != 2 || segments[0] != "docx") {
                throw IllegalArgumentException("$uri not docx address")
            }
            return DocxAddress(segments[1])
        }

    }

    fun toUri() = URI("https://feishu.cn/docx/${documentId}")

    override fun toString(): String {
        return toUri().toString()
    }

}
