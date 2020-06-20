package com.fatcloud.account.entity.product

import com.fatcloud.account.common.Constants
import java.io.Serializable
import java.math.BigDecimal

class ProductDetail : Serializable {
    val bannerImgUrls: List<String> = ArrayList()
    val detailImgUrls: List<String> = ArrayList()
    val publicImgUrls: List<String> = ArrayList()
    val serviceImgUrls: List<String> = ArrayList()
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
     * @see Constants.P1
     * @see Constants.P2
     * @see Constants.P3
     * @see Constants.P4
     * @see Constants.P5
     * @see Constants.P6
     * @see Constants.P7
     */
    val mold: String = ""
    val money: BigDecimal = BigDecimal.ZERO
    val name: String = ""
    val prices: ArrayList<Price> = ArrayList()
    val state: String = ""


/*    "id": "1262568059554496512",
    "name": "企业套餐办理",
    "introduce": "企业营业执照办理+企业刻章+开立银行对公账户+代理记账",
    "state": "S1",
    "money": 3176,
    "mold": "P2",
    "logoImgUrl": "https://ftacloud-bucket-public.oss-cn-qingdao.aliyuncs.com/product/web-20206313291886329903-logo.jpg",
    "bannerImgUrls": [
    "https://ftacloud-bucket-public.oss-cn-qingdao.aliyuncs.com/product/web-202063132943890573-banner.jpg"
    ],
    "detailImgUrls": [
    "https://ftacloud-bucket-public.oss-cn-qingdao.aliyuncs.com/product/web-20206179273952894652-qiyejiage.jpg",
    "https://ftacloud-bucket-public.oss-cn-qingdao.aliyuncs.com/product/web-202063950392211774-2.jpg",
    "https://ftacloud-bucket-public.oss-cn-qingdao.aliyuncs.com/product/web-2020639501357593311-3.jpg"
    ],
    "serviceImgUrls": [
    "https://ftacloud-bucket-public.oss-cn-qingdao.aliyuncs.com/product/web-2020631161340765416-4.jpg",
    "https://ftacloud-bucket-public.oss-cn-qingdao.aliyuncs.com/product/web-2020639504760218392-5.jpg"
    ],
    "publicImgUrls": [
    "https://ftacloud-bucket-public.oss-cn-qingdao.aliyuncs.com/product/web-202061613392240213673-police20200616133806.jpg"
    ],
    "prices": [*/

}