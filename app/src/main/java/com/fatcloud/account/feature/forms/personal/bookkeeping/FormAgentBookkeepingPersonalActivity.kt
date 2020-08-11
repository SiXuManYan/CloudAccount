package com.fatcloud.account.feature.forms.personal.bookkeeping

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import butterknife.OnClick
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.data.CloudDataBase
import com.fatcloud.account.entity.local.form.PersonalBookkeepingDraft
import com.fatcloud.account.entity.product.NativeBookkeeping
import com.fatcloud.account.entity.users.User
import com.fatcloud.account.event.entity.ImageUploadEvent
import com.fatcloud.account.event.entity.OrderPaySuccessEvent
import com.fatcloud.account.feature.forms.personal.bookkeeping.signature.SignatureActivity
import com.fatcloud.account.feature.matisse.Matisse
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_agent_bookkeeping_personal.*
import kotlinx.android.synthetic.main.layout_image_upload_single.*
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/13 0013 15:40.
 * </br>
 * 个体户代理记账
 */
class FormAgentBookkeepingPersonalActivity : BaseMVPActivity<FormAgentBookkeepingPersonalPresenter>(), FormAgentBookkeepingPersonalView {


    lateinit var database: CloudDataBase @Inject set

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
     * 图片所在本地路径
     */
    private var mBusinessLicenseImgFilePath: String = ""

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

        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_PRODUCT_ID) || !intent.extras!!.containsKey(Constants.PARAM_PRODUCT_PRICE_ID)) {
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
        ProductUtils.onlySupportChineseInput(legal_name_et, store_name_et)
        restoreDraft()
    }

    private fun restoreDraft() {
        val draft = PersonalBookkeepingDraft.get()
        if (draft.loginPhone != User.get().username || draft.productId.isNullOrBlank() || draft.productId != mProductId) {
            return
        }

        if (mFinalMoney.isBlank()) {
            draft.finalMoney?.let {
                mFinalMoney = it
            }
        }


        draft.legalPersonName?.let {
            legal_name_et.setText(it)
        }
        draft.legalPersonPhone?.let {
            legal_phone_et.setText(it)
        }
        draft.idNumber?.let {
            id_number_et.setText(it)
        }
        draft.businessLicenseName?.let {
            store_name_et.setText(it)
        }
        draft.businessLicenseImgUrl?.let {
            mBusinessLicenseImgUrl = it
        }
        draft.businessLicenseImgFilePath?.let {
            Glide.with(this).load(it)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        // 图片路径发生改变或不存在，导致加载本地文件路径失败，清空已上传连接，让用户重新上传
                        mBusinessLicenseImgUrl = ""
                        id_card_front_iv.setImageResource(R.drawable.ic_upload_default)
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean = false
                })
                .into(id_card_front_iv)
        }


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
                    mBusinessLicenseImgFilePath = fileDirPath
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
        R.id.id_card_front_iv,
        R.id.bottom_left_tv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.commit_tv -> {
                handleCommit()
            }
            R.id.id_card_front_iv -> {
                ProductUtils.handleMediaSelect(this, Matisse.IMG, view.id)
            }
            R.id.bottom_left_tv -> {

                saveDraft()
            }
            else -> {
            }
        }
    }

    private fun saveDraft() {

        val draft = PersonalBookkeepingDraft().apply {
            loginPhone = User.get().username
            finalMoney = mFinalMoney
            productId = mProductId
            productPriceId = mProductPriceId
            legalPersonName = legal_name_et.text.toString().trim()
            legalPersonPhone = legal_phone_et.text.toString().trim()
            idNumber = id_number_et.text.toString().trim()
            businessLicenseName = store_name_et.text.toString().trim()
            businessLicenseImgUrl = mBusinessLicenseImgUrl
            businessLicenseImgFilePath = mBusinessLicenseImgFilePath
        }
        database.personalBookkeepingDraftDao().add(draft)
        PersonalBookkeepingDraft.update()
        ToastUtils.showShort(R.string.save_success)

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