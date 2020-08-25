package com.fatcloud.account.feature.order.details.personal.license.handle

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
import com.fatcloud.account.entity.order.detail.PersonalLicenseHandleDetail
import com.fatcloud.account.extend.RoundTransFormation
import kotlinx.android.synthetic.main.activity_order_detail_personal.*
import kotlinx.android.synthetic.main.layout_detail_personal.*
import kotlinx.android.synthetic.main.layout_detail_tax_registration.*

/**
 * Created by Wangsw on 2020/6/4 0004 14:03.
 * </br>
 * 个体户营业执照办理 P1回显
 */
class PersonalLicenseHandleInfoActivity : BaseMVPActivity<PersonalLicenseHandleInfoPresenter>(), PersonalLicenseHandleInfoView {


    private var orderId: String? = ""

    /**
     * 产品类型
     * PW1  营业执照办理
     * PW2  税务登记办理
     */
    private var productWorkType: String? = ""


    private var idCardFrontUrl: String = ""
    private var idCardBackUrl: String = ""
    private var licenseUrl: String = ""

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId() = R.layout.activity_order_detail_personal

    override fun initViews() {
        initExtra()
        setMainTitle(
            when (productWorkType) {
                Constants.PW1 -> {
                    "注册人信息"
                }
                else -> {
                    "办理信息"
                }
            }
        )
        presenter.getRegistrantInfo(this, orderId)
    }


    private fun initExtra() {

        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_ID)) {
            finish()
            return
        }
        orderId = intent.extras!!.getString(Constants.PARAM_ORDER_ID)
        productWorkType = intent.extras!!.getString(Constants.PARAM_PRODUCT_WORK_TYPE)
    }


    override fun bindDetailInfo(data: PersonalLicenseHandleDetail) {
        ProductUtils.setPaymentStatus(data.state, data.stateText, payment_status_iv, payment_status_tv)

        when (productWorkType) {
            Constants.PW1 -> {
                registrant_info.visibility = View.VISIBLE
                setRegistrantInfo(data)
            }
            else -> {
                tax_registration.visibility = View.VISIBLE
                setTaxRegistration(data)
            }
        }
    }


    /**
     * PW1 营业执照办理 ：注册人信息
     */
    private fun setRegistrantInfo(data: PersonalLicenseHandleDetail) {

//        sex_tv.text = if (data.gender == "1") {
//            "男"
//        } else {
//            "女"
//        }
//

        sex_tv.text = when (data.gender) {
            "1" -> {
                "男"
            }
            "2" -> {
                "女"
            }
            else -> {
                data.gender
            }
        }



        nation_tv.text = data.nation


        if (data.area == null) {
            data.area = ""
        }
        if (data.addr == null) {
            data.addr = ""
        }
        addr_tv.text = getString(R.string.two_format, data.area, data.addr)

        phone_tv.text = data.tel
        first_choice_name_tv.text = data.name0
        alternative_name_01_tv.text = data.name1
        alternative_name_02_tv.text = data.name2
        business_scope_tv.text = data.businessScopeNames
        employees_number_tv.text = data.employedNum.toString()
        employment_amount_tv.text = data.income
        form_tv.text = data.formName
        identity_number_tv.text = data.idno
        name_tv.text = data.realName

        if (data.bankNo.isNotBlank()) {
            license_bank_number_ll.visibility = View.VISIBLE
            license_bank_number_tv.text = data.bankNo
        } else {
            // 旧版P1表单没有银行卡号输入项
            license_bank_number_ll.visibility = View.GONE
        }

        if (data.bankPhone.isNotBlank()) {
            license_bank_phone_ll.visibility = View.VISIBLE
            license_bank_phone_tv.text = data.bankPhone
        } else {
            // 旧版P1表单没有银行预留手机号输入项
            license_bank_phone_ll.visibility = View.GONE
        }






        data.imgs?.forEachIndexed { index, identityImg ->


            val imgUrl = identityImg.imgUrl
            if (!imgUrl.isNullOrBlank()) {


                if (ProductUtils.isOssSignUrl(imgUrl)) {
                    ProductUtils.getRealOssUrl(context, imgUrl, object : CloudAccountApplication.OssSignCallBack {
                        override fun ossUrlSignEnd(url: String) {


                            Glide.with(this@PersonalLicenseHandleInfoActivity)
                                .load(url)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(
                                    if (index == 0) {
                                        id_card_front_iv
                                    } else {
                                        id_card_obverse_iv
                                    }
                                )

                            if (index == 0) {
                                this@PersonalLicenseHandleInfoActivity.idCardFrontUrl = url
                            } else {
                                this@PersonalLicenseHandleInfoActivity.idCardBackUrl = url
                            }

                        }

                    })


                } else {
                    Glide.with(this)
                        .load(imgUrl)
                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                        .error(R.drawable.ic_error_image_load)
                        .into(
                            if (index == 0) {
                                id_card_front_iv
                            } else {
                                id_card_obverse_iv
                            }
                        )
                    if (index == 0) {
                        this@PersonalLicenseHandleInfoActivity.idCardFrontUrl = imgUrl
                    } else {
                        this@PersonalLicenseHandleInfoActivity.idCardBackUrl = imgUrl
                    }

                }


            }


        }


    }

    /**
     * PW2  税务登记办理 办理信息
     */
    private fun setTaxRegistration(data: PersonalLicenseHandleDetail) {
        taxpayer_id_tv.text = data.taxpayerNo
        id_number_tv.text = data.idno
        bank_card_number_tv.text = data.bankNo
        bank_phone_tv.text = data.phoneOfBank
        legal_person_name_tv.text = data.legalPersonName

        val businessLicenseImgUrl = data.businessLicenseImgUrl
        if (!businessLicenseImgUrl.isNullOrBlank()) {

            if (ProductUtils.isOssSignUrl(businessLicenseImgUrl)) {
                ProductUtils.getRealOssUrl(this, businessLicenseImgUrl, object : CloudAccountApplication.OssSignCallBack {
                    override fun ossUrlSignEnd(url: String) {
                        Glide.with(this@PersonalLicenseHandleInfoActivity)
                            .load(url)
                            .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                            .error(R.drawable.ic_error_image_load)
                            .into(business_license_iv)
                        this@PersonalLicenseHandleInfoActivity.licenseUrl = url
                    }

                })
            } else {
                Glide.with(this)
                    .load(businessLicenseImgUrl)
                    .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                    .error(R.drawable.ic_error_image_load)
                    .into(business_license_iv)
                this@PersonalLicenseHandleInfoActivity.licenseUrl = businessLicenseImgUrl
            }
        }
    }


    @OnClick(
        R.id.id_card_front_iv,
        R.id.id_card_obverse_iv,
        R.id.business_license_iv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.id_card_front_iv -> {
                ProductUtils.lookGallery(this, idCardFrontUrl)
            }
            R.id.id_card_obverse_iv -> {
                ProductUtils.lookGallery(this, idCardBackUrl)
            }
            R.id.business_license_iv -> {
                ProductUtils.lookGallery(this, licenseUrl)
            }

            else -> {
            }
        }
    }


}