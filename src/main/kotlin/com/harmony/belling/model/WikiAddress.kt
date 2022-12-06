package com.harmony.belling.model

import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

/**
 * @author wuxin
 */
class WikiAddress(val token: String) {

    companion object {

        fun fromUri(uri: URI): WikiAddress {
            val segments = UriComponentsBuilder.fromUri(uri).build().pathSegments
            if (segments.size != 2 || segments[0] != "wiki") {
                throw IllegalArgumentException("$uri not wiki address")
            }
            return WikiAddress(segments[1])
        }

    }

    fun toUri() = URI("https://feishu.cn/wiki/${token}")

}
