package com.fatcloud.account.feature.forms.personal.change

import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.data.CloudDataBase
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/7/10 0010 15:37.
 * </br>
 *  个体户营业执照变更
 *   @see  Constants.P5
 */
class FormLicenseChangeActivity : BaseMVPActivity<FormLicenseChangePresenter>(), FormLicenseChangeView {

    lateinit var database: CloudDataBase @Inject set

    /**
     * 产品id
     */
    private var mProductId: String = "0"

    /**
     * 最终需支付金额
     */
    private var mFinalMoney: String = ""

    /**
     * 选中的产品价格id
     */
    private var mProductPriceId: String = "0"


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId() = R.layout.activity_form_license_change_personal

    override fun initViews() {
        initExtra()
        initEvent()
        initView()
    }

    private fun initView() {

    }

    private fun initEvent() {

    }


    private fun initExtra() {

        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_FINAL_MONEY)) {
            finish()
            return
        }

        intent.extras!!.getString(Constants.PARAM_PRODUCT_ID)?.let {
            mProductId = it
        }

        intent.extras!!.getString(Constants.PARAM_FINAL_MONEY)?.let {
            mFinalMoney = it
        }

        intent.extras!!.getString(Constants.PARAM_PRODUCT_PRICE_ID)?.let {
            mProductPriceId = it
        }



    }

}