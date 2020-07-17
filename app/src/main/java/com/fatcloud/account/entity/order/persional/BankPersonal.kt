package com.fatcloud.account.entity.order.persional

import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.IdentityImg

/**
 * Created by Wangsw on 2020/7/13 0013 8:53.
 * </br>
 * 个体户营业执照变更
 */
class BankPersonal {

    /** 产品id */
    var productId: String? = null

    /** 选中的产品价格id */
    var productPriceId: String? = null

    /** 客户端计算出的最终金额 */
    var money: String? = null

    /** 银行信息 */
    var bank: String? = "渤海银行"

    /** 存款人姓名 */
    var depositorName: String? = null

    /** 社会统一信用代码 */
    var enterpriseCode: String? = null

    /** 注册地址 */
    var addressRegistered: String? = null

    /** 货币 */
    var currency: String? = "人民币"

    /**
     * 账户类型
     * @see Constants.AN1
     * @see Constants.AN2
     * @see Constants.AN3
     */
    var accountType: String? = null

    /** 邮寄地址 */
    var addressPost: String? = null

    /**
     * 邮寄详细地址
     */
    var addressDetailed: String? = null


    /**
     * 法人身份证正反面
     */
    var imgsIdno: List<IdentityImg>? = null

    /**
     * 营业执照正本
     */
    var imgsLicense: List<IdentityImg>? = null

    /**
     * 基本存款账户图片
     */
    var imgsDepositAccount: List<IdentityImg>? = null


    /**
     * 法人姓名和联系方式
     */
    var personLegal: NamePhoneBean? = null

    /**
     * 财务负责人的姓名和联系方式
     */
    var personFinance: NamePhoneBean? = null


    /**
     * 大额业务查证联系人1信息
     */
    var personVerification1: NamePhoneBean? = null

    /**
     * 大额业务查证联系人2信息
     */
    var personVerification2: NamePhoneBean? = null


    /**
     * 对账联系人
     */
    var personReconciliation: NamePhoneBean? = null


}