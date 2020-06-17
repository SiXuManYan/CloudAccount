package com.fatcloud.account.entity.news

data class News(
    val createDt: String = "",
    val id: String = "",
    val imgUrls: ArrayList<String> = ArrayList(),
    val likeCount: Int = 0,
    var readCount: Int = 0,
    val title: String = "",

    // 资讯列表字段 ↓
    val mold: String = "",
    val moldText: String = "",

    /**
     * 1 置顶
     */
    val recommandFlag: Int = 0,
    val recommandFlagText: String = ""


)
