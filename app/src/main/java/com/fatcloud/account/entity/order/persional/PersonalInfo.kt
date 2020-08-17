package com.fatcloud.account.entity.order.persional

import com.fatcloud.account.entity.commons.BusinessScope
import com.fatcloud.account.entity.commons.Form
import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.enterprise.Shareholder
import java.math.BigDecimal

/**
 * 个人业务订单详情
 *
 */
class PersonalInfo {

    var accountId: String? = null
    var addr: String? = null
    var area: String? = null


    /**
     * 营业执照地址
     */
    var businessLicenseImgUrl: String? = null
    var createDt: String? = null
    var delFlag: Int? = null

    /**
     * 个人业务id
     */
    var id: String? = null

    /**
     * 身份证号
     */
    var idno: String? = null
    var legalPersonName: String? = null

    /**
     * @see  Order.mold
     */
    var mold: String? = null
    var moldText: String? = null

    /** 客户端计算出的金额 */
    var money: BigDecimal? = null
    var nickName: String? = null
    var no: String? = null

    /**
     * PS1 未支付
     * PS2 微信支付中
     * PS3 支付宝支付中
     * PS4 微信已支付
     * PS5 支付宝已支付
     */
    var payState: String? = null
    var payStateText: String? = null
    var phoneOfBank: String? = null
    var productId: String? = null
    var productName: String? = null
    var productPriceId: String? = null
    var productPriceName: String? = null


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
    var state: String? = null
    var stateText: String? = null
    var taxpayerNo: String? = null
    var updateDt: String? = null
    var username: String? = null
    var version: Int? = null
    var weixinPayMap: String? = null


    /**
     * 经营范围 id
     * @see BusinessScope.id
     */

    var businessScope: List<String>? = null


    var businessScopeNames: String? = null


    /**
     * 资金数额，注册资本
     */
    var capital: BigDecimal? = null

    /**
     * 从业人数
     */
    var employedNum: String? = null

    /**
     * 组成形式的 Id
     * @see Form.id
     */
    var form: Int? = 0
    var formName: String? = null

    /**
     * 1 男
     * 2 女
     */
    var gender: String? = null
    var imgs: List<IdentityImg>? = null


    /**
     * 收入
     */
    var income: BigDecimal? = null
    var name0: String? = null
    var name1: String? = null
    var name2: String? = null

    /**
     * 民族
     */
    var nation: String? = null

    var realName: String? = null
    var tel: String? = null


    // 公司信息


    var enterpriseName0: String? = null
    var enterpriseName1: String? = null
    var enterpriseName2: String? = null

    /**
     * 注册资本
     */
    var investMoney: BigDecimal? = null

    var investYearNum: Int? = null

    var shareholders: ArrayList<Shareholder>? = null


    var bankNo: String? = null
    var bankPhone: String? = null

}

