package com.fatcloud.account.entity.order.persional

import com.fatcloud.account.common.Constants

/**
 * 订单相关实体
 */
data class Order(

    var createDt: String = "",

    /** order id  */
    var id: String = "",
    var imgUrl: String = "",

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
    var mold: String = "",
    var money: String? = "",

    /**
     * 订单号 order number
     */
    var no: String? = "",
    var productId: String? = "",
    var productName: String? = "",
    var productPriceId: String? = "",
    var productPriceName: String? = "",

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
     * OS OS_UN_SUBMITTED 未提交
     * @see Constants.OS1
     * @see Constants.OS2
     * @see Constants.OS3
     * @see Constants.OS4
     * @see Constants.OS5
     * @see Constants.OS6
     * @see Constants.OS7
     * @see Constants.OS8
     * @see Constants.OS_UN_SUBMITTED
     */
    var state: String = "",

    /**
     * 订单状态类型对应的文案
     */
    var stateText: String = "",

    /**
     * 用户草稿数据类型
     */
    var nativeExtraDraftObject: Any? = null,

    /**
     * 草稿箱
     */
    var nativeProductId: String = ""

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

/*

{
    "code": "200",
    "msg": "成功",
    "data": {
    "orderId": "1273145220543807488",
    "orderNo": "2020061700002066",
    "money": 0.01,
    "productLogoImgUrl": "https://ftacloud-bucket-public.oss-cn-qingdao.aliyuncs.com/product/web-20206313265265765849-logo.jpg",
    "productName": "个体户营业执照办理",
    "createDt": "2020-06-17 14:47:29"
}
}

*/

