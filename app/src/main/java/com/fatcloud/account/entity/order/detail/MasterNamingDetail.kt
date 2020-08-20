package com.fatcloud.account.entity.order.detail

data class MasterNamingDetail(
    val accountId: String,
    val birthday: String,
    val businessEntity: String,
    val businessProduct: String,
    val businessScopeNames: String,
    val createDt: String,
    val delFlag: Int,
    val id: String,
    val mold: String,
    val moldText: String,
    val money: Int,
    val name: String,
    val nickName: String,
    val no: String,
    val payState: String,
    val payStateText: String,
    val phone: String,
    val productId: String,
    val productName: String,
    val productPriceId: Int,
    val productPriceName: String,
    val remark: String,
    val state: String,
    val stateText: String,
    val updateDt: String,
    val username: String
)