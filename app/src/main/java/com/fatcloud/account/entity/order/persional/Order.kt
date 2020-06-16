package com.fatcloud.account.entity.order.persional

import java.math.BigDecimal

/**
 * 订单相关实体
 */
data class Order(
    val createDt: String = "",
    val id: String = "",
    val imgUrl: String = "",

    /**
     * 产品类型
     * P1 个体户营业执照办理
     * P2 企业套餐
     * P3 个体户代理记账
     * P4 个体户税务登记
     * P5 个体户营业执照变更
     * P6 个体户营业执照注销
     * P7 大师起名
     */
    val mold: String = "",
    val money: BigDecimal = BigDecimal.ZERO,

    /**
     * 订单号 order number
     */
    val no: String = "",
    val productId: String = "",
    val productName: String = "",
    val productPriceId: String = "",
    val productPriceName: String = "",

    /**
     * 订单状态类型
     * OS 1 待支付
     * OS 2 取消订单
     * OS 3 支付超时
     * OS 4 支付中
     * OS 5 已支付
     * OS 6 已受理
     * OS 7 办理中
     * OS 8 已办结
     * OS unsubmitted 未提交
     */
    val state: String = "",

    /**
     * 订单状态类型对应的文案
     */
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
