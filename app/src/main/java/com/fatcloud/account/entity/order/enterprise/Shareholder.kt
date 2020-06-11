package com.fatcloud.account.entity.order.enterprise

import com.fatcloud.account.entity.order.IdentityImg


/**
 *  股权人相关信息
 */
class Shareholder(
    val idno: String,
    val idnoAddr: String,
    val imgs: ArrayList<IdentityImg> = ArrayList(),

    /**
     * SH1 	企业法人
     * SH2 	监事
     * SH3  股东
     */
    val mold: String,
    val name: String,
    val phone: String,
    val shareProportion: String
)

/*

{
    "idno" : "210102191203072090",
    "imgs" : [ {
    "mold" : "I1",
    "imgUrl" : "ios_20200604140230577_202003272.png"
}, {
    "mold" : "I2",
    "imgUrl" : "ios_20200604140231136_202008896.png"
} ],
    "mold" : "SH1",
    "name" : "佛举行",
    "phone" : "13200010001",
    "idnoAddr" : "佛裤子",
    "shareProportion" : 8
}*/
