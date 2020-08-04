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
import com.fatcloud.account.data.CloudDataBase
import com.fatcloud.account.entity.local.form.EnterprisePackageDraft
import com.fatcloud.account.entity.users.User
import com.fatcloud.account.event.entity.OrderPaySuccessEvent
import com.fatcloud.account.extend.LimitInputTextWatcher
import com.fatcloud.account.feature.extra.BusinessScopeActivity
import com.fatcloud.account.feature.forms.enterprise.license.FormLicenseEnterpriseActivity
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_basic.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by Wangsw on 2020/6/29 0029 17:04.
 * </br>
 * 企业套餐，前缀页
 */
class FormEnterpriseBasicActivity : BaseMVPActivity<FormEnterpriseBasicPresenter>(), FormEnterpriseBasicView {

    lateinit var database: CloudDataBase @Inject set

    /**
     * 用户选中的一级经营范围pid
     */
    private var mSelectPid = ArrayList<String>()

    /**
     * 用户选中的一级经营范围pid名称
     */
    private var mSelectPidNames = ArrayList<String>()

    /**
     * 年收入（和 finalMoney 是同一个值，都是客户端计算好的金额，是一个来自接口的额外字段）
     */
    private var incomeMoney: String = ""


    /**
     * 最终收入
     */
    private var mFinalMoney: String = ""


    /**
     * 选中的产品价格id
     */
    private var mProductPriceId: String = "0"

    /**
     * 产品id
     */
    private var mProductId: String = "0"


    /**
     * 位置信息id
     */
    private var mAreaId: String = ""

    private var mAreaName: String = ""


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
            mFinalMoney = it
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
                Constants.EVENT_FORM_CLOSE,
                Constants.EVENT_CLOSE_PAY_UNKNOWN -> {
                    finish()
                }

                else -> {
                }
            }
        })
    }

    private fun initView() {
        setMainTitle("注册信息")
        zero_choice_name_et.addTextChangedListener(LimitInputTextWatcher(zero_choice_name_et))
        first_choice_name_et.addTextChangedListener(LimitInputTextWatcher(first_choice_name_et))
        second_choice_name_et.addTextChangedListener(LimitInputTextWatcher(second_choice_name_et))
        restoreDraft()
    }

    private fun restoreDraft() {

        val draft = EnterprisePackageDraft.get()
        if (draft.loginPhone != User.get().username || draft.productId.isNullOrBlank() || draft.productId != mProductId) {
            return
        }
        draft.zeroName?.let {
            zero_choice_name_et.setText(it)
        }
        draft.firstName?.let {
            first_choice_name_et.setText(it)
        }
        draft.secondName?.let {
            second_choice_name_et.setText(it)
        }
        draft.investmentYear?.let {
            investment_period_et.setText(it)
        }
        draft.investMoney?.let {
            amount_of_funds_et.setText(it)
        }

        draft.selectPid?.let {
            mSelectPid = it
        }

        draft.selectPidNames?.let {
            mSelectPidNames = it
            business_scope_value.text =
                Arrays.toString(it.toArray()).replace("[", "").replace("]", "")
        }


        draft.bankNumber?.let {
            bank_number_et.setText(it)
        }
        draft.bankPhone?.let {
            bank_phone_et.setText(it)
        }
        draft.area?.let {
            mAreaName = it
            addr_value_tv.text = it
        }
        draft.areaId?.let {
            mAreaId = it
        }
        draft.detailAddress?.let {
            detail_addr_et.setText(it)
        }

    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        when (requestCode) {

            Constants.REQUEST_BUSINESS_SCOPE -> {
                data?.let {
                    mSelectPid = it.getStringArrayListExtra(Constants.PARAM_SELECT_PID)
                    mSelectPidNames = it.getStringArrayListExtra(Constants.PARAM_SELECT_PID_NAME)
                    business_scope_value.text = Arrays.toString(mSelectPidNames.toArray()).replace("[", "").replace("]", "")
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
                startActivityForResult(
                    Intent(
                        this,
                        BusinessScopeActivity::class.java
                    ).putExtra(Constants.PARAM_PRODUCT_TYPE, Constants.P2),
                    Constants.REQUEST_BUSINESS_SCOPE
                )
            }
            R.id.addr_rl -> {
                ProductUtils.showLocationPicker(this, object : OnCityItemClickListener() {
                    override fun onSelected(
                        province: ProvinceBean,
                        city: CityBean,
                        district: DistrictBean
                    ) {
                        mAreaName = StringUtils.getString(
                            R.string.location_information_format,
                            province.name,
                            city.name,
                            district.name
                        )
                        addr_value_tv.text = mAreaName
                        mAreaId = district.id
                    }

                    override fun onCancel() = Unit
                })
            }

            R.id.bottom_left_tv -> {
                saveDraft()
            }
            R.id.bottom_right_tv -> {
                handleCommit(view)
            }

            else -> {


            }

        }
    }


    /**
     * 保存草稿
     */
    private fun saveDraft() {

        val draft = EnterprisePackageDraft().apply {
            loginPhone = User.get().username
            productId = mProductId
            productPriceId = mProductPriceId
            finalMoney = mFinalMoney
            zeroName = zero_choice_name_et.text.toString().trim()
            firstName = first_choice_name_et.text.toString().trim()
            secondName = second_choice_name_et.text.toString().trim()
            investmentYear = investment_period_et.text.toString().trim()
            investMoney = amount_of_funds_et.text.toString().trim()
            selectPid = mSelectPid
            selectPidNames = mSelectPidNames
            bankNumber = bank_number_et.text.toString().trim()
            bankPhone = bank_phone_et.text.toString().trim()
            area = mAreaName
            areaId = mAreaId
            detailAddress = detail_addr_et.text.toString()
        }
        database.enterprisePackageDraftDao().add(draft)
        EnterprisePackageDraft.update()
        ToastUtils.showShort(R.string.save_success)

    }

    private fun handleCommit(view: View) {
        ProductUtils.handleDoubleClick(view)
        val zeroName = zero_choice_name_et.text.toString().trim()
        if (!ProductUtils.isThreeChineseName(zeroName, getString(R.string.zero_choice_name))) {
            return
        }
        val firstName = first_choice_name_et.text.toString().trim()
        if (!ProductUtils.isThreeChineseName(firstName, getString(R.string.first_choice_name))) {
            return
        }
        val secondName = second_choice_name_et.text.toString().trim()
        if (!ProductUtils.isThreeChineseName(secondName, getString(R.string.second_choise_name))) {
            return
        }


        val investmentYear = investment_period_et.text.toString()
        if (investmentYear.isBlank()) {
            ToastUtils.showShort("请输入出资年限")
            return
        }
        val investMoney = amount_of_funds_et.text.toString()
        if (investMoney.isBlank()) {
            ToastUtils.showShort("请输入资金数额")
            return
        }
        try {
            val investMoneyInt = investMoney.toInt()
            if (investMoneyInt < 50) {
                ToastUtils.showShort("资金数额不能少于50万")
                return
            }

            if (investMoneyInt >= 10000) {
                ToastUtils.showShort("资金数额不能大于10000万")
                return
            }


        } catch (e: Exception) {
        }



        if (business_scope_value.text.toString().trim().isBlank()) {
            ToastUtils.showShort("请选择经营范围")
            return
        }


        val bankNumber = bank_number_et.text.toString()
        if (bankNumber.isBlank()) {
            ToastUtils.showShort("请输入银行卡号")
            return
        }
        if (!ProductUtils.isBankCardNumber(bankNumber)) {
            return
        }

        val bankPhone = bank_phone_et.text.toString()
        if (bankPhone.isBlank()) {
            ToastUtils.showShort("请输入银行预留手机号 ")
            return
        }
        if (!ProductUtils.isPhoneNumber(bankPhone, "银行预留")) {
            return
        }

        if (addr_value_tv.text.toString().trim().isBlank()) {
            ToastUtils.showShort("请选择地址")
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
                .putExtra(Constants.PARAM_FINAL_MONEY, mFinalMoney)
                .putExtra(Constants.PARAM_PRODUCT_PRICE_ID, mProductPriceId)

                .putStringArrayListExtra(
                    Constants.PARAM_SELECT_BUSINESS_SCOPE_PID,
                    mSelectPid
                )
                .putStringArrayListExtra(
                    Constants.PARAM_SELECT_BUSINESS_SCOPE_NAME,
                    mSelectPidNames
                )
                .putExtra(Constants.PARAM_SELECT_AREA_NAME, mAreaName)
                .putExtra(Constants.PARAM_DETAIL_ADDRESS, detailAddress)
                .putExtra(Constants.PARAM_ZERO_NAME, zeroName)
                .putExtra(Constants.PARAM_FIRST_NAME, firstName)
                .putExtra(Constants.PARAM_SECOND_NAME, secondName)
                .putExtra(Constants.PARAM_INVEST_YEAR, investmentYear)
                .putExtra(Constants.PARAM_INVEST_MONEY, investMoney)
                .putExtra(Constants.PARAM_BANK_NUMBER, bankNumber)
                .putExtra(Constants.PARAM_BANK_PHONE, bankPhone)

        )
    }


}