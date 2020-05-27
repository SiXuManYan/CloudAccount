package com.account.entity.home

data class News(
    val createDt: String = "",
    val id: String = "",
    val imgUrls: ArrayList<String> = ArrayList(),
    val likeCount: Int = 0,
    val readCount: Int = 0,
    val title: String = ""
)