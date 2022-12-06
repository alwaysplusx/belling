package com.harmony.belling.controller

import com.harmony.belling.domain.Template
import com.harmony.belling.service.TemplateService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/templates")
@RestController
class TemplateController(private val templateService: TemplateService) {

    @GetMapping
    fun get(key: String): Template {
        return templateService.getTemplate(key)
    }

    @GetMapping(path = ["/raw-content"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun rawContent(key: String): String {
        return templateService.getTemplateContent(key)
    }

}
