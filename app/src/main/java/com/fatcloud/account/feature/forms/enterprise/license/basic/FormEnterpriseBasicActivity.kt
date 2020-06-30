package com.fatcloud.account.feature.forms.enterprise.license.basic

import android.content.Intent
import android.view.View
import butterknife.OnClick
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.event.entity.OrderPaySuccessEvent
import com.fatcloud.account.feature.extra.BusinessScopeActivity
import com.fatcloud.account.feature.forms.enterprise.license.FormLicenseEnterpriseActivity
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_basic.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Wangsw on 2020/6/29 0029 17:04.
 * </br>
 * 企业套餐，前缀页
 */
class FormEnterpriseBasicActivity : BaseMVPActivity<FormEnterpriseBasicPresenter>(), FormEnterpriseBasicView {


    /**
     * 用户选中的一级经营范围pid
     */
    private var selectPid = ArrayList<String>()

    /**
     * 用户选中的一级经营范围pid名称
     */
    private var selectPidNames = ArrayList<String>()

    /**
     * 年收入
     */
    private var incomeMoney: String = ""


    /**
     * 最终收入
     */
    private var finalMoney: String = ""


    /**
     * 选中的产品价格id
     */
    private var mProductPriceId: String = "0"

    /**
     * 产品id
     */
    private var mProductId: String = "0"


    /**
     * 产品类型
     * P1 P2 ....
     */
    private var mProductType: String = Constants.P2

    /**
     * 位置信息id
     */
    private var areaId: String = ""

    private var areaName: String = ""


    override fun getLayoutId() = R.layout.activity_form_basic

    override fun initViews() {
        initExtra()
        initEvent()
        initView()
    }

    private fun initExtra() {
        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_FINAL_MONEY)) {
            finish()
            return
        }

        intent.extras!!.getString(Constants.PARAM_INCOME_MONEY, "0")?.let {
            incomeMoney = it
        }

        intent.extras!!.getString(Constants.PARAM_PRODUCT_ID)?.let {
            mProductId = it
        }

        intent.extras!!.getString(Constants.PARAM_FINAL_MONEY)?.let {
            finalMoney = it
        }

        intent.extras!!.getString(Constants.PARAM_PRODUCT_PRICE_ID)?.let {
            mProductPriceId = it
        }

    }

    private fun initEvent() {

        presenter.subsribeEventEntity<OrderPaySuccessEvent>(Consumer {
            finish()
        })

        presenter.subsribeEvent(Consumer {
            when (it.code) {
                Constants.EVENT_FORM_CLOSE -> {
                    finish()
                }
                else -> {
                }
            }
        })

    }

    private fun initView() {
        setMainTitle("注册信息")
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        when (requestCode) {

            Constants.REQUEST_BUSINESS_SCOPE -> {
                data?.let {
                    selectPid = it.getStringArrayListExtra(Constants.PARAM_SELECT_PID)
                    selectPidNames = it.getStringArrayListExtra(Constants.PARAM_SELECT_PID_NAME)
                    business_scope_value.text = Arrays.toString(selectPidNames.toArray()).replace("[", "").replace("]", "")

                }

            }
            else -> {

            }
        }


    }


    @OnClick(
        R.id.business_scope_rl,
        R.id.bottom_left_tv,
        R.id.bottom_right_tv,
        R.id.addr_rl
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.business_scope_rl -> {
                // 参照 EnterpriseInfo
                startActivityForResult(
                    Intent(this, BusinessScopeActivity::class.java).putExtra(Constants.PARAM_PRODUCT_TYPE, mProductType),
                    Constants.REQUEST_BUSINESS_SCOPE
                )
            }
            R.id.addr_rl -> {
                ProductUtils.showLocationPicker(this, object : OnCityItemClickListener() {
                    override fun onSelected(province: ProvinceBean, city: CityBean, district: DistrictBean) {
                        areaName = StringUtils.getString(R.string.location_information_format, province.name, city.name, district.name)
                        addr_value_iv.text = areaName
                        areaId = district.id
                    }

                    override fun onCancel() = Unit
                })
            }

            R.id.bottom_left_tv -> {
                // 保存
            }
            R.id.bottom_right_tv -> {
                val zeroName = zero_choice_name_et.text.toString()
                if (zeroName.isBlank()) {
                    ToastUtils.showShort("请输入首选公司名称")
                    return
                }

                val firstName = first_choice_name_et.text.toString()
                if (firstName.isBlank()) {
                    ToastUtils.showShort("请输入备选公司名称1")
                    return
                }
                val secondName = second_choice_name_et.text.toString()
                if (secondName.isBlank()) {
                    ToastUtils.showShort("请输入备选公司名称2")
                    return
                }

                val investmentYear = investment_period_et.text.toString()
                if (investmentYear.isBlank()) {
                    ToastUtils.showShort("请输入出资年限")
                    return
                }
                val investMoney = amount_of_funds_et.text.toString()
                if (investMoney.isBlank()) {
                    ToastUtils.showShort("请输入资金数额 ")
                    return
                }
                val bankNumber = bank_number_et.text.toString()
                if (bankNumber.isBlank()) {
                    ToastUtils.showShort("请输入银行卡号 ")
                    return
                }
                val bankPhone = bank_phone_et.text.toString()
                if (bankPhone.isBlank()) {
                    ToastUtils.showShort("请输入银行预留手机号 ")
                    return
                }

                val detailAddress = detail_addr_et.text.toString()
                if (detailAddress.isBlank()) {
                    ToastUtils.showShort("请输入详细地址 ")
                    return
                }

                // 企业套餐
                startActivity(
                    Intent(this, FormLicenseEnterpriseActivity::class.java)
                        .putExtra(Constants.PARAM_PRODUCT_ID, mProductId)
                        .putExtra(Constants.PARAM_INCOME_MONEY, incomeMoney)
                        .putExtra(Constants.PARAM_FINAL_MONEY, finalMoney)
                        .putExtra(Constants.PARAM_PRODUCT_PRICE_ID, mProductPriceId)

                        .putStringArrayListExtra(Constants.PARAM_SELECT_BUSINESS_SCOPE_PID, selectPid)
                        .putStringArrayListExtra(Constants.PARAM_SELECT_BUSINESS_SCOPE_NAME, selectPidNames)
                        .putExtra(Constants.PARAM_SELECT_AREA_NAME, areaName)
                        .putExtra(Constants.PARAM_ZERO_NAME, zeroName)
                        .putExtra(Constants.PARAM_FIRST_NAME, firstName)
                        .putExtra(Constants.PARAM_SECOND_NAME, secondName)
                        .putExtra(Constants.PARAM_INVEST_YEAR, investmentYear)
                        .putExtra(Constants.PARAM_INVEST_MONEY, investMoney)
                        .putExtra(Constants.PARAM_BANK_NUMBER, bankNumber)
                        .putExtra(Constants.PARAM_BANK_PHONE, bankPhone)
                        .putExtra(Constants.PARAM_DETAIL_ADDRESS, detailAddress)
                )


            }

            else -> {


            }

        }
    }


}