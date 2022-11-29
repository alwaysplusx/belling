package com.harmony.belling.domain

import com.harmony.bitable.annotations.Bitable
import com.harmony.bitable.annotations.Bitfield
import com.lark.oapi.service.bitable.v1.model.Person

@Bitable("人员信息")
class User : BaseDomain() {

    @Bitfield("姓名")
    var name: String? = null

    @Bitfield("人员")
    var person: Person? = null

    @Bitfield("部门ID")
    var departmentId: String? = null

    @Bitfield("部门名称")
    var departmentName: String? = null

}
