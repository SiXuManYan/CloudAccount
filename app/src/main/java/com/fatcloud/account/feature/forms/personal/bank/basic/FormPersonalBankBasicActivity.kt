package com.fatcloud.account.feature.forms.personal.bank.basic

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
import com.fatcloud.account.entity.local.form.BankPersonalDraft
import com.fatcloud.account.entity.users.User
import com.fatcloud.account.feature.forms.personal.bank.FormPersonalBankActivity
import com.fatcloud.account.feature.sheet.nature.AccountNatureSheetFragment
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import kotlinx.android.synthetic.main.activity_form_bank_personal_basic.*
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/7/16 0016 11:53.
 * </br>
 * 个体户对公账户
 */
class FormPersonalBankBasicActivity : BaseMVPActivity<FormPersonalBankBasicPresenter>(), FormPersonalBankBasicView {

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

    /**
     * 用户选中的城市名称
     */
    private var mAreaName: String = ""


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId() = R.layout.activity_form_bank_personal_basic

    override fun initViews() {
        initExtra()
        initView()
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


    private fun initView() {
        setMainTitle("个体户银行对公账户")
        restoreDraft()
    }

    private fun restoreDraft() {

        val draft = BankPersonalDraft.get()
        if (draft.loginPhone != User.get().username || draft.productId.isNullOrBlank() || draft.productId != mProductId) {
            return
        }

        draft.depositorName?.let {
            name_et.setText(it)
        }
        draft.enterpriseCode?.let {
            trn_et.setText(it)
        }
        draft.addressRegistered?.let {
            registered_address_et.setText(it)
        }
        draft.accountType?.let {
            account_nature_value.text = it
        }

        draft.addressPost?.let {
            mAreaName = it
            mailing_address_tv.text = mAreaName
        }

        draft.addressDetailed?.let {
            mailing_detail_address_et.setText(it)
        }


    }

    @OnClick(
        R.id.bottom_right_tv,
        R.id.account_nature_rl,
        R.id.mailing_address_rl,
        R.id.bottom_left_tv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.bottom_right_tv -> {
                ProductUtils.handleDoubleClick(view)
                handleCommit()
            }
            R.id.account_nature_rl -> {
                AccountNatureSheetFragment.newInstance().apply {
                    setOnItemSelectListener(object : AccountNatureSheetFragment.OnItemSelectedListener {
                        override fun onItemSelected(currentSelected: AccountNature) {
                            this@FormPersonalBankBasicActivity.account_nature_value.text = currentSelected.name
                        }
                    })
                    show(supportFragmentManager, this.tag)
                }
            }
            R.id.mailing_address_rl -> {
                ProductUtils.showLocationPicker(this, object : OnCityItemClickListener() {
                    override fun onSelected(province: ProvinceBean, city: CityBean, district: DistrictBean) {
                        mAreaName = StringUtils.getString(
                            R.string.location_information_format,
                            province.name,
                            city.name,
                            district.name
                        )
                        mailing_address_tv.text = mAreaName
                    }

                    override fun onCancel() = Unit
                })
            }
            R.id.bottom_left_tv -> {
                saveDraft()
            }
            else -> {
            }
        }
    }

    private fun saveDraft() {
        val draft = BankPersonalDraft().apply {
            loginPhone = User.get().username

            productId = mProductId
            productPriceId = mProductPriceId
            finalMoney = mFinalMoney
            bank = "渤海银行"
            depositorName = name_et.text.toString().trim()
            enterpriseCode = trn_et.text.toString().trim()
            addressRegistered = registered_address_et.text.toString().trim()
            currency = "人民币"
            accountType = account_nature_value.text.toString().trim()
            addressPost = mAreaName
            addressDetailed = mailing_detail_address_et.text.toString().trim()

        }

        database.bankPersonalDraftDao().add(draft)
        BankPersonalDraft.update()
        ToastUtils.showShort(R.string.save_success)
    }

    private fun handleCommit() {
        val nameValue = name_et.text.toString().trim()
        if (nameValue.isBlank()) {
            ToastUtils.showShort("请输入存款人姓名")
            return
        }
        if (nameValue.length < 2) {
            ToastUtils.showShort("请输入不少于两个字的存款人姓名")
            return
        }
        val taxpayerNumberValue = trn_et.text.toString().trim()
        if (taxpayerNumberValue.isBlank()) {
            ToastUtils.showShort("请输入统一社会信用代码")
            return
        }

        val registeredAddressValue = registered_address_et.text.toString().trim()
        if (registeredAddressValue.isBlank()) {
            ToastUtils.showShort("请输入注册地址")
            return
        }


        val accountNatureValue = account_nature_value.text.toString().trim()
        if (accountNatureValue.isBlank()) {
            ToastUtils.showShort("请选择账户性质")
            return
        }

        if (mAreaName.isBlank()) {
            ToastUtils.showShort("请选择邮寄地址")
            return
        }

        val mailingDetailAddressValue = mailing_detail_address_et.text.toString().trim()
        if (mailingDetailAddressValue.isBlank()) {
            ToastUtils.showShort("请输入详细地址")
            return
        }

        val bundle = Bundle().apply {
            putString(Constants.PARAM_PRODUCT_ID, mProductId)
            putString(Constants.PARAM_FINAL_MONEY, mFinalMoney)
            putString(Constants.PARAM_PRODUCT_PRICE_ID, mProductPriceId)
            putString(Constants.PARAM_NAME, nameValue)
            putString(Constants.PARAM_TAXPAYER_NUMBER, taxpayerNumberValue)
            putString(Constants.PARAM_REGISTERED_ADDRESS, registeredAddressValue)
            putString(Constants.PARAM_ACCOUNT_NATURE, accountNatureValue)
            putString(Constants.PARAM_MAILING_ADDRESS, mAreaName)
            putString(Constants.PARAM_MAILING_DETAIL_ADDRESS, mailingDetailAddressValue)
        }
        startActivity(Intent(this, FormPersonalBankActivity::class.java).putExtras(bundle))
    }


}