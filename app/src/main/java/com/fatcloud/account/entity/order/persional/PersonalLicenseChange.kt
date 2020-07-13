package com.fatcloud.account.entity.order.persional

import com.fatcloud.account.entity.commons.BusinessScope
import com.fatcloud.account.entity.order.IdentityImg

/**
 * Created by Wangsw on 2020/7/13 0013 8:53.
 * </br>
 * 个体户营业执照变更
 */
class PersonalLicenseChange {

    /**
     * 产品id
     */
    var productId: String? = null

    /**
     * 选中的产品价格id
     */
    var productPriceId: String? = null


    /**
     * 经营范围 id
     * @see BusinessScope.id
     */
    var businessScope: ArrayList<String>? = null


    /**
     * 首选名称
     */
    var enterpriseName0: String? = null

    /**
     * 备选名称1
     */
    var enterpriseName1: String? = null

    /**
     * 备选名称2
     */
    var enterpriseName2: String? = null

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


}