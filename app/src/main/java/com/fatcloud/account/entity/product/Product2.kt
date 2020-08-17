package com.fatcloud.account.entity.product

import java.math.BigDecimal

/**
 * 产品列表model
 */
data class Product2(
    val id: String = "",
    val imgurl: String = "",
    val introduce: String = "",
    val money: BigDecimal = BigDecimal.ZERO,
    val name: String = "",
    val orderCount: Int = 0,
    val mold: String = ""
)

/*

{
    "id": "1260886938362052608",
    "name": "个体户营业执照办理",
    "introduce": "个体户营业执照办理",
    "imgurl": "web-202052015434939001785-切图@2X_190.jpg",
    "money": 298,
    "orderCount": 42
}

*/
