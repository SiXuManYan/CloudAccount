package com.fatcloud.account.entity.order.progress

/**
 * 企业业务办理流程进度
 */
data class BusinessProgress(
    val id: String,

    /**
     * PW1 营业执照办理
     * PW2 税务登记办理
     * PW3 银行账户办理
     * PW4 代理记账办理
     */
    val code: String,


    /**
     * 个人业务：
     * OS1 待支付
     * OS2 取消订单
     * OS3 支付超时
     * OS4 支付中
     * OS5 已支付
     * OS6 已受理
     * OS7 办理中
     * OS8 已办结
     * OS unsubmitted 未提交
     *
     * 企业套餐
     * OW1 已激活
     * OW2 办理中
     * OW3 已办结
     * OW4 未激活
     *
     */
    val state: String,
    val stateText: String,
    val productWorkName: String,
    val productWorkIntroduce: String,

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
