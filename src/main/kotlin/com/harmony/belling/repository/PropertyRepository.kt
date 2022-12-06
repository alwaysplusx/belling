package com.harmony.belling.repository

import com.harmony.belling.domain.Property
import com.harmony.bitable.repository.BitableRepository
import org.springframework.stereotype.Repository

@Repository
interface PropertyRepository : BitableRepository<Property, String> {
}
