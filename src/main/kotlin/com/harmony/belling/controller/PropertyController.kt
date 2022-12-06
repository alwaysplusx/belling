package com.harmony.belling.controller

import com.harmony.belling.domain.Property
import com.harmony.belling.service.PropertyService
import com.harmony.lark.model.PageResult
import com.harmony.lark.model.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/properties")
@RestController
class PropertyController(private val propertyService: PropertyService) {

    @GetMapping
    fun page(pageable: Pageable): PageResult<Property> {
        return propertyService.page(pageable)
    }

}
