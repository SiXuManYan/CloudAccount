package com.fatcloud.account.entity.order

/**
 * 个人业务订单详情
 *
 */
data class PersonalOrderDetail(
    val accountId: String,
    val addr: String,
    val area: String,
    val bankNo: String,
    val businessLicenseImgUrl: String,
    val createDt: String,
    val delFlag: Int,
    val id: String,
    val idno: String,
    val legalPersonName: String,

    /**
     * @see  Order.mold
     */
    val mold: String,
    val moldText: String,
    val money: Int,
    val nickName: String,
    val no: String,
    val payState: String,
    val payStateText: String,
    val phoneOfBank: String,
    val productId: String,
    val productName: String,
    val productPriceId: String,
    val productPriceName: String,



    /**
     * OS1 待支付
     * OS2 取消订单
     * OS3 订单失效
     * OS4 支付中
     * OS5 已支付
     * OS6 已受理
     * OS7 办理中
     * OS8 已办结
     */
    val state: String,
    val stateText: String,
    val taxpayerNo: String,
    val updateDt: String,
    val username: String,
    val version: Int,
    val weixinPayMap: String
)

/*

{
    "id": "1268456920973312000",
    "delFlag": 0,
    "updateDt": "2020-06-04 16:19:57",
    "createDt": "2020-06-04 16:19:35",
    "version": 2,
    "no": "2020060400003117",
    "accountId": "1264788939516936192",
    "productId": "1262573783173038080",
    "productPriceId": "1263057059755065344",
    "money": 0.01,
    "mold": "P4",
    "state": "OS7",
    "payState": "PS4",
    "weixinPayMap": "{\"sign\": \"8F530264FB7D1CA2AEB920FE1EB9C24B\", \"appid\": \"wxd47f6921c3df9750\", \"package\": \"Sign=WXPay\", \"noncestr\": \"ia0HbHqoBEAYR1U0\", \"prepayid\": \"wx041619389915239288e1960b1703460000\", \"partnerid\": \"1528759041\", \"timestamp\": \"1591258675\"}",
    "area": "内蒙古自治区通辽市科左后旗常胜镇",
    "payStateText": "微信已支付",
    "productId": "1262573783173038080",
    "productPriceName": "个体户税务登记",
    "nickName": "你好123",
    "productPriceId": "1263057059755065344",
    "idno": "210102191203072090",
    "productName": "个体户税务登记",
    "moldText": "个体户税务登记",
    "legalPersonName": "呵呵哒nd",
    "taxpayerNo": "123456789123456789",
    "phoneOfBank": "13200010001",
    "money": 298,
    "stateText": "办理中",
    "bankNo": "6227003028940029516",
    "businessLicenseImgUrl": "ios_20200604161935153_202009906.png",
    "addr": "营业执照我",
    "username": "13200010001"
}
*/

