package com.fatcloud.account.entity.order.detail

import com.fatcloud.account.entity.order.IdentityImg

data class PersonalPackageDetail(
    val accountId: String,
    val addr: String,
    val area: String,
    val bankNo: String,
    val bankPhone: String,
    val businessScope: List<Int>,
    val businessScopeNames: String,
    val capital: String,
    val createDt: String,
    val delFlag: Int,
    val employedNum: String,
    val form: Int,
    val formName: String,
    val gender: String,
    val id: String,
    val idno: String,
    val imgs: List<IdentityImg>,
    val mold: String,
    val moldText: String,
    val money: String,
    val name0: String,
    val name1: String,
    val name2: String,
    val nation: String,
    val nickName: String,
    val no: String,
    val payState: String,
    val payStateText: String,
    val productId: String,
    val productName: String,
    val productPriceId: Int,
    val productPriceName: String,
    val realName: String,
    val state: String,
    val stateText: String,
    val tel: String,
    val updateDt: String,
    val username: String,
    val version: Int
)


/*
{
    "id": "1287622992234283008",
    "delFlag": 0,
    "updateDt": "2020-07-27 14:37:19",
    "createDt": "2020-07-27 13:36:59",
    "version": 1,
    "no": "2020072700000496",
    "accountId": "1267626363628552192",
    "productId": "1282868558430208000",
    "productPriceId": 10010104,
    "money": 360,
    "mold": "P10",
    "state": "OS3",
    "payState": "PS1",
    "capital": 50,
    "gender": "1",
    "nation": "汉族",
    "productPriceName": "收入100万至300万",
    "businessScopeNames": "投资咨询",
    "productName": "个人独资企业套餐",
    "moldText": "个人独资企业套餐",
    "formName": "商贸中心",
    "bankNo": "6666666666666666666",
    "tel": "17699999933",
    "addr": "哦哦哦",
    "area": "北京市北京市朝阳区",
    "imgs": [
    {
        "mold": "I1",
        "imgUrl": "dev/android/order/encryption1595828168496.jpg"
    },
    {
        "mold": "I2",
        "imgUrl": "dev/android/order/encryption1595828184228.jpg"
    }
    ],
    "payStateText": "未支付",
    "employedNum": 6,
    "nickName": "默默",
    "businessScope": [
    327
    ],
    "idno": "232321199410073514",
    "realName": "鲁班七号",
    "bankPhone": "17640339671",
    "form": 19,
    "stateText": "支付超时",
    "name2": "噩噩噩噩噩",
    "name1": "添加剂",
    "name0": "莫默默",
    "username": "17640339671"
}*/
