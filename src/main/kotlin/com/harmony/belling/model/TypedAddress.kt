package com.harmony.belling.model

import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

/**
 * @author wuxin
 */
class TypedAddress(val type: String, val value: String) {

    companion object {
        fun fromUriString(uri: String): TypedAddress {
            val segments = UriComponentsBuilder.fromUriString(uri).build().pathSegments
            if (segments.size != 2) {
                throw IllegalArgumentException("illegal address $uri")
            }
            return TypedAddress(segments[0], segments[1])
        }
    }

    fun toUri(): URI = URI("https://feishu.cn/$type/$value")

}

fun TypedAddress.asWikiAddress(): WikiAddress = WikiAddress.fromUri(this.toUri())

fun TypedAddress.asDocxAddress(): DocxAddress = DocxAddress.fromUri(this.toUri())
