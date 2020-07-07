package com.fatcloud.account.entity.local.form

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blankj.utilcode.util.Utils
import com.fatcloud.account.app.CloudAccountApplication

/**
 * Created by Wangsw on 2020/7/2 0002 14:17.
 * </br>
 * 个体户税务登记草稿
 */
@Entity(tableName = "tb_personal_tax_draft")
class PersonalTaxDraft {


    companion object {
        private var instance: PersonalTaxDraft? = null
            get() {
                if (field == null) {
                    field = (Utils.getApp() as CloudAccountApplication).database.personalTaxDraftDao().find()
                    if (field == null) {
                        field = PersonalTaxDraft()
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
            (Utils.getApp() as CloudAccountApplication).database.personalTaxDraftDao().clear()
            instance = null
        }
    }




    @PrimaryKey
    @ColumnInfo(name = "id")
    var primaryId: Int = 0


    @ColumnInfo(name = "login_phone")
    var loginPhone: String? = ""


    /**
     * 计算出的产品价格
     */
    @ColumnInfo(name = "final_money")
    var finalMoney: String? = ""

    /**
     * 产品id
     */
    @ColumnInfo(name = "product_id")
    var productId: String? = ""


    /**
     * 选中的产品价格id
     */
    @ColumnInfo(name = "product_price_id")
    var productPriceId: String? = ""

    /**
     * 纳税人识别号
     */
    @ColumnInfo(name = "taxpayer_number")
    var taxpayerNo: String? = ""

    /**
     * 法人姓名
     */
    @ColumnInfo(name = "legal_person_name")
    var legalPersonName: String? = ""

    /**
     * 身份证号
     */
    @ColumnInfo(name = "id_number")
    var idNumber: String? = ""



    /**
     * 银行卡号
     */
    @ColumnInfo(name = "bank_number")
    var bankNumber: String? = ""

    /**
     * 银行卡预留手机号
     */
    @ColumnInfo(name = "bank_phone")
    var bankPhone: String? = ""



    /**
     * 详细地址
     */
    @ColumnInfo(name =  "detail_address")
    var detailAddress: String? = ""

    /**
     * 用户选中的区域地址
     */
    @ColumnInfo(name =  "area")
    var area: String? = ""


    /**
     * 营业执照图片地址
     */
    @ColumnInfo(name =  "business_license_image_url")
    var businessLicenseImgUrl: String? = ""

    /**
     * 营业执照图片路径
     */
    @ColumnInfo(name =  "business_license_image_file_path")
    var businessLicenseImgFilePath: String? = ""




}