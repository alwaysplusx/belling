package com.harmony.belling.model

import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

data class DocxAddress(val documentId: String) {

    companion object {

        fun fromUri(uri: String): DocxAddress {
            val segments = UriComponentsBuilder.fromUriString(uri).build().pathSegments
            return DocxAddress(segments[segments.size - 1])
        }

    }

    fun toUri() = URI("https://feishu.cn/docx/${documentId}")

    override fun toString(): String {
        return toUri().toString()
    }

}
