package com.fatcloud.account.feature.forms.enterprise.license

import android.content.Intent
import android.view.View
import butterknife.OnClick
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.VibrateUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.entity.BusinessScopeEvent
import com.fatcloud.account.event.entity.TabRefreshEvent
import com.fatcloud.account.feature.extra.BusinessScopeActivity
import com.fatcloud.account.feature.home.HomeFragment
import com.fatcloud.account.view.CompanyMemberEditView
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_license_enterprise.*
import kotlinx.android.synthetic.main.layout_bottom_action.*
import java.math.BigDecimal

/**
 * Created by Wangsw on 2020/6/10 0010 18:30.
 * </br>
 *  企业营业执照表单
 */
class FormLicenseEnterpriseActivity : BaseMVPActivity<FormLicenseEnterprisePresenter>(), FormLicenseEnterpriseView {


    private var selectPid = ArrayList<String>()

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
    private var mProductId: String = "0" +
            "-"

    override fun getLayoutId() = R.layout.activity_form_license_enterprise

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun initViews() {
        initExtra()
        initEvent()
        initView()
    }


    private fun initExtra() {

        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_INCOME_MONEY)) {
            finish()
            return
        }

        intent.extras!!.getString(Constants.PARAM_INCOME_MONEY)?.let {
            incomeMoney = it
        }

        intent.extras!!.getString(Constants.PARAM_PRODUCT_ID)?.let {
            incomeMoney = it
        }

        intent.extras!!.getString(Constants.PARAM_FINAL_MONEY)?.let {
            finalMoney = it
        }


        intent.extras!!.getString(Constants.PARAM_PRODUCT_PRICE_ID)?.let {
            finalMoney = it
        }


    }


    private fun initEvent() {

    }


    private fun initView() {
        setMainTitle("注册信息")

        bottom_left_tv.text = getString(R.string.save)
        bottom_right_tv.text = getString(R.string.commit)
        zero_choice_name.setTitleAndHint(getString(R.string.zero_company_name), getString(R.string.no_less_than_3_word))
        first_choice_name.setTitleAndHint(getString(R.string.first_company_name), getString(R.string.no_less_than_3_word))
        second_choice_name.setTitleAndHint(getString(R.string.second_company_name), getString(R.string.no_less_than_3_word))
        investment_period.setTitleAndHint(getString(R.string.invest_year_num), getString(R.string.invest_year_num_hint))
        amount_of_funds.setTitleAndHint(getString(R.string.amount_of_fund), getString(R.string.amount_of_fund_hint))
        bank_number.setTitleAndHint(getString(R.string.bank_card_number), getString(R.string.for_tax_registration))
        bank_phone.setTitleAndHint(getString(R.string.bank_phone), getString(R.string.for_tax_registration))
        detail_addr.setTitleAndHint(getString(R.string.detailed_address), getString(R.string.detailed_address_hint))

        // 法人信息
        legal_person_ev.apply {
            currentMold = Constants.SH1
            initHighlightTitle(getString(R.string.legal_person_info))
            initNameTitleHint(getString(R.string.legal_person_name), getString(R.string.legal_person_name_hint))
            initIdNumberTitleHint(getString(R.string.identity_number), getString(R.string.identity_number_hint))
            initIdAddressTitleHint(getString(R.string.id_address), getString(R.string.id_address_hint))
            initPhoneTitleHint(getString(R.string.contact_number), getString(R.string.contact_number_hint))
            initShareRatioTitleHint(getString(R.string.share_ratio), getString(R.string.share_ratio_hint))
        }

        // 监事信息
        supervisor_ev.apply {
            currentMold = Constants.SH2
            initHighlightTitle(getString(R.string.supervisor_info))
            initNameTitleHint(getString(R.string.supervisor_name), getString(R.string.supervisor_name_hint))
            initIdNumberTitleHint(getString(R.string.identity_number), getString(R.string.identity_number_hint))
            initIdAddressTitleHint(getString(R.string.id_address), getString(R.string.id_address_hint))
            initPhoneTitleHint(getString(R.string.contact_number), getString(R.string.contact_number_hint))
            initShareRatioTitleHint(getString(R.string.share_ratio), getString(R.string.share_ratio_hint_2))
        }

        // 默认股东信息
        shareholder_ev.apply {
            currentMold = Constants.SH3
            initHighlightTitle(getString(R.string.shareholder_info2))
            initNameTitleHint(getString(R.string.shareholder_name), getString(R.string.shareholder_name_hint))
            initIdNumberTitleHint(getString(R.string.identity_number), getString(R.string.identity_number_hint))
            initIdAddressTitleHint(getString(R.string.id_address), getString(R.string.id_address_hint))
            initPhoneTitleHint(getString(R.string.contact_number), getString(R.string.contact_number_hint))
            initShareRatioTitleHint(getString(R.string.share_ratio), getString(R.string.share_ratio_hint_2))
            showAddActionView().setOnClickListener {
                VibrateUtils.vibrate(10)
                it.visibility = View.GONE
                shareholder_more_container.addView(getShareholderView(0), 0)
                scroll_nsv.smoothScrollTo(0, ScreenUtils.getScreenHeight())

            }

        }


    }

    private fun getShareholderView(index: Int): CompanyMemberEditView {
        return CompanyMemberEditView(this).apply {

            currentMold = Constants.SH3
            // 坐标
            tag = index
            initHighlightTitle(getString(R.string.shareholder_info2))
            initNameTitleHint(getString(R.string.shareholder_name), getString(R.string.shareholder_name_hint))
            initIdNumberTitleHint(getString(R.string.identity_number), getString(R.string.identity_number_hint))
            initIdAddressTitleHint(getString(R.string.id_address), getString(R.string.id_address_hint))
            initPhoneTitleHint(getString(R.string.contact_number), getString(R.string.contact_number_hint))
            initShareRatioTitleHint(getString(R.string.share_ratio), getString(R.string.share_ratio_hint_2))

            //  添加股东
            showAddActionView().setOnClickListener {
                VibrateUtils.vibrate(10)
                it.visibility = View.GONE
                shareholder_more_container.addView(getShareholderView(index + 1), index + 1)
                scroll_nsv.smoothScrollTo(0, ScreenUtils.getScreenHeight())
            }

            // 移除当前股东
            showHighlightDeleteView().setOnClickListener {
                shareholder_more_container.removeViewAt(index)
            }

        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && data != null) {
            selectPid = data.getStringArrayListExtra(Constants.KEY_DATA)
        }

    }

    @OnClick(
        R.id.business_scope_rl,
        R.id.bottom_left_tv,
        R.id.bottom_right_tv
    )
    fun onClick(view: View) {
        when (view.id) {
            R.id.business_scope_rl -> {
                // EnterpriseInfo
                startActivityForResult(BusinessScopeActivity::class.java, 1, null)
            }
            R.id.bottom_left_tv -> {


            }

            R.id.bottom_right_tv -> {

            }
            else -> {
            }
        }
    }

    private fun handlePost() {

        val scope: ArrayList<Int> = ArrayList()
        selectPid.forEach {
            scope.add(it.toInt())
        }
        EnterpriseInfo().apply {
            addr = detail_addr.value()
            area = ""
            bankNo = bank_number.value()
            bankNo = bank_phone.value()
            businessScope.addAll(scope)
            enterpriseName0 = zero_choice_name.value()
            enterpriseName1 = first_choice_name.value()
            enterpriseName2 = second_choice_name.value()
            income = BigDecimal(incomeMoney)
            investMoney = BigDecimal(amount_of_funds.value())
            investYearNum = investment_period.value()
            money = BigDecimal(finalMoney)
            productId = mProductId
            productPriceId = mProductPriceId.toInt()
            shareholders
        }
        presenter.addEnterprise(this)


    }


}