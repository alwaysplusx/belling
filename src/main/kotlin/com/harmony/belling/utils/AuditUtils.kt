package com.harmony.belling.utils

import com.harmony.belling.domain.BaseDomain
import com.lark.oapi.service.bitable.v1.model.Person
import java.time.LocalDateTime

object AuditUtils {

    fun whenCreate(domain: BaseDomain) {
        domain.apply {
            if (this.createdAt == null) {
                this.createdAt = LocalDateTime.now()
            }
            if (this.createdBy == null) {
                this.createdBy = currentPerson()
            }
        }
    }

    fun whenUpdate(domain: BaseDomain) {
        domain.apply {
            if (this.updatedAt == null) {
                this.updatedAt = LocalDateTime.now()
            }
            if (this.updatedBy == null) {
                this.updatedBy = currentPerson()
            }
        }
    }

    private fun currentPerson(): Person {
        return Person()
    }

}
