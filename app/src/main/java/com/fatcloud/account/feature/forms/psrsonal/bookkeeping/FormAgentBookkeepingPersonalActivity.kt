package com.fatcloud.account.feature.forms.psrsonal.bookkeeping

import android.os.Bundle
import android.view.View
import butterknife.OnClick
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.product.NativeBookkeeping
import com.fatcloud.account.feature.forms.psrsonal.bookkeeping.signature.SignatureActivity
import kotlinx.android.synthetic.main.activity_form_agent_bookkeeping_personal.*

/**
 * Created by Wangsw on 2020/6/13 0013 15:40.
 * </br>
 * 个体户代理记账
 * todo 签字页
 */
class FormAgentBookkeepingPersonalActivity : BaseMVPActivity<FormAgentBookkeepingPersonalPresenter>(), FormAgentBookkeepingPersonalView {

    /**
     * 最终需支付金额
     */
    private var mFinalMoney: String = ""

    /**
     * 产品id
     */
    private var mProductId: String = "0"

    /**
     * 选中的产品价格id
     */
    private var mProductPriceId: String = "0"


    /**
     * 图片地址
     */
    private var mBusinessLicenseImgUrl: String = ""


    override fun getLayoutId() = R.layout.activity_form_agent_bookkeeping_personal

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
            mFinalMoney = it
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
        setMainTitle("法人信息")
        legal_name.setTitleAndHint(R.string.legal_person_name, R.string.legal_person_name)
        legal_phone.setTitleAndHint(R.string.contact_number, R.string.legal_person_phone_hint)
        id_number.setTitleAndHint(R.string.legal_person_id_number, R.string.legal_person_id_number_hint)
        store_name.setTitleAndHint(R.string.store_name, R.string.store_name_hint)
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
                // 去签字页
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

        ProductUtils.checkEditEmptyWithVibrate(legal_name, legal_phone, id_number, store_name)

        startActivity(SignatureActivity::class.java,
            Bundle().apply {
                this.putSerializable(Constants.PARAM_DATA, NativeBookkeeping().apply {
                    finalMoney = mFinalMoney
                    productId = mProductId
                    productPriceId = mProductPriceId
                    legalPersonName = legal_name.value()
                    phone = legal_phone.value()
                    idNumber = id_number.value()
                    storeName = store_name.value()
                    businessLicenseImgUrl = mBusinessLicenseImgUrl
                })
            })




    }


}