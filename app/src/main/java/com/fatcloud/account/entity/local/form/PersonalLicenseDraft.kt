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
                        (Utils.getApp() as CloudAccountApplication).database.formPersonalInfoDraftDao()
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
            (Utils.getApp() as CloudAccountApplication).database.formPersonalInfoDraftDao().clear()
            instance = null
        }
    }


    @PrimaryKey
    @ColumnInfo(name = "id")
    var primaryId: Int = 0


    /**
     * @see Order.mold
     */
    @ColumnInfo(name = "mold")
    var mold: String? = ""


    @ColumnInfo(name = "login_user_phone")
    var loginUserPhone: String? = ""



    @ColumnInfo(name = "address")
    var addr: String? = ""

    @ColumnInfo(name = "area")
    var area: String? = ""


    /**
     * 身份证号
     */
    @ColumnInfo(name = "id_number")
    var idno: String? = ""


    @ColumnInfo(name = "money")
    var money: BigDecimal? = BigDecimal.ZERO


    @ColumnInfo(name = "product_id")
    var productId: String? = ""


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


    @ColumnInfo(name = "imgs")
    var imgs: List<IdentityImg>? = ArrayList()


    /**
     * 收入
     */
    @ColumnInfo(name = "income")
    var income: BigDecimal? = BigDecimal.ZERO

    @ColumnInfo(name = "name0")
    var name0: String? = ""

    @ColumnInfo(name = "name1")
    var name1: String? = ""

    @ColumnInfo(name = "name2")
    var name2: String? = ""

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


