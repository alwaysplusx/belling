package com.harmony.belling.domain

import com.harmony.bitable.annotations.Bitable
import com.harmony.bitable.annotations.Bitfield
import com.lark.oapi.service.bitable.v1.model.Url

/**
 * @author wuxin
 */
@Bitable("消息模板")
class Template : BaseDomain() {

    @Bitfield("模板名称")
    var name: String? = null

    @Bitfield("模板Key")
    var key: String? = null

    @Bitfield("模板地址")
    var url: Url? = null

}
