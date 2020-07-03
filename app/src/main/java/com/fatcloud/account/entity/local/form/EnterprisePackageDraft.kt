package com.fatcloud.account.entity.local.form

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blankj.utilcode.util.Utils
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.entity.order.enterprise.Shareholder

/**
 * Created by Wangsw on 2020/7/3 0003 11:05.
 * </br>
 * 企业套餐 草稿箱
 */
@Entity(tableName = "tb_enterprise_package_draft")
class EnterprisePackageDraft {

    companion object {
        private var instance: EnterprisePackageDraft? = null
            get() {
                if (field == null) {
                    field =
                        (Utils.getApp() as CloudAccountApplication).database.enterprisePackageDraftDao()
                            .find()
                    if (field == null) {
                        field = EnterprisePackageDraft()
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
            (Utils.getApp() as CloudAccountApplication).database.enterprisePackageDraftDao().clear()
            instance = null
        }
    }




    @PrimaryKey
    @ColumnInfo(name = "id")
    var primaryId: Int = 0


    @ColumnInfo(name = "login_phone")
    var loginPhone: String? = ""

    /**
     * 产品id
     */
    @ColumnInfo(name = "product_id")
    var productId: String? = ""


    /**
     * 年收入 和 finalMoney 一样
     */
    @ColumnInfo(name = "income_money")
    var incomeMoney: String? = ""


    /**
     * 客户端计算出的最终金额
     */
    @ColumnInfo(name = "final_money")
    var finalMoney: String? = ""

    /**
     * 选中的产品价格id
     */
    @ColumnInfo(name = "product_price_id")
    var productPriceId: String? = ""

    /**
     * 用户选中的一级经营范围pid
     */
    @ColumnInfo(name = "select_pid_list")
    var selectPid = ArrayList<String>()

    /**
     * 用户选中的一级经营范围pid名称
     */
    @ColumnInfo(name = "select_pid_name_list")
    var selectPidNames = ArrayList<String>()


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
     * 出资年限
     */
    @ColumnInfo(name = "investment_year")
    var investmentYear: String? = ""


    /**
     * 资金数额
     */
    @ColumnInfo(name = "invest_money")
    var investMoney: String? = ""

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


    @ColumnInfo(name = "share_holders")
    var shareholders: List<Shareholder> ? = ArrayList()

}