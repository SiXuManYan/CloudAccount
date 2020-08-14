package com.fatcloud.account.feature.order.details.personal.license.change

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
import com.fatcloud.account.entity.order.detail.PersonalLicenseChangeDetail
import com.fatcloud.account.extend.RoundTransFormation
import kotlinx.android.synthetic.main.activity_personal_license_info_change.*

/**
 * Created by Wangsw on 2020/7/22 0022 11:41.
 * </br>
 * P5 个体户营业支支招变更 回显页
 */
class PersonalLicenseChangeInfoActivity : BaseMVPActivity<PersonalLicenseChangeInfoPresenter>(), PersonalLicenseChangeInfoView {


    /**
     * 订单id，
     * 当为 产品流程类型为：PW3银行账户办理时，tOrderWork/detail 接口不返回相关法人信息
     * 需要使用订单id调用  tOrder/detail 接口请求法人股东信息
     *
     */
    private var orderId: String? = ""


    var mIdFrontUrl: String = ""
    var mIdBackUrl: String = ""
    var mLicenseFrontUrl: String = ""
    var mLicenseBackUrl: String = ""


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId(): Int = R.layout.activity_personal_license_info_change


    private fun initExtra() {
        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_ID)) {
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

    override fun bindDetailInfo(data: PersonalLicenseChangeDetail) {
        ProductUtils.setPaymentStatus(data.state, data.stateText, payment_status_iv, payment_status_tv)
        data.enterpriseName0.apply {
            if (isNullOrBlank()) {
                name0_ll.visibility = View.GONE
            } else {
                name0_ll.visibility = View.VISIBLE
                name0_tv.text = this
            }
        }
        data.enterpriseName1.apply {
            if (isNullOrBlank()) {
                name1_ll.visibility = View.GONE
            } else {
                name1_ll.visibility = View.VISIBLE
                name1_tv.text = this
            }
        }
        data.enterpriseName2.apply {
            if (isNullOrBlank()) {
                name2_ll.visibility = View.GONE
            } else {
                name2_ll.visibility = View.VISIBLE
                name2_tv.text = this
            }
        }
        data.businessScopeNames.apply {
            if (isNullOrBlank()) {
                business_scope_ll.visibility = View.GONE
            } else {
                business_scope_ll.visibility = View.VISIBLE
                business_scope_tv.text = this
            }
        }

        personLegalName_tv.text = data.legalPersonName
        personLegalIdNumber_tv.text = data.idno

        data.imgsIdno.forEachIndexed { _, identityImg ->

            val imgUrl = identityImg.imgUrl

            when (identityImg.mold) {
                Constants.I1 -> {

                    if (!imgUrl.isNullOrBlank()) {
                        if (ProductUtils.isOssSignUrl(imgUrl)) {
                            ProductUtils.getRealOssUrl(this, imgUrl, object : CloudAccountApplication.OssSignCallBack {
                                override fun ossUrlSignEnd(url: String) {
                                    Glide.with(this@PersonalLicenseChangeInfoActivity)
                                        .load(url)
                                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                        .error(R.drawable.ic_error_image_load)
                                        .into(id_card_front_iv)

                                    this@PersonalLicenseChangeInfoActivity.mIdFrontUrl = url
                                }

                            })
                        } else {
                            Glide.with(this)
                                .load(imgUrl)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(id_card_front_iv)
                            this@PersonalLicenseChangeInfoActivity.mIdFrontUrl = imgUrl
                        }

                    }
                }

                Constants.I2 -> {
                    if (!imgUrl.isNullOrBlank()) {
                        if (ProductUtils.isOssSignUrl(imgUrl)) {
                            ProductUtils.getRealOssUrl(this, imgUrl, object : CloudAccountApplication.OssSignCallBack {
                                override fun ossUrlSignEnd(url: String) {
                                    Glide.with(this@PersonalLicenseChangeInfoActivity)
                                        .load(url)
                                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                        .error(R.drawable.ic_error_image_load)
                                        .into(id_card_back_iv)
                                    this@PersonalLicenseChangeInfoActivity.mIdBackUrl = url
                                }

                            })
                        } else {
                            Glide.with(this)
                                .load(imgUrl)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(id_card_back_iv)
                            this@PersonalLicenseChangeInfoActivity.mIdBackUrl = imgUrl
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
                                    Glide.with(this@PersonalLicenseChangeInfoActivity)
                                        .load(url)
                                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                        .error(R.drawable.ic_error_image_load)
                                        .into(license_front_iv)

                                    this@PersonalLicenseChangeInfoActivity.mLicenseFrontUrl = url
                                }

                            })
                        } else {
                            Glide.with(this)
                                .load(imgUrl)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(license_front_iv)
                            this@PersonalLicenseChangeInfoActivity.mLicenseFrontUrl = imgUrl
                        }

                    }
                }

                Constants.I4 -> {
                    if (!imgUrl.isNullOrBlank()) {
                        if (ProductUtils.isOssSignUrl(imgUrl)) {
                            ProductUtils.getRealOssUrl(this, imgUrl, object : CloudAccountApplication.OssSignCallBack {
                                override fun ossUrlSignEnd(url: String) {
                                    Glide.with(this@PersonalLicenseChangeInfoActivity)
                                        .load(url)
                                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                        .error(R.drawable.ic_error_image_load)
                                        .into(license_back_iv)
                                    this@PersonalLicenseChangeInfoActivity.mLicenseBackUrl = url
                                }

                            })
                        } else {
                            Glide.with(this)
                                .load(imgUrl)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(license_back_iv)
                            this@PersonalLicenseChangeInfoActivity.mLicenseBackUrl = imgUrl
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
        R.id.license_back_iv

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


            else -> {
            }
        }
    }


}