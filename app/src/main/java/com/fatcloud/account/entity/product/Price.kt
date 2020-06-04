package com.fatcloud.account.entity.product

import java.math.BigDecimal

data class Price(
    val id: String = "",
    val mold: String = "",
    val money: BigDecimal = BigDecimal.ZERO,
    val name: String = ""
)