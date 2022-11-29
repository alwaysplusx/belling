package com.harmony.belling.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class TemplateServiceTest {

    @Autowired
    lateinit var templateService: TemplateService

    @Test
    fun test_get_template_content() {
        val content = templateService.getTemplateContent("happen")
        println(content)
    }

}
