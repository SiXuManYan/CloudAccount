package com.fatcloud.account.entity.local.form

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blankj.utilcode.util.Utils
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.entity.order.enterprise.Shareholder

/**
 * Created by Wangsw on 2020/7/3 0003 15:31.
 * </br>
 * 银行对公账户表单
 *
 */
@Entity(tableName = "tb_bank_public_draft")
class BankPublicDraft {


    companion object {
        private var instance: BankPublicDraft? = null
            get() {
                if (field == null) {
                    field =
                        (Utils.getApp() as CloudAccountApplication).database.bankPublicDraftDao()
                            .find()
                    if (field == null) {
                        field = BankPublicDraft()
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
            (Utils.getApp() as CloudAccountApplication).database.bankPublicDraftDao().clear()
            instance = null
        }
    }


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
    @ColumnInfo(name = "account_nature")
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
     * 用户选中的区域地址 id
     */
    @ColumnInfo(name = "area_id")
    var areaId: String? = ""


    /**
     * 财务负责人
     */
    @ColumnInfo(name = "share_holders")
    var shareholders: List<Shareholder>? = ArrayList()


    /**
     * 营业执照url
     */
    @ColumnInfo(name = "business_license_url")
    var businessLicenseUrl: String? = ""

    /**
     * 营业执照 path
     */
    @ColumnInfo(name = "business_license_path")
    var businessLicensePath: String? = ""


    /**
     * 电子图章url
     */
    @ColumnInfo(name = "electronic_seal_url")
    var electronicSealUrl: String? = ""

    /**
     * 电子图章path
     */
    @ColumnInfo(name = "electronic_seal_path")
    var electronicSealPath: String? = ""

    /**
     * 法人签字授权书url
     */
    @ColumnInfo(name = "legal_person_warrant_image_url")
    var legalPersonWarrantImgUrl: String? = ""

    /**
     * 法人签字授权书 path
     */
    @ColumnInfo(name = "legal_person_warrant_image_path")
    var legalPersonWarrantImgPath: String? = ""


    /**
     * 邮编
     */
    @ColumnInfo(name = "postcode")
    var postcode: String? = ""

    /**
     * 邮寄地址
     */
    @ColumnInfo(name = "mailing_address")
    var mailingAddress: String? = ""

    /**
     * 邮寄详细地址
     */
    @ColumnInfo(name = "mailing_detail_address")
    var mailingDetailAddress: String? = ""




    @ColumnInfo(name = "create_time")
    var createTime: String = ""

}