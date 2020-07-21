package com.fatcloud.account.feature.order.details.personal.bank

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
import com.fatcloud.account.entity.order.persional.bank.PersonalBankDetail
import com.fatcloud.account.extend.RoundTransFormation
import kotlinx.android.synthetic.main.activity_personal_bank_info.*

/**
 * Created by Wangsw on 2020/7/21 0021 14:52.
 * </br>
 *
 */

class PersonalBankInfoActivity : BaseMVPActivity<PersonalBankInfoPresenter>(), PersonalBankInfoView {


    /**
     * 订单id，
     * 当为 产品流程类型为：PW3银行账户办理时，tOrderWork/detail 接口不返回相关法人信息
     * 需要使用订单id调用  tOrder/detail 接口请求法人股东信息
     *
     */
    private var orderId: String? = ""


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId(): Int = R.layout.activity_personal_bank_info

    private fun initExtra() {
        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_WORK_ID)) {
            finish()
            return
        }
        orderId = intent.extras!!.getString(Constants.PARAM_ORDER_ID)
    }

    override fun initViews() {
        initExtra()

        setMainTitle("订单详情")
        presenter.getDetailInfo(this, orderId)
    }

    override fun bindDetail(data: PersonalBankDetail) {

        CommonUtils.setPaymentStatus(data.state, payment_status_iv, payment_status_tv)
        bank_name_tv.text = data.bank
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

                                }

                            })
                        } else {
                            Glide.with(this)
                                .load(imgUrl)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(id_card_front_iv)
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

                                }

                            })
                        } else {
                            Glide.with(this)
                                .load(imgUrl)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(id_card_back_iv)
                        }

                    }
                }
            }


        }


        data.imgsLicense.forEachIndexed { _, identityImg ->

            val imgUrl = identityImg.imgUrl

            when (identityImg.mold) {
                Constants.I3 -> {
                    if (!imgUrl.isNullOrBlank()) {
                        if (ProductUtils.isOssSignUrl(imgUrl)) {
                            ProductUtils.getRealOssUrl(this, imgUrl, object : CloudAccountApplication.OssSignCallBack {
                                override fun ossUrlSignEnd(url: String) {
                                    Glide.with(this@PersonalBankInfoActivity)
                                        .load(url)
                                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                        .error(R.drawable.ic_error_image_load)
                                        .into(license_iv)
                                }
                            })
                        } else {
                            Glide.with(this)
                                .load(imgUrl)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(license_iv)
                        }

                    }
                }


            }


        }



        data.imgsDepositAccount.forEachIndexed { _, identityImg ->

            val imgUrl = identityImg.imgUrl

            when (identityImg.mold) {
                Constants.I7 -> {
                    if (!imgUrl.isNullOrBlank()) {
                        if (ProductUtils.isOssSignUrl(imgUrl)) {
                            ProductUtils.getRealOssUrl(this, imgUrl, object : CloudAccountApplication.OssSignCallBack {
                                override fun ossUrlSignEnd(url: String) {
                                    Glide.with(this@PersonalBankInfoActivity)
                                        .load(url)
                                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                        .error(R.drawable.ic_error_image_load)
                                        .into(deposit_iv)
                                }
                            })
                        } else {
                            Glide.with(this)
                                .load(imgUrl)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(deposit_iv)
                        }

                    }
                }


            }


        }


    }


}