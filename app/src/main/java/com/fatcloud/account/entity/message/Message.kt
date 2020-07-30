package com.fatcloud.account.entity.message

import com.fatcloud.account.common.Constants

class Message {
    var accountId: String = ""
    var businessId: String = ""
    var content: String = ""
    var id: String = ""
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
}


/*

{
    "id": "1288663418709999616",
    "accountId": "1267626363628552192",
    "mold": "NOTICE2",
    "businessId": "1288032308292485120",
    "title": "业务办理状态通知",
    "content": "您办理的大师起名业务已办结，请注意查验！",
    "readFlag": "READ1"
}

*/
