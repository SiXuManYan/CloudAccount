package com.fatcloud.account.entity.upgrade

import java.io.Serializable

/**
 * app 版本升级
 */
class Upgrade :Serializable{
    val appExplain: String = ""
    val appForce: Int = 0
    val appPlatform: String = ""
    val appUrl: String = ""
    val appVersion: String = ""
    val createDt: String = ""
    val delFlag: Int = 0
    val id: String = ""
    val updateDt: String = ""
    val version: Int = 0
}

/*

{
    "code": "200",
    "msg": "成功",
    "data":
    {
        "id": "3",
        "delFlag": 0,
        "updateDt": "2020-06-08 14:51:04",
        "createDt": "2020-06-08 14:51:04",
        "version": 1,
        "appPlatform": "android",
        "appVersion": "1.1",
        "appForce": 0,
        "appExplain": "== 安卓 非强制更新1.1",
        "appUrl": ""
    }
}

*/
