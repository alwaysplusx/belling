package com.harmony.belling.service

import com.harmony.belling.domain.Property
import com.harmony.belling.repository.PropertyRepository
import org.springframework.stereotype.Service

@Service
class PropertyService(propertyRepository: PropertyRepository) : BaseService<Property>(propertyRepository) {

    fun getValue(key: String): String {
        return repository.first {
            Property::key eq key
        }.value!!
    }

}
