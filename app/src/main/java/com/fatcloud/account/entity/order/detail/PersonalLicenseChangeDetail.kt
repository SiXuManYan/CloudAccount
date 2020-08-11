package com.fatcloud.account.entity.order.detail

import com.fatcloud.account.entity.order.IdentityImg

/**
 * 个体户 营业执照变更回显
 */
data class PersonalLicenseChangeDetail(
    val accountId: String,
//    val businessScope: String,
    val businessScopeNames: String,
    val createDt: String,
    val delFlag: Int,
    val enterpriseName0: String,
    val enterpriseName1: String,
    val enterpriseName2: String,
    val id: String,
    val idno: String,
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
    val state: String,
    val stateText: String,
    val updateDt: String,
    val username: String,
    val version: Int
)