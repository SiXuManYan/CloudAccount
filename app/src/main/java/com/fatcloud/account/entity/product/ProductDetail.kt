package com.fatcloud.account.entity.product

import java.io.Serializable
import java.math.BigDecimal

class ProductDetail : Serializable {
    val bannerImgUrls: List<String> = ArrayList()
    val detailImgUrls: List<String> = ArrayList()
    val publicImgUrls: List<String> = ArrayList()
    val id: String = ""
    val introduce: String = ""
    val logoImgUrl: String = ""

    /**
     * 产品类型
     * P1 个体户营业执照办理
     * P2 企业套餐
     * P3 个体户代理记账
     * P4 个体户税务登记
     * P5 个体户营业执照变更
     * P6 个体户营业执照注销
     * P7 大师起名
     */
    val mold: String = ""
    val money: BigDecimal = BigDecimal.ZERO
    val name: String = ""
    val prices: ArrayList<Price> = ArrayList()
    val state: String = ""
}