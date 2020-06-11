package com.fatcloud.account.entity.commons

data class BusinessScope(
    val childs: List<BusinessScope>,
    val createDt: String,
    val delFlag: Int,
    val id: String,
    val mold: String,
    val name: String,
    val pid: String,
    val updateDt: String,
    val version: Int,

    var nativeIsSelect: Boolean
)