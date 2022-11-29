package com.harmony.belling

import com.harmony.lark.LarkClient
import com.harmony.lark.ensureData
import com.lark.oapi.service.docx.v1.DocxService
import com.lark.oapi.service.docx.v1.model.RawContentDocumentReq
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author wuxin
 */
@SpringBootTest
class LarkClientTests {

    @Autowired
    lateinit var larkClient: LarkClient

    @Test
    fun test() {
        val docxService = larkClient.unwrap(DocxService::class.java)
        val request = RawContentDocumentReq.newBuilder().documentId("WnoZdGmEzoZMjyxKJcGcUEbXnne").build()
        val data = docxService.document().rawContent(request).ensureData()

        println(data.content)
    }

}
