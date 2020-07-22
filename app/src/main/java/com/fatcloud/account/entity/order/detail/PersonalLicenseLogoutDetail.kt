package com.fatcloud.account.entity.order.detail

import com.fatcloud.account.entity.order.IdentityImg

/**
 * 个体户营业执照注销 回显
 */
data class PersonalLicenseLogoutDetail(
    val accountId: String,
    val businessScope: String,
    val businessScopeNames: String,
    val createDt: String,
    val delFlag: Int,
    val enterpriseCode: String,
    val enterpriseName: String,
    val id: String,
    val idno: String,
    val imgsCommitment: List<IdentityImg>,
    val imgsIdno: List<IdentityImg>,
    val imgsLicense: List<IdentityImg>,
    val legalPersonName: String,
    val mold: String,
    val moldText: String,
    val money: String,
    val nickName: String,
    val no: String,
    val payState: String,
    val payStateText: String,
    val phone: String,
    val productId: String,
    val productName: String,
    val productPriceId: Int,
    val productPriceName: String,
    val reason: String,
    val state: String,
    val stateText: String,
    val updateDt: String,
    val username: String,
    val version: Int
)