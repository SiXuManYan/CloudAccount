package com.fatcloud.account.entity.local.form

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fatcloud.account.entity.commons.BusinessScope
import com.fatcloud.account.entity.order.IdentityImg

/**
 * Created by Wangsw on 2020/7/13 0013 8:53.
 * </br>
 * 个体户营业执照变更
 */
@Entity(tableName = "tb_personal_license_change_draft")
class PersonalLicenseChangeDraft {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var primaryId: Int = 0

    @ColumnInfo(name = "product_id")
    var productId: String? = ""

    /**
     * 选中的产品价格id
     */
    @ColumnInfo(name = "product_price_id")
    var productPriceId: String? = ""


    @ColumnInfo(name = "login_phone")
    var loginPhone: String? = ""


    /**
     * 经营范围 id
     * @see BusinessScope.id
     */
    @ColumnInfo(name = "business_scope_id")
    var businessScopeId: ArrayList<String>? = ArrayList()


    /**
     * 经营范围 name
     * @see BusinessScope.name
     */
    @ColumnInfo(name = "business_scope_name")
    var businessScopeName: ArrayList<String>? = ArrayList()

    /**
     * 首选名称
     */
    @ColumnInfo(name = "zero_name")
    var zeroName: String? = ""

    /**
     * 备选名称1
     */
    @ColumnInfo(name = "first_name")
    var firstName: String? = ""

    /**
     * 备选名称2
     */
    @ColumnInfo(name = "second_name")
    var secondName: String? = ""

    /**
     * 身份证号
     */
    @ColumnInfo(name = "id_number")
    var idNumber: String? = ""


    /**
     * 身份证正反面
     */
    @ColumnInfo(name = "id_images")
    var idImages: List<IdentityImg>? = ArrayList()

    /**
     * 营业执照正反面
     */
    @ColumnInfo(name = "license_images")
    var licenseImages: List<IdentityImg>? = ArrayList()


    /**
     * 法人姓名
     */
    @ColumnInfo(name = "legal_person_name")
    var legalPersonName: String? = ""


    /**
     * 客户端计算出的最终金额
     */
    @ColumnInfo(name = "final_money")
    var finalMoney: String? = ""


    @ColumnInfo(name = "phone")
    var phone: String? = ""


}