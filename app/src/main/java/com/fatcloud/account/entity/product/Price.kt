package com.fatcloud.account.entity.product

import com.fatcloud.account.entity.order.persional.Order
import java.io.Serializable
import java.math.BigDecimal

class Price : Serializable {


    var id: String? = ""

    /**
     * @see Order.mold
     * P1 ~ P7 ,
     * PP1 固定价格 PP2 动态价格
     */
    var mold: String? = ""

    var money: BigDecimal = BigDecimal.ZERO
    var name: String = ""

    var nativeIsSelect = false


    /**
     * 企业下拉表单
     * child 1 报税类型
     * child 2 收入情况
     */
    val childs: java.util.ArrayList<Price> = ArrayList()

}

