package com.fatcloud.account.entity.commons

/**
 * 经营范围
 */
data class BusinessScope(
    val childs: ArrayList<BusinessScope> = ArrayList(),
    val createDt: String,
    val delFlag: Int,

    /**
     *  经营范围的id
     *  计算机、网络科技 id:1
     */
    val id: String,
    val mold: String,
    val name: String,
    val pid: String,
    val updateDt: String,
    val version: Int,

    var nativeIsSelect: Boolean
)

/*

{
    "name":"计算机、网络科技",
    "mold":"S1",
    "pid":"0",
    "id":"1",
    "delFlag":0,
    "updateDt":"2020-05-28 09:11:37",
    "createDt":"2019-11-27 22:56:29",
    "version":1,
    "childs":[
    {
        "name":"计算机系统设计",
        "mold":"S1",
        "pid":"1",
        "id":"2",
        "delFlag":0,
        "updateDt":"2020-05-28 09:11:37",
        "createDt":"2019-11-27 22:57:01",
        "version":1
    }
    ]

}

*/
