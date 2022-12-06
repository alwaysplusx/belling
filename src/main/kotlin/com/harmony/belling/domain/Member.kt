package com.harmony.belling.domain

import com.harmony.belling.constants.MemberType
import com.harmony.bitable.BitfieldType
import com.harmony.bitable.annotations.Bitable
import com.harmony.bitable.annotations.Bitfield
import com.lark.oapi.service.bitable.v1.model.Person

@Bitable("项目成员信息")
class Member : BaseDomain() {

    @Bitfield("项目名称")
    var projectName: String? = null

    @Bitfield("项目人员")
    var person: Person? = null

    @Bitfield(name = "成员类型", type = BitfieldType.MULTI_SELECT)
    var memberTypes: List<MemberType> = listOf()

}
