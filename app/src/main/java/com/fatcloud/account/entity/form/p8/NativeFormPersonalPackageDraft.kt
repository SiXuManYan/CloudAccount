package com.fatcloud.account.entity.form.p8

import com.fatcloud.account.entity.order.IdentityImg
import java.math.BigDecimal

/**
 * 个体户套餐 P9 草稿
 */
class NativeFormPersonalPackageDraft {

    /** 详细地址 */
    val addr: String = ""

    /** 用户选中的城市名称 */
    val area: String = ""

    /** 银行卡号 */
    val bankNo: String = ""

    /** 银行预留手机号 */
    val bankPhone: String = ""

    /** 经营范围ID */
    val businessScope: List<String> = ArrayList()

    /** 资金数额，注册资本 */
    val capital: BigDecimal = BigDecimal.ZERO

    /** 从业人数 */
    val employedNum: String = "0"

    /** 组成形式的 Id */
    val form: Int = 0

    /** 性别 index 1男 2女*/
    val gender: Int = 0


    /** 身份证号 */
    val idno: String = ""

    /** 身份证正反面 */
    val imgs: List<IdentityImg> = ArrayList()

    /** 客户端计算出的金额 */
    val money: String = "0"

    /** 首选名称 */
    val name0: String = ""

    /** 备选名称1 */
    val name1: String = ""

    /** 备选名称2 */
    val name2: String = ""

    /** 民族 */
    val nation: String = ""

    /** 产品id */
    val productId: String = ""

    /** 产品价格id */
    val productPriceId: String = ""

    /** 法人姓名 */
    val realName: String = ""

    /** 法人联系方式 */
    val tel: String = ""
}