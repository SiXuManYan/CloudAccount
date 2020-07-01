package com.fatcloud.account.feature.forms.personal.bookkeeping

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import butterknife.OnClick
import com.blankj.utilcode.util.ToastUtils
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.product.NativeBookkeeping
import com.fatcloud.account.event.entity.ImageUploadEvent
import com.fatcloud.account.event.entity.OrderPaySuccessEvent
import com.fatcloud.account.feature.forms.personal.bookkeeping.signature.SignatureActivity
import com.fatcloud.account.feature.matisse.Matisse
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_agent_bookkeeping_personal.*

/**
 * Created by Wangsw on 2020/6/13 0013 15:40.
 * </br>
 * 个体户代理记账
 */
class FormAgentBookkeepingPersonalActivity :
    BaseMVPActivity<FormAgentBookkeepingPersonalPresenter>(), FormAgentBookkeepingPersonalView {

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

    /**
     * 证件正面
     */
    var isFaceUp = false


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
        // 图片上传成功
        presenter.subsribeEventEntity<ImageUploadEvent>(Consumer {

            if (it.formWhichClass != this.javaClass) {
                return@Consumer
            }
            mBusinessLicenseImgUrl = it.finalUrl
        })

        presenter.subsribeEventEntity<OrderPaySuccessEvent>(Consumer {
            finish()
        })
        presenter.subsribeEvent(Consumer {
            when (it.code) {
                Constants.EVENT_FORM_COMMIT_SUCCESS -> {
                    finish()
                }
                else -> {
                }
            }
        })

    }

    private fun initView() {
        setMainTitle("法人信息")

    }


    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null) {
            return
        }

        when (requestCode) {

            Constants.REQUEST_MEDIA -> {
                // 相册选择图片
                val elements = Matisse.obtainPathResult(data)
                if (elements.isNotEmpty()) {
                    val fileDirPath = elements[0]
                    val fromViewId = data.getIntExtra(Matisse.MEDIA_FROM_VIEW_ID, 0)
                    if (fromViewId != 0) {
                        val fromView = findViewById<ImageView>(fromViewId)
                        if (fromView != null) {
                            Glide.with(this).load(fileDirPath).into(fromView)

                        }
                    }
                    val application = application as CloudAccountApplication
                    application.getOssSecurityToken(
                        true,
                        isFaceUp,
                        fileDirPath,
                        fromViewId,
                        this@FormAgentBookkeepingPersonalActivity.javaClass
                    )
                }
            }
            else -> {
            }
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
                // 去签字页
                handleCommit()
            }
            R.id.id_card_front_iv -> {
                ProductUtils.handleMediaSelect(this, Matisse.IMG, view.id)
            }
            else -> {
            }
        }
    }


    private fun handleCommit() {

        val nameValue = legal_name_et.text.toString().trim()
        if (nameValue.isBlank()) {
            ToastUtils.showShort("请输入法人姓名")
            return
        }

        val phoneValue = legal_phone_et.text.toString().trim()
        if (phoneValue.isBlank()) {
            ToastUtils.showShort("请输入法人联系电话")
            return
        }
        if (!ProductUtils.isPhoneNumber(phoneValue, "法人")) {
            return
        }


        val idNumberValue = id_number_et.text.toString().trim()
        if (idNumberValue.isBlank()) {
            ToastUtils.showShort("请输入法人身份证号")
            return
        }
        if (!ProductUtils.isIdCardNumber(idNumberValue, "法人")) {
            return
        }

        val storeNameValue = store_name_et.text.toString().trim()
        if (storeNameValue.isBlank()) {
            ToastUtils.showShort("请输入营业执照名称")
            return
        }

        if (mBusinessLicenseImgUrl.isNullOrBlank()) {
            ToastUtils.showShort("请上传营业执照副本")
            return
        }

        startActivity(SignatureActivity::class.java,
            Bundle().apply {
                this.putSerializable(Constants.PARAM_DATA, NativeBookkeeping().apply {
                    finalMoney = mFinalMoney
                    productId = mProductId
                    productPriceId = mProductPriceId
                    legalPersonName = nameValue
                    phone = phoneValue
                    idNumber = idNumberValue
                    storeName = storeNameValue
                    businessLicenseImgUrl = mBusinessLicenseImgUrl
                })
            })


    }


}