package com.fatcloud.account.feature.order.details.personal

import android.view.View
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.fatcloud.account.R
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.persional.PersonalInfo
import com.fatcloud.account.extend.RoundTransFormation
import kotlinx.android.synthetic.main.activity_order_detail_personal.*
import kotlinx.android.synthetic.main.layout_detail_personal.*
import kotlinx.android.synthetic.main.layout_detail_tax_registration.*

/**
 * Created by Wangsw on 2020/6/4 0004 14:03.
 * </br>
 *  个人业务订单详情 注册信息
 */
class RegistrantInfoActivity : BaseMVPActivity<RegistrantInfoPresenter>(), RegistrantInfoView {


    private var orderId: String? = ""

    /**
     * 产品类型
     * PW1  营业执照办理
     * PW2  税务登记办理
     */
    private var productType: String? = ""

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId() = R.layout.activity_order_detail_personal

    override fun initViews() {
        initExtra()
        setMainTitle(
            when (productType) {
                "PW1" -> {
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
        productType = intent.extras!!.getString(Constants.PARAM_PRODUCT_WORK_TYPE)
    }


    override fun bindDetailInfo(data: PersonalInfo) {
        setPaymentStatus(data.state)

        when (productType) {
            "PW1" -> {
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
     * 订单支付状态
     */
    private fun setPaymentStatus(payState: String) {
        when (payState) {
            "OS1" -> {
                payment_status_iv.setImageResource(R.drawable.ic_status_daizhifu)
                payment_status_tv.text = "待支付"
            }
            "OS2" -> {
                payment_status_iv.setImageResource(R.drawable.ic_status_dingdanshixiao)
                payment_status_tv.text = "取消订单"
            }
            "OS3" -> {
                payment_status_iv.setImageResource(R.drawable.ic_status_dingdanshixiao)
                payment_status_tv.text = "订单实效"
            }
            "OS4" -> {
                payment_status_iv.setImageResource(R.drawable.ic_status_zhifuzhong)
                payment_status_tv.text = "支付中"
            }
            "OS5" -> {
                payment_status_iv.setImageResource(R.drawable.ic_status_yibanjie)
                payment_status_tv.text = "已支付"
            }

            "OS6" -> {
                payment_status_iv.setImageResource(R.drawable.ic_status_banlizhong)
                payment_status_tv.text = "已受理"
            }
            "OS7" -> {
                payment_status_iv.setImageResource(R.drawable.ic_status_banlizhong)
                payment_status_tv.text = "办理中"
            }
            "OS8" -> {
                payment_status_iv.setImageResource(R.drawable.ic_status_yibanjie)
                payment_status_tv.text = "已办结"
            }
            else -> {
                payment_status_iv.visibility = View.INVISIBLE
                payment_status_tv.visibility = View.INVISIBLE
            }
        }
    }


    /**
     * PW1 营业执照办理 ：注册人信息
     */
    private fun setRegistrantInfo(data: PersonalInfo) {
        sex_tv.text = if (data.gender == "1") {
            "男"
        } else {
            "女"
        }
        nation_tv.text = data.nation
        addr_tv.text = data.area
        phone_tv.text = data.tel
        first_choice_name_tv.text = data.name0
        alternative_name_01_tv.text = data.name1
        alternative_name_02_tv.text = data.name2
        business_scope_tv.text = data.businessScopeNames
        employees_number_tv.text = data.employedNum.toString()
        employment_amount_tv.text = data.money
        form_tv.text = data.formName
        identity_number_tv.text = data.idno
        name_tv.text = data.realName


        data.imgs.forEachIndexed { index, identityImg ->

            if (index == 0) {
                Glide.with(this)
//                    .load(identityImg.imgUrl)
                    .load(CommonUtils.getTestUrl())
                    .apply(
                        RequestOptions().transform(
                            MultiTransformation(
                                CenterCrop(),
                                RoundTransFormation(context, 4)
                            )
                        )
                    )
                    .error(R.drawable.ic_error_image_load)
                    .into(id_card_front_iv)

            } else {
                Glide.with(this)
//                    .load(identityImg.imgUrl)
                    .load(CommonUtils.getTestUrl())
                    .apply(
                        RequestOptions().transform(
                            MultiTransformation(
                                CenterCrop(),
                                RoundTransFormation(context, 4)
                            )
                        )
                    )
                    .error(R.drawable.ic_error_image_load)
                    .into(id_card_obverse_iv)
            }


        }


    }

    /**
     * PW2  税务登记办理 办理信息
     */
    private fun setTaxRegistration(data: PersonalInfo) {
        taxpayer_id_tv.text = data.taxpayerNo
        id_number_tv.text = data.idno
        bank_card_number_tv.text = data.bankNo
        bank_phone_tv.text = data.phoneOfBank
        legal_person_name_tv.text = data.legalPersonName

        Glide.with(this)
//            .load(data.businessLicenseImgUrl)
            .load(CommonUtils.getTestUrl())
            .apply(
                RequestOptions().transform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundTransFormation(context, 4)
                    )
                )
            )
            .error(R.drawable.ic_error_image_load)
            .into(business_license_iv)

    }


}