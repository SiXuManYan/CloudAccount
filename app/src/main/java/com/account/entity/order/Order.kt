package com.account.entity.order

import java.math.BigDecimal

/**
 * 订单相关实体
 */
data class Order(
    val createDt: String = "",
    val id: String = "",
    val imgUrl: String = "",
    val mold: String = "",
    val money: BigDecimal = BigDecimal.ZERO,
    val no: String = "",
    val productId: String = "",
    val productName: String = "",
    val productPriceId: String = "",
    val productPriceName: String = "",
    val state: String = "",
    val stateText: String = ""
)

/*

{
    "id": "1265248114348916736",
    "no": "2020052600007020",
    "money": 0.01,
    "productId": "1262568059554496512",
    "productPriceId": "3",
    "productName": "企业套餐办理",
    "productPriceName": "无业务,零申报",
    "mold": "P2",
    "state": "OS3",
    "stateText": "支付超时",
    "createDt": "2020-05-26 19:47:12",
    "imgUrl": "product/web-20206313291886329903-logo.jpg"
}
*/
