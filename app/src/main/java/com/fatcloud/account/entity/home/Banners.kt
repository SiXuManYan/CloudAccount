package com.fatcloud.account.entity.home

data class Banners(
    val id: String = "",
    val imgUrl: String = "",

    /**
     * 首页Banner类型
     * B1 网页H5
     * B2 个体户营业执照
     * B3 资讯
     * B4 企业套餐
     */
    val mold: String = "",
    val name: String = "",
    val link: String = ""
)


/*

"banners" : [ {
    "id" : "1265843378646417408",
    "name" : "返税轮播图",
    "imgUrl" : "https://ftacloud-bucket-public.oss-cn-qingdao.aliyuncs.com/banner/web-20206316543728596385-banner2.jpg",
    "link" : "https://www.baidu.com",
    "mold" : "B1"
}
*/
