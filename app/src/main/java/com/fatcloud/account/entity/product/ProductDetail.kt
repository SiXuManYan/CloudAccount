package com.fatcloud.account.entity.product

import java.math.BigDecimal

data class ProductDetail(
    val bannerImgUrls: List<String> = ArrayList(),
    val detailImgUrls: List<String> = ArrayList(),
    val id: String = "",
    val introduce: String = "",
    val logoImgUrl: String = "",
    val mold: String = "",
    val money: BigDecimal = BigDecimal.ZERO,
    val name: String = "",
    val prices: List<Price> = ArrayList(),
    val state: String = ""
)