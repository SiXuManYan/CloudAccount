package com.fatcloud.account.entity.order.master

import com.fatcloud.account.entity.commons.BusinessScope

class MasterNaming {

    var birthday: String? = null

    /**
     * 经营主体
     */
    var businessEntity: String? = null

    /**
     * 经营产品
     */
    var businessProduct: String? = null

    /**
     * 经营范围 id
     * @see BusinessScope.id
     */
    var businessScope: ArrayList<String>? = null


    /**
     * 客户端计算出的最终金额
     */
    var money: String? = null
    var name: String? = null
    var phone: String? = null

    /**
     * 产品id
     */
    var productId: String? = null

    /**
     * 选中的产品价格id
     */
    var productPriceId: String? = null

    /**
     * 备注
     */
    var remark: String? = null
}