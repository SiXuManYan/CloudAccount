package com.fatcloud.account.entity.order.detail

import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.persional.Order

/**
 * 个体户营业执照办理表单
 */
class PersonalLicenseHandleDetail {

    var accountId: String = ""
    var addr: String = ""
    var area: String = ""
    var bankNo: String = ""

    /**
     * 营业执照地址
     */
    var businessLicenseImgUrl: String = ""
    var createDt: String = ""


    /**
     * 个人业务id
     */
    var id: String = ""

    /**
     * 身份证号
     */
    var idno: String = ""
    var legalPersonName: String = ""

    /**
     * @see  Order.mold
     */
    var mold: String = ""
    var moldText: String = ""


    var no: String = ""

    /**
     * PS1 未支付
     * PS2 微信支付中
     * PS3 支付宝支付中
     * PS4 微信已支付
     * PS5 支付宝已支付
     */
    var payState: String = ""
    var payStateText: String = ""
    var phoneOfBank: String = ""
    var productId: String = ""
    var productName: String = ""
    var productPriceId: String = ""
    var productPriceName: String = ""


    /**
     * OS1 待支付
     * OS2 取消订单
     * OS3 订单失效
     * OS4 支付中
     * OS5 已支付
     * OS6 已受理
     * OS7 办理中
     * OS8 已办结
     */
    var state: String = ""
    var stateText: String = ""
    var taxpayerNo: String = ""
    var updateDt: String = ""
    var username: String = ""


    var businessScopeNames: String = ""


    /**
     * 从业人数
     */
    var employedNum: String = ""


    var formName: String = ""

    /**
     * 1 男
     * 2 女
     */
    var gender: String = ""

    var imgs: List<IdentityImg> = ArrayList()


    /**
     * 收入
     */
    var income: String = ""
    var name0: String = ""
    var name1: String = ""
    var name2: String = ""

    /**
     * 民族
     */
    var nation: String = ""

    var realName: String = ""

    var tel: String = ""


}