package com.fatcloud.account.entity.form

/**
 * Created by Wangsw on 2020/8/5 0005 15:20.
 * </br>
 *
 */
class MarketData {

/*    "data": {
        "url": "http://wsdj.lngs.gov.cn",
        "govcnImgUrls": [
        "https://ftacloud-bucket-public.oss-cn-qingdao.aliyuncs.com/product/web-20207231655926118256-p2-app-07-gov.cn-01.jpg",
        "https://ftacloud-bucket-public.oss-cn-qingdao.aliyuncs.com/product/web-20207231661274893338-p2-app-07-gov.cn-02.jpg",
        "https://ftacloud-bucket-public.oss-cn-qingdao.aliyuncs.com/product/web-20207231663887388585-p2-app-07-gov.cn-03.jpg",
        "https://ftacloud-bucket-public.oss-cn-qingdao.aliyuncs.com/product/web-20207231665613634833-p2-app-07-gov.cn-04.jpg"
        ]
    }*/

    /** 注册地址 */
    val url: String = ""

    /** 注册流程 图片  */
    val govcnImgUrls: ArrayList<String> = ArrayList()


}