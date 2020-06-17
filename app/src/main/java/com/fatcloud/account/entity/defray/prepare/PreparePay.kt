package com.fatcloud.account.entity.defray.prepare

import java.math.BigDecimal


/**
 * 表单添加成功后，服务器返回数据结构
 * 用于跳转至准备支付页面
 */
data class PreparePay(
    val createDt: String,
    val money: BigDecimal = BigDecimal.ZERO,
    val orderId: String,
    val orderNo: String,
    val productLogoImgUrl: String,
    val productName: String
)


/*

"data": {
    "orderId": "1273145220543807488",
    "orderNo": "2020061700002066",
    "money": 0.01,
    "productLogoImgUrl": "https://ftacloud-bucket-public.oss-cn-qingdao.aliyuncs.com/product/web-20206313265265765849-logo.jpg",
    "productName": "个体户营业执照办理",
    "createDt": "2020-06-17 14:47:29"
}

*/
