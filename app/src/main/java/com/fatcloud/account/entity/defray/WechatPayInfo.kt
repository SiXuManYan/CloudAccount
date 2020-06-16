package com.fatcloud.account.entity.defray

data class WechatPayInfo(
    val appid: String,
    val noncestr: String,
    val `package`: String,
    val partnerid: String,
    val prepayid: String,
    val sign: String,
    val timestamp: String
)

/*

{
    "sign":"A169C6080305A3D03926679AB8A7BD44",
    "appid":"wxd47f6921c3df9750",
    "package":"Sign=WXPay",
    "noncestr":"xtU6bLzDh5WDVhk7",
    "prepayid":"wx16094228891634920643e94e1839729300",
    "partnerid":"1528759041",
    "timestamp":"1592271747"
}

*/
