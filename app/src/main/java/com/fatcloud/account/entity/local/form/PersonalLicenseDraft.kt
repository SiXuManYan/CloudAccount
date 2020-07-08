package com.fatcloud.account.entity.local.form

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blankj.utilcode.util.Utils
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.entity.commons.BusinessScope
import com.fatcloud.account.entity.commons.Form
import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.persional.Order
import com.fatcloud.account.entity.order.persional.PersonalInfo
import java.math.BigDecimal

/**
 * 个体户营业执照 草稿箱
 * @see PersonalInfo
 */
@Entity(tableName = "tb_personal_license_draft")
class PersonalLicenseDraft {

    companion object {
        private var instance: PersonalLicenseDraft? = null
            get() {
                if (field == null) {
                    field =
                        (Utils.getApp() as CloudAccountApplication).database.personalLicenseDraftDao()
                            .find()
                    if (field == null) {
                        field = PersonalLicenseDraft()
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
            (Utils.getApp() as CloudAccountApplication).database.personalLicenseDraftDao().clear()
            instance = null
        }
    }


    @PrimaryKey
    @ColumnInfo(name = "id")
    var primaryId: Int = 0


    @ColumnInfo(name = "login_phone")
    var loginPhone: String? = ""


    /**
     * @see Order.mold
     */
    @ColumnInfo(name = "mold")
    var mold: String? = ""



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
     * 身份证号
     */
    @ColumnInfo(name = "id_number")
    var idno: String? = ""


    /**
     * 客户端计算出的最终金额
     */
    @ColumnInfo(name = "final_money")
    var finalMoney: String? = ""


    @ColumnInfo(name = "product_id")
    var productId: String? = ""

    /**
     * 选中的产品价格id
     */
    @ColumnInfo(name = "product_price_id")
    var productPriceId: String? = ""


    /**
     * 经营范围 id
     * @see BusinessScope.id
     */
    @ColumnInfo(name = "business_scope_id")
    var businessScope: ArrayList<String>? = ArrayList()


    /**
     * 资金数额，注册资本
     */
    @ColumnInfo(name = "capital")
    var capital: BigDecimal? = BigDecimal.ZERO

    /**
     * 从业人数
     */
    @ColumnInfo(name = "employed_num")
    var employedNum: String? = ""

    /**
     * 组成形式的 Id
     * @see Form.id
     */
    @ColumnInfo(name = "form")
    var form: Int? = 0


    /**
     * 1 男
     * 2 女
     */
    @ColumnInfo(name = "gender")
    var gender: String? = "1"


    @ColumnInfo(name = "identity_img")
    var identityImg: List<IdentityImg>? = ArrayList()


    /**
     * 收入
     */
    @ColumnInfo(name = "income")
    var income: BigDecimal? = BigDecimal.ZERO

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
     * 民族
     */
    @ColumnInfo(name = "nation")
    var nation: String? = ""

    @ColumnInfo(name = "real_name")
    var realName: String? = ""

    @ColumnInfo(name = "tel")
    var tel: String? = ""



}


