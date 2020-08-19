package com.fatcloud.account.entity.order.persional.bank

import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.persional.NamePhoneBean
import java.math.BigDecimal

class PersonalBankDetailP8 {

    var accountId: String = ""
    var accountType: String = ""
    var addressDetailed: String = ""
    var addressPost: String = ""
    var addressRegistered: String = ""
    var bank: String = ""
    var businessScopeNames: String = ""
    var createDt: String = ""
    var currency: String = ""
    var delFlag: Int = 0
    var depositorName: String = ""
    var enterpriseCode: String = ""
    var id: String = ""
    var imgsDepositAccount: List<IdentityImg> = ArrayList()
    var imgsIdno: List<IdentityImg> = ArrayList()
    var imgsLicense: List<IdentityImg> = ArrayList()
    var mold: String = ""
    var moldText: String = ""
    var money: BigDecimal = BigDecimal.ZERO
    var nickName: String = ""
    var no: String = ""
    var payState: String = ""
    var payStateText: String = ""
    var personFinance: NamePhoneBean = NamePhoneBean()
    var personLegal: NamePhoneBean = NamePhoneBean()
    var personReconciliation: NamePhoneBean = NamePhoneBean()
    var personVerification1: NamePhoneBean = NamePhoneBean()
    var personVerification2: NamePhoneBean = NamePhoneBean()
    var productId: String = ""
    var productName: String = ""
    var productPriceId: Int = 0
    var productPriceName: String = ""
    var state: String = ""
    var stateText: String = ""
    var updateDt: String = ""
    var username: String = ""
    var version: Int = 0


}