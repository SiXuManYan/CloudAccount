package com.fatcloud.account.entity.message

import com.fatcloud.account.common.Constants

class Message {



    var accountId: String = ""
    var businessId: String = ""
    var content: String = ""
    var id: String = ""

    /**
     *    READ0 未读读
     *    READ1 已读
     */
    var readFlag: String = ""
    var title: String = ""

    /**
     * 新闻推送
     * @see Constants.NOTICE1
     * 订单推送
     * @see Constants.NOTICE2
     * 分销推送
     * @see Constants.NOTICE3
     */
    var mold: String = ""

    var img:String = ""

}


/*

{
        "id" : "1296372278556098560",
        "accountId" : "1267626363628552192",
        "mold" : "NOTICE2",
        "businessId" : "1291194918403833856",
        "title" : "业务办理状态通知",
        "content" : "您办理的个体户银行对公账户业务正在办理中，请耐心等待！",
        "readFlag" : "READ1",
        "img" : "product/web-2020710925369952208-app-p8-00-logo.png"
      }
*/
