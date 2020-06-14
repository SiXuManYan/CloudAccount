package com.fatcloud.account.feature.forms.personal.tax

import android.text.InputType
import android.view.View
import butterknife.OnClick
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import kotlinx.android.synthetic.main.activity_form_tax_registration_personal.*

/**
 * Created by Wangsw on 2020/6/13 0013 13:47.
 * </br>
 * 个体户税务登记
 */
class FormTaxRegistrationPersonalActivity : BaseMVPActivity<FormTaxRegistrationPersonalPresenter>(), FormTaxRegistrationPersonalView {


    /**
     * 最终需支付金额
     */
    private var finalMoney: String = ""

    /**
     * 是否额外选择了刻章业务
     */
    private var addSeal: Boolean = false


    /**
     * 产品id
     */
    private var mProductId: String = "0"

    /**
     * 选中的产品价格id
     */
    private var mProductPriceId: String = "0"

    /**
     * 地址
     */
    private var address: String = ""

    /**
     * 图片地址
     */
    private var businessLicenseImgUrl: String = ""


    override fun getLayoutId() = R.layout.activity_form_tax_registration_personal

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

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

        intent.extras!!.getString(Constants.PARAM_FINAL_MONEY)?.let {
            finalMoney = it
        }

        intent.extras!!.getBoolean(Constants.PARAM_ADD_SEAL, false).let {
            addSeal = it
        }

        intent.extras!!.getString(Constants.PARAM_PRODUCT_ID)?.let {
            mProductId = it
        }



        intent.extras!!.getString(Constants.PARAM_PRODUCT_PRICE_ID)?.let {
            mProductPriceId = it
        }


    }

    private fun initEvent() {

    }

    private fun initView() {
        setMainTitle("办理信息")
        trn_ev.setTitleAndHint(R.string.taxpayer_registration_number, R.string.taxpayer_registration_number_hint)
            .setInputType(InputType.TYPE_CLASS_NUMBER)
        legal_name.setTitleAndHint(R.string.legal_person_name, R.string.legal_person_name)
        id_number.setTitleAndHint(R.string.identity_number, R.string.identity_number_hint).setInputType(InputType.TYPE_CLASS_NUMBER)
        bank_number.setTitleAndHint(R.string.bank_card_number, R.string.bank_card_number_hint).setInputType(InputType.TYPE_CLASS_NUMBER)
        bank_phone.setTitleAndHint(R.string.bank_phone, R.string.bank_phone_hint).setInputType(InputType.TYPE_CLASS_NUMBER)

        if (addSeal) {
            addr_rl.visibility = View.VISIBLE
            detail_addr.setTitleAndHint(R.string.detailed_address, R.string.detailed_address).visibility = View.VISIBLE
        } else {
            addr_rl.visibility = View.GONE
            detail_addr.visibility = View.GONE
        }

    }

    @OnClick(
        R.id.commit_tv,
        R.id.id_card_front_iv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.commit_tv -> {
                handleCommit()
            }
            R.id.id_card_front_iv -> {
                // todo 上传图片至阿里云
            }
            else -> {
            }
        }
    }

    private fun handleCommit() {

        if (!ProductUtils.checkEditEmptyWithVibrate(trn_ev, legal_name, id_number, bank_number, bank_phone)) {
            return
        }

        presenter.addLicensePersonal(
            this,
            finalMoney,
            mProductId,
            mProductPriceId,
            trn_ev.value(),
            legal_name.value(),
            id_number.value(),
            bank_number.value(),
            bank_phone.value(),
            businessLicenseImgUrl,
            address,
            detail_addr.value()
        )


    }

}