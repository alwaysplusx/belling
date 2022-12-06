package com.harmony.belling.domain

import com.harmony.bitable.annotations.Bitable
import com.harmony.bitable.annotations.Bitfield

@Bitable("项目信息")
class Project : BaseDomain() {

    @Bitfield("项目名称")
    var name: String? = null

    @Bitfield("项目描述")
    var description: String? = null

}
