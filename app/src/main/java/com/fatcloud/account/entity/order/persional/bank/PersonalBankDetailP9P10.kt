package com.fatcloud.account.entity.order.persional.bank

import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.persional.NamePhoneBean

data class PersonalBankDetailP9P10(
    val accountType: String = "",
    val addressDetailed: String = "",
    val addressPost: String = "",
    val addressRegistered: String = "",
    val bank: String = "",
    val createDt: String = "",
    val currency: String = "",
    val depositorName: String = "",
    val enterpriseCode: String = "",
    val imgsIdno: List<IdentityImg> = ArrayList(),
    val imgsLicense: List<IdentityImg> = ArrayList(),
    var imgsDepositAccount: List<IdentityImg> = ArrayList(),
    val orderWorkId: String = "",
    val personFinance: NamePhoneBean = NamePhoneBean(),
    val personLegal: NamePhoneBean = NamePhoneBean(),
    val personReconciliation: NamePhoneBean = NamePhoneBean(),
    val personVerification1: NamePhoneBean = NamePhoneBean(),
    val personVerification2: NamePhoneBean = NamePhoneBean(),
    val settingStateText: String = "",
    val state: String = "",
    val stateText: String = ""
)