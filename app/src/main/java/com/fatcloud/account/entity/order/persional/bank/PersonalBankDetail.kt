package com.fatcloud.account.entity.order.persional.bank

import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.persional.NamePhoneBean

data class PersonalBankDetail(

    val accountId: String,
    val accountType: String,
    val addressDetailed: String,
    val addressPost: String,
    val addressRegistered: String,
    val bank: String,
    val businessScope: String,
    val businessScopeNames: String,
    val createDt: String,
    val currency: String,
    val delFlag: Int,
    val depositorName: String,
    val enterpriseCode: String,
    val id: String,
    val imgsDepositAccount: List<IdentityImg>,
    val imgsIdno: List<IdentityImg>,
    val imgsLicense: List<IdentityImg>,
    val mold: String,
    val moldText: String,
    val money: Int,
    val nickName: String,
    val no: String,
    val payState: String,
    val payStateText: String,
    val personFinance: NamePhoneBean,
    val personLegal: NamePhoneBean,
    val personReconciliation: NamePhoneBean,
    val personVerification1: NamePhoneBean,
    val personVerification2: NamePhoneBean,
    val productId: String,
    val productName: String,
    val productPriceId: Int,
    val productPriceName: String,
    val state: String,
    val stateText: String,
    val updateDt: String,
    val username: String,
    val version: Int,
    val weixinPayMap: String
)