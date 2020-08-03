package com.fatcloud.account.entity.local.form

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blankj.utilcode.util.Utils
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.persional.NamePhoneBean

/**
 * Created by Wangsw on 2020/7/13 0013 8:53.
 * </br>
 * 个体户银行对公账户
 */
@Entity(tableName = "tb_bank_personal_draft")
class BankPersonalDraft {

    companion object {
        private var instance: BankPersonalDraft? = null
            get() {
                if (field == null) {
                    field =
                        (Utils.getApp() as CloudAccountApplication).database.bankPersonalDraftDao()
                            .find()
                    if (field == null) {
                        field = BankPersonalDraft()
                    }
                }
                return field
            }

        @Synchronized
        fun get() = instance!!

        fun update() {
            instance = null
        }

        fun clearAll() {
            (Utils.getApp() as CloudAccountApplication).database.bankPersonalDraftDao().clear()
            instance = null
        }
    }


    @PrimaryKey
    @ColumnInfo(name = "id")
    var primaryId: Int = 0


    @ColumnInfo(name = "login_phone")
    var loginPhone: String? = ""





    /** 产品id */
    @ColumnInfo(name = "product_id")
    var productId: String? = ""

    /** 选中的产品价格id */
    @ColumnInfo(name = "product_price_id")
    var productPriceId: String? = ""

    /**
     * 客户端计算出的最终金额
     */
    @ColumnInfo(name = "final_money")
    var finalMoney: String? = ""

    /** 银行信息 */
    @ColumnInfo(name = "bank_name")
    var bank: String? = ""

    /** 存款人姓名 */
    @ColumnInfo(name = "depositor_name")
    var depositorName: String? = ""

    /** 社会统一信用代码 */
    @ColumnInfo(name = "enterprise_code")
    var enterpriseCode: String? = ""

    /** 注册地址 */
    @ColumnInfo(name = "address_registered")
    var addressRegistered: String? = ""

    /** 货币 */
    @ColumnInfo(name = "currency")
    var currency: String? = ""

    /**
     * 账户类型
     * @see Constants.AN1
     * @see Constants.AN2
     * @see Constants.AN3
     */
    @ColumnInfo(name = "account_type")
    var accountType: String? = ""

    /** 邮寄地址 */
    @ColumnInfo(name = "address_post")
    var addressPost: String? = ""

    /**
     * 邮寄详细地址
     */
    @ColumnInfo(name = "address_detailed")
    var addressDetailed: String? = ""


    /**
     * 法人身份证正反面
     */
    @ColumnInfo(name = "identity_img")
    var imgsIdno: List<IdentityImg>? = ArrayList()

    /**
     * 营业执照正本
     */
    @ColumnInfo(name = "license_img")
    var imgsLicense: List<IdentityImg>? = ArrayList()

    /**
     * 存款账户图片
     */
    @ColumnInfo(name = "deposit_img")
    var imgsDepositAccount: List<IdentityImg>? = ArrayList()


    /**
     * 法人姓名和联系方式
     */
    @ColumnInfo(name = "person_legal")
    var personLegal: NamePhoneBean? = NamePhoneBean()

    /**
     * 财务负责人的姓名和联系方式
     */
    @ColumnInfo(name = "person_finance")
    var personFinance: NamePhoneBean? = NamePhoneBean()


    /**
     * 大额业务查证联系人1信息
     */
    @ColumnInfo(name = "person_verification_first")
    var personVerification1: NamePhoneBean? = NamePhoneBean()

    /**
     * 大额业务查证联系人2信息
     */
    @ColumnInfo(name = "person_verification_second")
    var personVerification2: NamePhoneBean? = NamePhoneBean()


    /**
     * 对账联系人
     */
    @ColumnInfo(name = "person_reconciliation")
    var personReconciliation: NamePhoneBean? = NamePhoneBean()

    /**
     * 订单流程id
     * P9(个体户套餐中的银行表单) ,P10(个人独资银行表单)不需要支付，所以额外需要此字段
     *
     */
    @ColumnInfo(name = "order_work_id")
    var orderWorkId: String? = ""

     /**
      * 区分来源，判断表单内是否需要支付
     * P9(个体户套餐中的银行表单) , P10(个人独资银行表单)不需要支付，所以额外需要此字段
     * @see Constants.P8
     * @see Constants.P9
     * @see Constants.P10
     */
    @ColumnInfo(name = "mold")
    var mold: String? = ""




}