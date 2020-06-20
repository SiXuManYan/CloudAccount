package com.fatcloud.account.feature.order.details.enterprise.company

import android.view.View
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
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo
import com.fatcloud.account.entity.order.persional.PersonalInfo
import com.fatcloud.account.extend.RoundTransFormation
import com.fatcloud.account.view.ShareholderView
import kotlinx.android.synthetic.main.activity_order_detail_company.*
import kotlinx.android.synthetic.main.layout_bank_info.*
import kotlinx.android.synthetic.main.layout_company_info.*
import java.net.URL

/**
 * Created by Wangsw on 2020/6/4 0004 14:03.
 * </br>
 *  公司信息 ,银行信息
 */
class CompanyRegisterInfoActivity : BaseMVPActivity<CompanyRegisterInfoPresenter>(),
    CompanyRegisterInfoView {

    /**
     * 订单流程id
     */
    var orderWorkId: String? = ""

    /**
     * 产品流程类型
     * PW1 营业执照办理
     * PW3 银行账户办理
     */
    private var productWorkType: String? = ""

    /**
     * 订单id，
     * 当为 产品流程类型为：PW3银行账户办理时，tOrderWork/detail 接口不返回相关法人信息
     * 需要使用订单id调用  tOrder/detail 接口请求法人股东信息
     *
     */
    private var orderId: String? = ""


    override fun getLayoutId() = R.layout.activity_order_detail_company


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun initViews() {
        initExtra()
        setMainTitle(
            title = when (productWorkType) {
                "PW1" -> {
                    "公司信息"
                }
                else -> {
                    "开立银行对公账户"
                }
            }
        )
        presenter.getEnterpriseInfo(this, orderWorkId, productWorkType, orderId)
    }

    private fun initExtra() {

        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_WORK_ID)) {
            finish()
            return
        }
        orderWorkId = intent.extras!!.getString(Constants.PARAM_ORDER_WORK_ID)
        productWorkType = intent.extras!!.getString(Constants.PARAM_PRODUCT_WORK_TYPE)
        orderId = intent.extras!!.getString(Constants.PARAM_ORDER_ID)
    }


    override fun bindDetailInfo(data: EnterpriseInfo) {
        CommonUtils.setPaymentStatus(data.state, payment_status_iv, payment_status_tv)


        when (productWorkType) {
            "PW1" -> {
                company_info.visibility = View.VISIBLE
                setCompanyInfo(data)
            }

            "PW3" -> {
                bank_info.visibility = View.VISIBLE
                setBankInfo(data)
            }

        }
    }


    private fun setCompanyInfo(data: EnterpriseInfo) {
        zero_company_name_tv.text = data.enterpriseName0
        first_company_name_tv.text = data.enterpriseName1
        second_company_name_tv.text = data.enterpriseName2
        invest_year_num_tv.text = data.investYearNum
        amount_of_funds_tv.text = data.investMoney?.stripTrailingZeros()?.toPlainString()
        business_scope_tv.text = data.businessScopeNames
        bank_card_number_tv.text = data.bankNo
        bank_phone_tv.text = data.bankPhone
        addr_tv.text = data.addr
        shareholder_container_ll.removeAllViews()
        data.shareholders?.forEach {
            shareholder_container_ll.addView(ShareholderView(this).apply { setShareHolderView(it) })
        }
    }


    private fun setBankInfo(data: EnterpriseInfo) {

        bank_name_tv.text = data.financeName // 银行
        company_name_tv.text = data.enterpriseName
        company_address_tv.text = data.enterpriseAddr
        postcode_tv.text = data.capital // 邮编


        account_nature_tv.text = data.enterpriseMold

        // 对账
        reconciliation_name_tv.text = data.reconciliatContact
        reconciliation_phone_tv.text = data.reconciliatPhone
        reconciliation_address_tv.text = data.reconciliatAddr


        // 营业执照
        if (data.businessLicenseImgUrl.isNullOrBlank()) {
            business_license_ll.visibility = View.GONE
        } else {
            data.businessLicenseImgUrl?.let {
                if (ProductUtils.isOssSignUrl(it)) {
                    ProductUtils.getRealOssUrl(this, it, object : CloudAccountApplication.OssSignCallBack {
                        override fun ossUrlSignEnd(url: String) {

                            Glide.with(this@CompanyRegisterInfoActivity)
                                .load(url)
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

                    })
                } else {
                    Glide.with(this)
                        .load(it)
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
        }


        // 电子公章
        val electronicSealImgUrl = data.electronicSealImgUrl
        if (!electronicSealImgUrl.isNullOrBlank()) {
            if (ProductUtils.isOssSignUrl(electronicSealImgUrl)) {
                ProductUtils.getRealOssUrl(this, electronicSealImgUrl, object : CloudAccountApplication.OssSignCallBack {
                    override fun ossUrlSignEnd(url: String) {
                        Glide.with(this@CompanyRegisterInfoActivity)
                            .load(url)
                            .apply(
                                RequestOptions().transform(
                                    MultiTransformation(
                                        CenterCrop(),
                                        RoundTransFormation(context, 4)
                                    )
                                )
                            )
                            .error(R.drawable.ic_error_image_load)
                            .into(electronic_seal_iv)
                    }

                })
            } else {
                Glide.with(this)
                    .load(electronicSealImgUrl)
                    .apply(
                        RequestOptions().transform(
                            MultiTransformation(
                                CenterCrop(),
                                RoundTransFormation(context, 4)
                            )
                        )
                    )
                    .error(R.drawable.ic_error_image_load)
                    .into(electronic_seal_iv)
            }

        } else {
            electronic_seal_ll.visibility = View.GONE
        }


        // 法人签字授权书
        val legalPersonWarrantImgUrl = data.legalPersonWarrantImgUrl
        if (!legalPersonWarrantImgUrl.isNullOrBlank()) {
            if (ProductUtils.isOssSignUrl(legalPersonWarrantImgUrl)) {

                ProductUtils.getRealOssUrl(this, legalPersonWarrantImgUrl, object : CloudAccountApplication.OssSignCallBack {
                    override fun ossUrlSignEnd(url: String) {

                        Glide.with(this@CompanyRegisterInfoActivity)
                            .load(url)
                            .apply(
                                RequestOptions().transform(
                                    MultiTransformation(
                                        CenterCrop(),
                                        RoundTransFormation(context, 4)
                                    )
                                )
                            )
                            .error(R.drawable.ic_error_image_load)
                            .into(Legal_signature_authorization_iv)


                    }

                })


            } else {
                Glide.with(this)
                    .load(legalPersonWarrantImgUrl)
                    .apply(
                        RequestOptions().transform(
                            MultiTransformation(
                                CenterCrop(),
                                RoundTransFormation(context, 4)
                            )
                        )
                    )
                    .error(R.drawable.ic_error_image_load)
                    .into(Legal_signature_authorization_iv)
            }


        } else {
            Legal_signature_authorization_ll.visibility = View.GONE
        }


    }


    override fun bindShareholdersInfo(data: PersonalInfo) {
        registered_capital_tv.text = data.investMoney?.stripTrailingZeros()?.toPlainString()  // 注册资本。。。。。
        shareholder_container_bank_ll.removeAllViews()
        data.shareholders?.forEach {
            shareholder_container_bank_ll.addView(ShareholderView(this).apply { setShareHolderView(it) })
        }
    }


}