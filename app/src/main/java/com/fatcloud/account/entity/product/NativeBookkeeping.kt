package com.fatcloud.account.entity.product

import java.io.Serializable

/**
 * Created by Wangsw on 2020/6/13 0013 17:07.
 * </br>
 * 代理记账Model
 */
class NativeBookkeeping : Serializable {

    /**
     * 最终需支付金额
     */
    var finalMoney: String = ""

    /**
     * 产品id
     */
    var productId: String = "0"

    /**
     * 选中的产品价格id
     */
    var productPriceId: String = "0"


    /**
     * 营业执照图片地址
     */
    var businessLicenseImgUrl: String = ""

    /**
     * 法人姓名
     */
    var legalPersonName: String = ""

    /**
     * 法人联系电话
     */
    var phone: String = ""


    /**
     * 银行卡号
     */
    var idNumber: String = ""

    /**
     * 店铺名称
     */
    var storeName: String = ""

    /**
     * 法人签字图片地址
     */
    var signImageUrl: String = ""


}