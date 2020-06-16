package com.fatcloud.account.entity.local

import com.fatcloud.account.entity.order.enterprise.Shareholder

/**
 * 企业套餐
 */
data class EnterprisePackage(
    val addr: String,
    val area: String,
    val bankNo: String,
    val bankPhone: String,
    val businessScope: ArrayList<Int> = ArrayList(),
    val enterpriseName0: String,
    val enterpriseName1: String,
    val enterpriseName2: String,
    val income: Int,
    val investMoney: Int,
    val investYearNum: Int,
    val money: Int,
    val productId: Int,
    val productPriceId: Int,
    val shareholders: List<Shareholder>
)