package com.harmony.belling.domain

import com.harmony.bitable.annotations.Bitable
import com.harmony.bitable.annotations.Bitfield

@Bitable("系统配置")
class Property : BaseDomain() {

    @Bitfield("配置Key")
    var key: String? = null

    @Bitfield("配置值")
    var value: String? = null

    @Bitfield("配置描述")
    var description: String? = null

}
