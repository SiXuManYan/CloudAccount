package com.fatcloud.account.entity.local.form

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Wangsw on 2020/7/3 0003 15:31.
 * </br>
 * 银行对公账户表单
 *
 */
@Entity(tableName = "tb_bank_public_draft")
class BankPublicDraft {


    @PrimaryKey
    @ColumnInfo(name = "id")
    var primaryId: Int = 0


    @ColumnInfo(name = "login_phone")
    var loginPhone: String? = ""


    /**
     * 订单流程id
     */
    @ColumnInfo(name = "order_work_id")
    var orderWorkId: String? = ""


    /**
     * 公司名称
     */
    @ColumnInfo(name = "company_name")
    var companyName: String? = ""

    /**
     * 公司地址
     */
    @ColumnInfo(name = "company_address")
    var companyAddress: String? = ""


    /**
     * 注册资金
     */
    @ColumnInfo(name = "registered_capital")
    var registeredCapital: String? = ""


    /**
     * 账户性质
     */
    @ColumnInfo(name = "registered_capital")
    var accountNatureValue: String? = ""


    /**
     * 对账联系人
     */
    @ColumnInfo(name = "reconciliation_name")
    var reconciliationName: String? = ""


    /**
     * 对账联系电话
     */
    @ColumnInfo(name = "reconciliation_phone")
    var reconciliationPhone: String? = ""


    /**
     * 详细地址
     */
    @ColumnInfo(name = "detail_address")
    var detailAddress: String? = ""


    /**
     * 用户选中的区域地址
     */
    @ColumnInfo(name = "area")
    var area: String? = ""

    /**
     * 营业执照url
     */
    @ColumnInfo(name = "business_license_url")
    var businessLicenseUrl: String? = ""

    /**
     * 电子图章url
     */
    @ColumnInfo(name = "electronic_seal_url")
    var electronicSealUrl: String? = ""


    /**
     * 身份证号
     */
    @ColumnInfo(name = "id_number")
    var idNumber: String? = ""

    /**
     * 身份证号 正面
     */
    @ColumnInfo(name = "id_number_image_front_url")
    var idNumberImageFrontUrl: String? = ""

    /**
     * 身份证号 反面
     */
    @ColumnInfo(name = "id_number_image_back_url")
    var idNumberImageBackUrl: String? = ""


    /**
     * 财务负责人姓名
     */
    @ColumnInfo(name = "finance_name")
    var financeName: String? = ""


    /**
     * 财务负责人手机号
     */
    @ColumnInfo(name = "finance_phone")
    var financePhone: String? = ""

    /**
     * 财务负责人股份占比
     */
    @ColumnInfo(name = "finance_shares")
    var financeShares: String? = ""

    /**
     * 法人签字授权书url
     */
    @ColumnInfo(name = "legalPersonWarrantImgUrl")
    var legalPersonWarrantImgUrl: String? = ""


}