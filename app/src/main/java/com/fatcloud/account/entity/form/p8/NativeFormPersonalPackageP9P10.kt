package com.fatcloud.account.entity.form.p8

import com.fatcloud.account.entity.order.IdentityImg
import java.math.BigDecimal

/**
 * 个体户套餐 P9
 */
data class NativeFormPersonalPackageP9P10(

    /** 详细地址 */
    var addr: String? = null,

    /** 用户选中的城市名称 */
    var area: String? = null,

    /** 银行卡号 */
    var bankNo: String? = null,

    /** 银行预留手机号 */
    var bankPhone: String? = null,

    /** 经营范围ID */
    var businessScope: List<String>? = null,

    /** 资金数额，注册资本 */
    var capital: BigDecimal? = null,

    /** 从业人数 */
    var employedNum: String? = null,

    /** 组成形式的 Id */
    var form: Int? = null,

    /** 性别 */
    var gender: String? = null,

    /** 身份证号 */
    var idno: String? = null,

    /** 身份证正反面 */
    var imgs: List<IdentityImg>? = null,

    /** 客户端计算出的金额 */
    var money: String? = null,

    /** 首选名称 */
    var name0: String? = null,

    /** 备选名称1 */
    var name1: String? = null,

    /** 备选名称2 */
    var name2: String? = null,

    /** 民族 */
    var nation: String? = null,

    /** 产品id */
    var productId: String? = null,

    /** 产品价格id */
    var productPriceId: String? = null,

    /** 法人姓名 */
    var realName: String? = null,

    /** 法人联系方式 */
    var tel: String? = null
)