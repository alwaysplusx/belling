package com.harmony.belling.domain

import com.harmony.belling.constants.AlarmStatus
import com.harmony.bitable.BitfieldType
import com.harmony.bitable.annotations.Bitable
import com.harmony.bitable.annotations.Bitdex
import com.harmony.bitable.annotations.Bitfield
import com.lark.oapi.service.bitable.v1.model.Person
import java.time.LocalDateTime

/**
 * @author wuxin
 */
@Bitable("告警记录")
class Alarm : BaseDomain() {

    @Bitdex
    @Bitfield("告警服务")
    var serviceName: String? = null

    @Bitfield("告警原因")
    var reason: String? = null

    @Bitfield(name = "告警状态", type = BitfieldType.SINGLE_SELECT)
    var status: AlarmStatus? = null

    @Bitfield("告警总结")
    var summary: String? = null

    @Bitfield("解决时间")
    var resolvedAt: LocalDateTime? = null

    @Bitfield(name = "解决人", type = BitfieldType.PERSON)
    var resolvedBy: Person? = null

    @Bitfield("告警来源")
    var source: String? = null

}
