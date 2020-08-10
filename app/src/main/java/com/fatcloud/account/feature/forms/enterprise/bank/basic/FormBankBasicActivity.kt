package com.fatcloud.account.feature.forms.enterprise.bank.basic

import android.content.Intent
import android.os.Bundle
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
import com.fatcloud.account.entity.commons.AccountNature
import com.fatcloud.account.entity.local.form.BankPublicDraft
import com.fatcloud.account.entity.users.User
import com.fatcloud.account.event.entity.BankFormCommitSuccessEvent
import com.fatcloud.account.feature.forms.enterprise.bank.FormBankActivity
import com.fatcloud.account.feature.sheet.nature.AccountNatureSheetFragment
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_bank_basic.*
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/7/1 0001 15:42.
 * </br>
 * 银行表单准备页
 */
class FormBankBasicActivity : BaseMVPActivity<FormBankBasicPresenter>(), FormBankBasicView {

    lateinit var database: CloudDataBase @Inject set

    /**
     * 订单流程id
     */
    var mOrderWorkId: String? = ""


    var mAreaName: String? = ""
    var mAreaId: String? = ""


    override fun getLayoutId() = R.layout.activity_form_bank_basic

    override fun initViews() {
        initExtra()
        initEvent()
        initView()
    }

    private fun initEvent() {
        // 银行对公账户表单添加成功
        presenter.subsribeEventEntity<BankFormCommitSuccessEvent>(Consumer {
            finish()
        })

    }


    private fun initExtra() {
        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_WORK_ID)) {
            finish()
            return
        }
        mOrderWorkId = intent.extras!!.getString(Constants.PARAM_ORDER_WORK_ID)
    }

    private fun initView() {
        setMainTitle("开立银行对公账户")
        ProductUtils.onlySupportChineseInput(reconciliation_name_et)
        restoreDraft()
    }

    private fun restoreDraft() {
        val draft = BankPublicDraft.get()
        if (draft.loginPhone != User.get().username || draft.orderWorkId.isNullOrBlank() || draft.orderWorkId != mOrderWorkId) {
            return
        }

        draft.companyName?.let {
            company_name_et.setText(it)
        }

        draft.companyAddress?.let {
            company_address_et.setText(it)
        }
        draft.registeredCapital?.let {
            registered_capital_et.setText(it)
        }

        draft.accountNatureValue?.let {
            account_nature_value.setText(it)
        }
        draft.reconciliationName?.let {
            reconciliation_name_et.setText(it)
        }
        draft.area?.let {
            mAreaName = it
        }
        draft.areaId?.let {
            mAreaId = it
        }
        draft.postcode?.let {
            postcode_et.setText(it)
        }

    }


    @OnClick(
        R.id.bottom_left_tv,
        R.id.bottom_right_tv,
        R.id.account_nature_rl,
        R.id.recipient_address_rl
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {

            R.id.bottom_left_tv -> {
                saveDraft()
            }
            R.id.bottom_right_tv -> {
                ProductUtils.handleDoubleClick(view)
                handleNext()
            }
            R.id.account_nature_rl -> {
                AccountNatureSheetFragment.newInstance().apply {
                    setOnItemSelectListener(object : AccountNatureSheetFragment.OnItemSelectedListener {
                        override fun onItemSelected(currentSelected: AccountNature) {
                            this@FormBankBasicActivity.account_nature_value.text = currentSelected.name
                        }
                    })
                    show(supportFragmentManager, this.tag)
                }
            }
            R.id.recipient_address_rl -> {
                ProductUtils.showLocationPicker(this, object : OnCityItemClickListener() {
                    override fun onSelected(province: ProvinceBean, city: CityBean, district: DistrictBean) {

                        mAreaName = StringUtils.getString(R.string.location_information_format, province.name, city.name, district.name)
                        mAreaId = district.id
                        province_title_tv.text = mAreaName
                    }

                    override fun onCancel() = Unit
                })
            }
            else -> {
            }
        }
    }

    private fun saveDraft() {
        val draft = BankPublicDraft().apply {
            loginPhone = User.get().username
            orderWorkId = mOrderWorkId

            companyName = company_name_et.text.toString().trim()
            companyAddress = company_address_et.text.toString().trim()
            registeredCapital = registered_capital_et.text.toString().trim()
            accountNatureValue = account_nature_value.text.toString().trim()
            reconciliationName = reconciliation_name_et.text.toString().trim()
            area = mAreaName
            areaId = mAreaId
            postcode = postcode_et.text.toString().trim()
        }
        database.bankPublicDraftDao().add(draft)
        BankPublicDraft.update()
        ToastUtils.showShort(R.string.save_success)

    }


    private fun handleNext() {

        val companyName = company_name_et.text.toString().trim()
        if (companyName.isBlank()) {
            ToastUtils.showShort("请输入公司名称")
            return
        }

        val companyAddress = company_address_et.text.toString().trim()
        if (companyAddress.isBlank()) {
            ToastUtils.showShort("请输入公司地址")
            return
        }


        val postcodeValue = postcode_et.text.toString().trim()
        if (postcodeValue.isBlank()) {
            ToastUtils.showShort("请输入邮编")
            return
        }


        val registeredCapital = registered_capital_et.text.toString().trim()
        if (registeredCapital.isBlank()) {
            ToastUtils.showShort("请输入注册资金")
            return
        }

        val accountNatureValue = account_nature_value.text.toString().trim()
        if (accountNatureValue.isBlank()) {
            ToastUtils.showShort("请选择账户性质")
            return
        }

        val reconciliationName = reconciliation_name_et.text.toString().trim()
        if (reconciliationName.isBlank()) {
            ToastUtils.showShort("请输入对账联系人")
            return
        }

        val reconciliationPhone = reconciliation_phone_et.text.toString().trim()
        if (reconciliationPhone.isBlank()) {
            ToastUtils.showShort("请输入对账联系方式")
            return
        }
        if (!ProductUtils.isPhoneNumber(reconciliationPhone, "对账联系人")) {
            return
        }

        val areaName = province_title_tv.text.toString().trim()
        if (areaName.isBlank()) {
            ToastUtils.showShort("请选择省份")
            return
        }

        val detailAddr = detail_addr_et.text.toString().trim()
        if (detailAddr.isBlank()) {
            ToastUtils.showShort("请输入详细地址")
            return
        }


        val bundle = Bundle().apply {
            putString(Constants.PARAM_ORDER_WORK_ID, mOrderWorkId)
            putString(Constants.PARAM_COMPANY_NAME, companyName)
            putString(Constants.PARAM_COMPANY_ADDRESS, companyAddress)
            putString(Constants.PARAM_REGISTERED_CAPITAL, registeredCapital)
            putString(Constants.PARAM_ACCOUNT_NATURE, accountNatureValue)
            putString(Constants.PARAM_RECONCILIATION_NAME, reconciliationName)
            putString(Constants.PARAM_RECONCILIATION_PHONE, reconciliationPhone)
            putString(Constants.PARAM_AREA_NAME, areaName)
            putString(Constants.PARAM_DETAIL_ADDRESS, detailAddr)
            putString(Constants.PARAM_POST_CODE, postcodeValue)
        }
        startActivity(Intent(this, FormBankActivity::class.java).putExtras(bundle))

    }

}