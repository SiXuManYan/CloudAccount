package com.fatcloud.account.feature.forms.personal.bookkeeping

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.ImageView
import butterknife.OnClick
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
    }

    private fun initView() {
        setMainTitle("法人信息")
        legal_name.setTitleAndHint(R.string.legal_person_name, R.string.legal_person_name_hint)
        legal_phone.setTitleAndHint(R.string.contact_number, R.string.legal_person_phone_hint).setInputType(InputType.TYPE_CLASS_NUMBER)
        id_number.setTitleAndHint(R.string.legal_person_id_number, R.string.legal_person_id_number_hint)
        store_name.setTitleAndHint(R.string.store_name, R.string.store_name_hint)
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
                    application.getOssSecurityToken(true, isFaceUp, fileDirPath, fromViewId, this@FormAgentBookkeepingPersonalActivity.javaClass)
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

        if (!ProductUtils.checkEditEmptyWithVibrate(legal_name, legal_phone, id_number, store_name)) {
            return
        }

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