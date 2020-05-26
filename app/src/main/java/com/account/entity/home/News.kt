package com.account.entity.home

data class News(
    val createDt: String,
    val id: String,
    val imgUrls: List<String>,
    val likeCount: Int,
    val readCount: Int ,
    val title: String
)