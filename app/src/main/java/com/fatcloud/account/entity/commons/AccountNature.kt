package com.fatcloud.account.entity.commons

import com.fatcloud.account.common.Constants

/**
 * 账户性质

 */
data class AccountNature(

    val createTime: String,
    val id: String,
    val name: String,
    val type: String,
    val typeName: String,

    /**
     * 银行账户性质类型
     * @see Constants.AN1
     * @see Constants.AN2
     * @see Constants.AN3
     */
    val value: String
)

/*

"accountNatues":[
{
    "id":"132",
    "value":"AN1",
    "createTime":"2020-05-23 16:11:55",
    "type":"account_nature_type",
    "typeName":"银行账户性质类型",
    "name":"基本户"
},
{
    "id":"133",
    "value":"AN2",
    "createTime":"2020-05-23 16:12:10",
    "type":"account_nature_type",
    "typeName":"银行账户性质类型",
    "name":"一般户"
},
{
    "id":"134",
    "value":"AN3",
    "createTime":"2020-05-23 16:12:17",
    "type":"account_nature_type",
    "typeName":"银行账户性质类型",
    "name":"专用户"
}
]*/
