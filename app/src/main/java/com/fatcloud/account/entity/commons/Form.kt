package com.fatcloud.account.entity.commons

/**
 * 组成形式
 */
data class Form(
    val createDt: String,
    val delFlag: Int,

    /**
     * 组成形式 的id
     * 17：商贸点
     * 18：商贸部
     */
    val id: String,
    val name: String,
    val updateDt: String,
    val version: Int,
    var nativeIsSelect: Boolean = false
)


/*

"forms":[
{
    "id":"17",
    "delFlag":0,
    "updateDt":"2020-05-28 10:25:34",
    "createDt":"2020-05-28 10:25:34",
    "version":1,
    "name":"商贸店"
},
{
    "id":"18",
    "delFlag":0,
    "updateDt":"2020-05-28 10:25:34",
    "createDt":"2020-05-28 10:25:34",
    "version":1,
    "name":"商贸部"
}
]
*/
