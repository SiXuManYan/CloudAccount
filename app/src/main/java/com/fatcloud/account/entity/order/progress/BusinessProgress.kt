package com.fatcloud.account.entity.order.progress

/**
 * 企业业务办理流程进度
 */
data class BusinessProgress(
    val id: String,
    val code: String,
    val state: String,
    val stateText: String,
    val productWorkName: String,
    val productWorkIntroduce: String,
    val mold: String

)




/*
{
    "code": "200",
    "msg": "成功",
    "data": [
    {
        "id": "1268456920998477824",
        "code": "PW2",
        "state": "OS7",
        "stateText": "办理中",
        "productWorkName": "个体户税务登记开立",
        "productWorkIntroduce": "个体户税务登记",
        "mold": "P4"
    }
    ]
}
*/
