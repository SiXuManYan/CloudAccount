package com.fatcloud.account.entity.order

import com.fatcloud.account.common.Constants

data class IdentityImg (

    val imgUrl: String,

    /**
     * 身份证正反面
     * @see Constants.I1
     * @see Constants.I2
     */
    val mold: String



)