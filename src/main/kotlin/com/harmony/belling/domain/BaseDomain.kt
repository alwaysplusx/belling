package com.harmony.belling.domain

import com.harmony.bitable.BitfieldType
import com.harmony.bitable.annotations.BitId
import com.harmony.bitable.annotations.Bitfield
import com.lark.oapi.service.bitable.v1.model.Person
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

/**
 * @author wuxin
 */
abstract class BaseDomain {

    @Id
    @BitId
    var id: String? = null

    @Bitfield(name = "创建时间", type = BitfieldType.CREATED_AT)
    var createdAt: LocalDateTime? = null

    @Bitfield(name = "创建人", type = BitfieldType.CREATED_BY)
    var createdBy: Person? = null

    @Bitfield(name = "更新时间", type = BitfieldType.UPDATED_AT)
    var updatedAt: LocalDateTime? = null

    @Bitfield(name = "更新人", type = BitfieldType.UPDATED_BY)
    var updatedBy: Person? = null

    override fun toString(): String {
        return String.format("%s(id=%s)", javaClass.simpleName, id)
    }

}
