package com.fatcloud.account.entity.order.persional

import com.fatcloud.account.entity.order.IdentityImg

/**
 * Created by Wangsw on 2020/7/14 0013 13:30.
 * </br>
 * 个体户营业执照注销
 */
class PersonalLicenseLogout {

    /**
     * 产品id
     */
    var productId: String? = null

    /**
     * 选中的产品价格id
     */
    var productPriceId: String? = null



    /**
     * 身份证号
     */
    var idno: String? = null


    /**
     * 身份证正反面
     */
    var imgsIdno: List<IdentityImg>? = null

    /**
     * 营业执照正反面
     */
    var imgsLicense: List<IdentityImg>? = null

    /**
     * 承诺书
     */
    var imgsCommitment: List<IdentityImg>? = null


    /**
     * 法人姓名
     */
    var legalPersonName: String? = null


    /**
     * 客户端计算出的最终金额
     */
    var money: String? = null

    /**
     * 联系电话
     */
    var phone: String? = null

    /**
     * 社会统一代码
     */
    var enterpriseCode: String? = null

    /**
     * 机构全称
     */
    var enterpriseName: String? = null

    /**
     * 注销原因
     */
    var reason: String? = null


}