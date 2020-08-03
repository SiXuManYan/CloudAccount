package com.fatcloud.account.entity.form.p9p10

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blankj.utilcode.util.Utils
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.entity.commons.BusinessScope
import com.fatcloud.account.entity.order.IdentityImg

/**
 * 个体户套餐 P9  、 个人独资企业套餐P10  草稿
 */
@Entity(tableName = "tb_p9_p10_personal_package_draft")
class NativeFormPersonalPackageP9P10Draft {


    companion object {
        private var instance: NativeFormPersonalPackageP9P10Draft? = null
            get() {
                if (field == null) {
                    field = (Utils.getApp() as CloudAccountApplication).database.p9p10PersonalPackageDao().find()
                    if (field == null) {
                        field = NativeFormPersonalPackageP9P10Draft()
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
            (Utils.getApp() as CloudAccountApplication).database.p9p10PersonalPackageDao().clear()
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

    /** 客户端计算出的最终金额 */
    @ColumnInfo(name = "final_money")
    var finalMoney: String? = ""


    /** 详细地址 */
    @ColumnInfo(name = "address")
    var addr: String = ""


    /** 用户选中的城市名称 */
    @ColumnInfo(name = "area")
    var area: String = ""


    /** 银行卡号 */
    @ColumnInfo(name = "bank_number")
    var bankNo: String = ""

    /** 银行预留手机号 */
    @ColumnInfo(name = "bank_phone")
    var bankPhone: String = ""

    /**
     * 经营范围 id
     * @see BusinessScope.id
     */
    @ColumnInfo(name = "business_scope_id")
    var businessScopeId: ArrayList<String>? = ArrayList()

    /** 资金数额，注册资本 */
    @ColumnInfo(name = "capital")
    var capital: String = ""

    /** 从业人数 */
    @ColumnInfo(name = "employed_number")
    var employedNum: String = "0"

    /** 组成形式的 Id */
    @ColumnInfo(name = "form_id")
    var formId: String = ""

    /** 性别 index 1男 2女*/
    @ColumnInfo(name = "gender")
    var gender: Int = 0


    /** 身份证号 */
    @ColumnInfo(name = "id_number")
    var idNumber: String = ""

    /** 身份证正反面 */
    @ColumnInfo(name = "id_images_list")
    var idImages: List<IdentityImg> = ArrayList()


    /** 首选名称 */
    @ColumnInfo(name = "name0")
    var name0: String = ""

    /** 备选名称1 */
    @ColumnInfo(name = "name1")
    var name1: String = ""

    /** 备选名称2 */
    @ColumnInfo(name = "name2")
    var name2: String = ""

    /** 民族 */
    @ColumnInfo(name = "nation")
    var nation: String = ""


    /** 法人姓名 */
    @ColumnInfo(name = "real_name")
    var realName: String = ""

    /** 法人联系方式 */
    @ColumnInfo(name = "telephone")
    var tel: String = ""

    /** 区分P9和P10 */
    @ColumnInfo(name = "product_mold")
    var mold: String = ""

}