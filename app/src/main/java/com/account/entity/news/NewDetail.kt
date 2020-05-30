package com.account.entity.news

data class NewDetail(
    val content: String = "",
    val createDt: String = "",
    val delFlag: Int = 0,
    val homeFlag: String = "",
    val homeFlagText: String = "",
    val id: String = "",
    val likeCount: Int = 0,
    val mold: String = "",
    val moldText: String = "",
    val readCount: Int = 0,
    val recommandMold: String = "",
    val recommandMoldText: String = "",
    val showFlag: String = "",
    val showFlagText: String = "",
    val title: String = "",
    val updateDt: String = "",
    val version: Int = 0
)