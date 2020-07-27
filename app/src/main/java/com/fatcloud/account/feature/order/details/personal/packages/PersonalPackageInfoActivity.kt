package com.fatcloud.account.feature.order.details.personal.packages

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
import com.fatcloud.account.entity.order.detail.PersonalPackageDetail
import com.fatcloud.account.extend.RoundTransFormation
import kotlinx.android.synthetic.main.activity_personal_package_info.*

/**
 * Created by Wangsw on 2020/7/27 0027 13:56.
 * </br>
 *  P9 P10 回显信息页
 */
class PersonalPackageInfoActivity : BaseMVPActivity<PersonalPackageInfoPresenter>(), PersonalPackageInfoView{


    private var mOrderId: String? = ""

    private var mIdFrontUrl: String = ""
    private var mIdBackUrl: String = ""


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId() = R.layout.activity_personal_package_info


    override fun initViews() {

        initExtra()

        setMainTitle("企业基本信息")
    }
    private fun initExtra() {
        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_ID)) {
            finish()
            return
        }
        mOrderId = intent.extras!!.getString(Constants.PARAM_ORDER_ID)
        presenter.getDetailInfo(this, mOrderId)
    }

    override fun bindDetailInfo(data: PersonalPackageDetail) {
        CommonUtils.setPaymentStatus(data.state, payment_status_iv, payment_status_tv)
        name_tv.text = data.nickName
        id_number_tv.text = data.idno
        gender_tv.text = when (data.gender) {
            "1" -> {
                "男"
            }
            "2"->{
                "女"
            }
            else -> {
                data.gender
            }
        }

        nation_tv.text = data.nation
        addr_tv.text = data.addr
        phone_tv.text = data.tel
        zero_name_tv.text = data.name0
        first_name_tv.text = data.name1
        second_name_tv.text = data.name2
        business_scope_tv.text = data.businessScopeNames
        employed_number_tv.text = data.employedNum
        amount_of_funds_tv.text = data.capital
        formation_tv.text = data.formName
        bank_phone_tv.text = data.bankPhone
        bank_number_tv.text = data.bankNo


        data.imgs.forEachIndexed { _, identityImg ->

            val imgUrl = identityImg.imgUrl

            when (identityImg.mold) {
                Constants.I1 -> {

                    if (!imgUrl.isNullOrBlank()) {
                        if (ProductUtils.isOssSignUrl(imgUrl)) {
                            ProductUtils.getRealOssUrl(this, imgUrl, object : CloudAccountApplication.OssSignCallBack {
                                override fun ossUrlSignEnd(url: String) {
                                    Glide.with(this@PersonalPackageInfoActivity)
                                        .load(url)
                                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                        .error(R.drawable.ic_error_image_load)
                                        .into(id_card_front_iv)

                                    this@PersonalPackageInfoActivity.mIdFrontUrl = url
                                }

                            })
                        } else {
                            Glide.with(this)
                                .load(imgUrl)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(id_card_front_iv)
                            this@PersonalPackageInfoActivity.mIdFrontUrl = imgUrl
                        }

                    }
                }

                Constants.I2 -> {
                    if (!imgUrl.isNullOrBlank()) {
                        if (ProductUtils.isOssSignUrl(imgUrl)) {
                            ProductUtils.getRealOssUrl(this, imgUrl, object : CloudAccountApplication.OssSignCallBack {
                                override fun ossUrlSignEnd(url: String) {
                                    Glide.with(this@PersonalPackageInfoActivity)
                                        .load(url)
                                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                        .error(R.drawable.ic_error_image_load)
                                        .into(id_card_back_iv)
                                    this@PersonalPackageInfoActivity.mIdBackUrl = url
                                }

                            })
                        } else {
                            Glide.with(this)
                                .load(imgUrl)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(id_card_back_iv)
                            this@PersonalPackageInfoActivity.mIdBackUrl = imgUrl
                        }

                    }
                }
            }


        }


    }

    @OnClick(
        R.id.id_card_front_iv,
        R.id.id_card_back_iv

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
            else -> {
            }
        }
    }



}