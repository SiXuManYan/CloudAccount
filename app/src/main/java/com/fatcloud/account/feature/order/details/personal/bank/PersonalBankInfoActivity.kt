package com.fatcloud.account.feature.order.details.personal.bank

import android.view.View
import butterknife.OnClick
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.order.persional.bank.PersonalBankDetailP8
import com.fatcloud.account.entity.order.persional.bank.PersonalBankDetailP9P10
import com.fatcloud.account.extend.RoundTransFormation
import kotlinx.android.synthetic.main.activity_personal_bank_info.*

/**
 * Created by Wangsw on 2020/7/21 0021 14:52.
 * </br>
 *  个体户银行对公账户回显
 *  单独产品P8回显，tOrder/detail
 *  以及P9个体户套餐的银行对公账户回显 /tOrderWork/detail
 */

class PersonalBankInfoActivity : BaseMVPActivity<PersonalBankInfoPresenter>(), PersonalBankInfoView {


    private var orderId: String? = ""



    /**
     * 订单流程ID
     */
    private var mOrderWorkId: String? = ""


    /**
     * 产品类型
     */
    private var mMold: String = ""


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId(): Int = R.layout.activity_personal_bank_info

    private fun initExtra() {
        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_ID)) {
            finish()
            return
        }
        orderId = intent.extras!!.getString(Constants.PARAM_ORDER_ID)

        mOrderWorkId = intent.extras!!.getString(Constants.PARAM_ORDER_WORK_ID)

        intent.extras!!.getString(Constants.PARAM_MOLD)?.let {
            mMold = it
        }

    }

    override fun initViews() {
        initExtra()

        setMainTitle("订单详情")

        when (mMold) {
            Constants.P8 -> {
                presenter.getDetailInfoP8(this, orderId)
            }
            Constants.P9,Constants.P10->{
                presenter.getDetailInfoP9P10(this, mOrderWorkId)
            }
            else -> {
            }
        }

    }

    var mIdFrontUrl: String = ""
    var mIdBackUrl: String = ""
    var mLicenseUrl: String = ""
    var mBasicImageUrl: String = ""

    override fun bindDetail(data: PersonalBankDetailP8) {

        ProductUtils.setPaymentStatus(data.state, data.stateText, payment_status_iv, payment_status_tv)
        depositorName_tv.text = data.depositorName
        enterpriseCode_tv.text = data.enterpriseCode
        addressRegistered_tv.text = data.addressRegistered
        accountType_tv.text = data.accountType
        currency_tv.text = data.currency
        addressPost_tv.text = data.addressPost

        personLegalName_tv.text = data.personLegal.name
        personLegalPhone_tv.text = data.personLegal.phone

        personFinanceName_tv.text = data.personFinance.name
        personFinancePhone_tv.text = data.personFinance.phone

        personVerification1Name_tv.text = data.personVerification1.name
        personVerification1Phone_tv.text = data.personVerification1.phone

        personVerification2Name_tv.text = data.personVerification2.name
        personVerification2Phone_tv.text = data.personVerification2.phone

        personReconciliationName_tv.text = data.personReconciliation.name
        personReconciliationPhone_tv.text = data.personReconciliation.phone

        data.imgsIdno.forEachIndexed { _, identityImg ->

            val imgUrl = identityImg.imgUrl

            when (identityImg.mold) {
                Constants.I1 -> {

                    if (!imgUrl.isNullOrBlank()) {
                        if (ProductUtils.isOssSignUrl(imgUrl)) {
                            ProductUtils.getRealOssUrl(this, imgUrl, object : CloudAccountApplication.OssSignCallBack {
                                override fun ossUrlSignEnd(url: String) {
                                    Glide.with(this@PersonalBankInfoActivity)
                                        .load(url)
                                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                        .error(R.drawable.ic_error_image_load)
                                        .into(id_card_front_iv)

                                    this@PersonalBankInfoActivity.mIdFrontUrl = url
                                }

                            })
                        } else {
                            Glide.with(this)
                                .load(imgUrl)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(id_card_front_iv)
                            this@PersonalBankInfoActivity.mIdFrontUrl = imgUrl
                        }

                    }
                }

                Constants.I2 -> {
                    if (!imgUrl.isNullOrBlank()) {
                        if (ProductUtils.isOssSignUrl(imgUrl)) {
                            ProductUtils.getRealOssUrl(this, imgUrl, object : CloudAccountApplication.OssSignCallBack {
                                override fun ossUrlSignEnd(url: String) {
                                    Glide.with(this@PersonalBankInfoActivity)
                                        .load(url)
                                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                        .error(R.drawable.ic_error_image_load)
                                        .into(id_card_back_iv)
                                    this@PersonalBankInfoActivity.mIdBackUrl = url
                                }

                            })
                        } else {
                            Glide.with(this)
                                .load(imgUrl)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(id_card_back_iv)
                            this@PersonalBankInfoActivity.mIdBackUrl = imgUrl
                        }

                    }
                }
            }


        }


        data.imgsLicense.forEachIndexed { _, identityImg ->
            // I3
            val imgUrl = identityImg.imgUrl

            if (!imgUrl.isNullOrBlank()) {
                if (ProductUtils.isOssSignUrl(imgUrl)) {
                    ProductUtils.getRealOssUrl(this, imgUrl, object : CloudAccountApplication.OssSignCallBack {
                        override fun ossUrlSignEnd(url: String) {
                            Glide.with(this@PersonalBankInfoActivity)
                                .load(url)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(license_iv)
                            mLicenseUrl = url
                        }
                    })
                } else {
                    Glide.with(this)
                        .load(imgUrl)
                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                        .error(R.drawable.ic_error_image_load)
                        .into(license_iv)

                    mLicenseUrl = imgUrl
                }

            }
            return@forEachIndexed


        }


        if (data.imgsDepositAccount.isNullOrEmpty()) {
            basic_rl.visibility = View.GONE
        } else {

            // I7
            basic_rl.visibility = View.VISIBLE
            data.imgsDepositAccount.forEachIndexed { _, identityImg ->

                val imgUrl = identityImg.imgUrl

                if (!imgUrl.isNullOrBlank()) {
                    if (ProductUtils.isOssSignUrl(imgUrl)) {
                        ProductUtils.getRealOssUrl(this, imgUrl, object : CloudAccountApplication.OssSignCallBack {
                            override fun ossUrlSignEnd(url: String) {
                                Glide.with(this@PersonalBankInfoActivity)
                                    .load(url)
                                    .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                    .error(R.drawable.ic_error_image_load)
                                    .into(deposit_iv)
                                mBasicImageUrl = url
                            }
                        })
                    } else {
                        Glide.with(this)
                            .load(imgUrl)
                            .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                            .error(R.drawable.ic_error_image_load)
                            .into(deposit_iv)
                        mBasicImageUrl = imgUrl
                    }
                }

                return@forEachIndexed

            }
        }

    }

    override fun bindDetailP9P10(it: PersonalBankDetailP9P10) {

        val p8Model = PersonalBankDetailP8().apply {
            state = it.state
            stateText = it.stateText
            depositorName = it.depositorName
            enterpriseCode = it.enterpriseCode
            addressRegistered = it.addressRegistered
            accountType = it.accountType
            currency = it.currency
            addressPost = it.addressPost
            personLegal = it.personLegal
            personFinance = it.personFinance
            personVerification1 = it.personVerification1
            personVerification2 = it.personVerification2
            personReconciliation = it.personReconciliation
            imgsIdno = it.imgsIdno
            imgsLicense = it.imgsLicense
            imgsDepositAccount = it.imgsDepositAccount
        }

        bindDetail(p8Model)
    }


    @OnClick(
        R.id.id_card_front_iv,
        R.id.id_card_back_iv,
        R.id.license_iv,
        R.id.deposit_iv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.id_card_front_iv -> {
                ProductUtils.lookGallery(this, mIdFrontUrl)
            }
            R.id.id_card_back_iv -> {
                ProductUtils.lookGallery(this, mIdBackUrl)
            }
            R.id.license_iv -> {
                ProductUtils.lookGallery(this, mLicenseUrl)
            }
            R.id.deposit_iv -> {
                ProductUtils.lookGallery(this, mBasicImageUrl)
            }
            else -> {
            }
        }
    }


}