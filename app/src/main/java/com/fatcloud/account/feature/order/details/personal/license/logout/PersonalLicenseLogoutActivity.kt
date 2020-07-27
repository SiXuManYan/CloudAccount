package com.fatcloud.account.feature.order.details.personal.license.logout

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
import com.fatcloud.account.entity.order.detail.PersonalLicenseLogoutDetail
import com.fatcloud.account.extend.RoundTransFormation

import kotlinx.android.synthetic.main.activity_personal_license_info_logout.*

/**
 * Created by Wangsw on 2020/7/22 0022 15:28.
 * </br>
 *  P6 个体户营业执照注销回显
 */
class PersonalLicenseLogoutActivity : BaseMVPActivity<PersonalLicenseLogoutPresenter>(), PersonalLicenseLogoutView {


    private var mOrderId: String? = ""

    var mIdFrontUrl: String = ""
    var mIdBackUrl: String = ""
    var mLicenseFrontUrl: String = ""
    var mLicenseBackUrl: String = ""
    var mCommitmentUrl: String = ""


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId(): Int = R.layout.activity_personal_license_info_logout


    private fun initExtra() {
        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_ID)) {
            finish()
            return
        }
        mOrderId = intent.extras!!.getString(Constants.PARAM_ORDER_ID)
    }


    override fun initViews() {
        initExtra()

        setMainTitle("订单详情")
        presenter.getDetailInfo(this, mOrderId)
    }

    override fun bindDetailInfo(data: PersonalLicenseLogoutDetail) {
        CommonUtils.setPaymentStatus(data.state, payment_status_iv, payment_status_tv)
        enterpriseName_tv.text = data.enterpriseName
        enterpriseCode_tv.text = data.enterpriseCode
        reason_tv.text = data.reason
        personLegalName_tv.text = data.legalPersonName
        personLegalIdNumber_tv.text = data.idno
        personLegalPhone_tv.text = data.phone

        data.imgsIdno.forEachIndexed { _, identityImg ->

            val imgUrl = identityImg.imgUrl

            when (identityImg.mold) {
                Constants.I1 -> {

                    if (!imgUrl.isNullOrBlank()) {
                        if (ProductUtils.isOssSignUrl(imgUrl)) {
                            ProductUtils.getRealOssUrl(this, imgUrl, object : CloudAccountApplication.OssSignCallBack {
                                override fun ossUrlSignEnd(url: String) {
                                    Glide.with(this@PersonalLicenseLogoutActivity)
                                        .load(url)
                                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                        .error(R.drawable.ic_error_image_load)
                                        .into(id_card_front_iv)

                                    this@PersonalLicenseLogoutActivity.mIdFrontUrl = url
                                }

                            })
                        } else {
                            Glide.with(this)
                                .load(imgUrl)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(id_card_front_iv)
                            this@PersonalLicenseLogoutActivity.mIdFrontUrl = imgUrl
                        }

                    }
                }

                Constants.I2 -> {
                    if (!imgUrl.isNullOrBlank()) {
                        if (ProductUtils.isOssSignUrl(imgUrl)) {
                            ProductUtils.getRealOssUrl(this, imgUrl, object : CloudAccountApplication.OssSignCallBack {
                                override fun ossUrlSignEnd(url: String) {
                                    Glide.with(this@PersonalLicenseLogoutActivity)
                                        .load(url)
                                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                        .error(R.drawable.ic_error_image_load)
                                        .into(id_card_back_iv)
                                    this@PersonalLicenseLogoutActivity.mIdBackUrl = url
                                }

                            })
                        } else {
                            Glide.with(this)
                                .load(imgUrl)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(id_card_back_iv)
                            this@PersonalLicenseLogoutActivity.mIdBackUrl = imgUrl
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
                                    Glide.with(this@PersonalLicenseLogoutActivity)
                                        .load(url)
                                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                        .error(R.drawable.ic_error_image_load)
                                        .into(license_front_iv)

                                    this@PersonalLicenseLogoutActivity.mLicenseFrontUrl = url
                                }

                            })
                        } else {
                            Glide.with(this)
                                .load(imgUrl)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(license_front_iv)
                            this@PersonalLicenseLogoutActivity.mLicenseFrontUrl = imgUrl
                        }

                    }
                }

                Constants.I4 -> {
                    if (!imgUrl.isNullOrBlank()) {
                        if (ProductUtils.isOssSignUrl(imgUrl)) {
                            ProductUtils.getRealOssUrl(this, imgUrl, object : CloudAccountApplication.OssSignCallBack {
                                override fun ossUrlSignEnd(url: String) {
                                    Glide.with(this@PersonalLicenseLogoutActivity)
                                        .load(url)
                                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                        .error(R.drawable.ic_error_image_load)
                                        .into(license_back_iv)
                                    this@PersonalLicenseLogoutActivity.mLicenseBackUrl = url
                                }

                            })
                        } else {
                            Glide.with(this)
                                .load(imgUrl)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(license_back_iv)
                            this@PersonalLicenseLogoutActivity.mLicenseBackUrl = imgUrl
                        }

                    }
                }
            }


        }





        data.imgsCommitment.forEachIndexed { _, identityImg ->

            val imgUrl = identityImg.imgUrl

            when (identityImg.mold) {
                Constants.I6 -> {

                    if (!imgUrl.isNullOrBlank()) {
                        if (ProductUtils.isOssSignUrl(imgUrl)) {
                            ProductUtils.getRealOssUrl(this, imgUrl, object : CloudAccountApplication.OssSignCallBack {
                                override fun ossUrlSignEnd(url: String) {
                                    Glide.with(this@PersonalLicenseLogoutActivity)
                                        .load(url)
                                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                        .error(R.drawable.ic_error_image_load)
                                        .into(commitment_iv)

                                    this@PersonalLicenseLogoutActivity.mCommitmentUrl = url
                                }
                            })
                        } else {
                            Glide.with(this)
                                .load(imgUrl)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(commitment_iv)
                            this@PersonalLicenseLogoutActivity.mCommitmentUrl = imgUrl
                        }

                    }
                }

            }


        }


    }


    @OnClick(
        R.id.id_card_front_iv,
        R.id.id_card_back_iv,
        R.id.license_front_iv,
        R.id.license_back_iv,
        R.id.commitment_iv

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
            R.id.license_front_iv -> {
                ProductUtils.lookGallery(this, mLicenseFrontUrl)
            }

            R.id.license_back_iv -> {
                ProductUtils.lookGallery(this, mLicenseBackUrl)
            }
            R.id.commitment_iv -> {
                ProductUtils.lookGallery(this, mCommitmentUrl)
            }

            else -> {
            }
        }
    }


}