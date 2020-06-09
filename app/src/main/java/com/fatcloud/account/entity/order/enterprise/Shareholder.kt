package com.fatcloud.account.entity.order.enterprise

import com.fatcloud.account.entity.order.IdentityImg


/**
 *  股权人相关信息
 */
data class Shareholder(
    val idno: String,
    val idnoAddr: String,
    val imgs: List<IdentityImg>,

    /**
     * SH1 	企业法人
     * SH2 	监事
     * SH3  股东
     */
    val mold: String,
    val name: String,
    val phone: String,
    val shareProportion: Int
)