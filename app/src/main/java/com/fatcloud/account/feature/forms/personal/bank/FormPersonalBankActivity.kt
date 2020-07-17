package com.fatcloud.account.feature.forms.personal.bank

import android.content.Intent
import android.view.View
import android.widget.ImageView
import butterknife.OnClick
import com.baidu.ocr.ui.camera.CameraActivity
import com.baidu.ocr.ui.util.FileUtil
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.persional.NamePhoneBean
import com.fatcloud.account.entity.order.persional.BankPersonal
import com.fatcloud.account.event.entity.ImageUploadEvent
import com.fatcloud.account.event.entity.OrderPaySuccessEvent
import com.fatcloud.account.feature.defray.prepare.PayPrepareActivity
import com.fatcloud.account.feature.matisse.Matisse
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_bank_personal.*

/**
 * Created by Wangsw on 2020/7/16 0016 11:04.
 * </br>
 * 个体户银行对公账户
 */
class FormPersonalBankActivity : BaseMVPActivity<FormPersonalBankPresenter>(), FormPersonalBankView {


    /**
     * 产品id
     */
    private var mProductId: String = "0"

    /**
     * 最终需支付金额
     */
    private var mFinalMoney: String = ""

    /**
     * 选中的产品价格id
     */
    private var mProductPriceId: String = "0"

    /** 存款人姓名 */
    private var mDepositorNameValue: String = ""

    /** 纳税人识别号 */
    private var mTaxpayerNumberValue: String = ""

    /** 注册地址 */
    private var mRegisteredAddressValue: String = ""

    /** 账户类型 */
    private var mAccountNatureValue: String = ""

    /** 邮寄地址 */
    private var mMailingAddressValue: String = ""

    /** 详细地址 */
    private var mailingDetailAddressValue: String = ""


    /**
     * 营业执照图片地址
     */
    private var mLicenseImgUrl: String = ""

    /**
     * 营业执照本地路径
     */
    private var mLicensePath: String = ""


    /**
     * 基本存款账户信息 图片地址
     */
    private var accountInfoUrl: String = ""

    /**
     * 基本存款账户信息 本地路径
     */
    private var accountInfoPath: String = ""


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId() = R.layout.activity_form_bank_personal

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

        intent.extras!!.getString(Constants.PARAM_PRODUCT_ID)?.let {
            mProductId = it
        }

        intent.extras!!.getString(Constants.PARAM_FINAL_MONEY)?.let {
            mFinalMoney = it
        }

        intent.extras!!.getString(Constants.PARAM_PRODUCT_PRICE_ID)?.let {
            mProductPriceId = it
        }

        intent.extras!!.getString(Constants.PARAM_NAME)?.let {
            mDepositorNameValue = it
        }

        intent.extras!!.getString(Constants.PARAM_TAXPAYER_NUMBER)?.let {
            mTaxpayerNumberValue = it
        }

        intent.extras!!.getString(Constants.PARAM_REGISTERED_ADDRESS)?.let {
            mRegisteredAddressValue = it
        }

        intent.extras!!.getString(Constants.PARAM_ACCOUNT_NATURE)?.let {
            mAccountNatureValue = it
        }

        intent.extras!!.getString(Constants.PARAM_MAILING_ADDRESS)?.let {
            mMailingAddressValue = it
        }

        intent.extras!!.getString(Constants.PARAM_MAILING_DETAIL_ADDRESS)?.let {
            mailingDetailAddressValue = it
        }


    }


    private fun initEvent() {
        // 图片上传成功
        presenter.subsribeEventEntity<ImageUploadEvent>(Consumer {
            if (it.formWhichClass != this.javaClass) {
                return@Consumer
            }
            val finalUrl = it.finalUrl

            when (it.fromViewId) {

                R.id.license_iv -> {
                    mLicenseImgUrl = finalUrl
                }
                R.id.account_info_iv -> {
                    accountInfoUrl = finalUrl
                }
                R.id.legal_person_view -> {
                    legal_person_view.setImageUrl(finalUrl)
                }
                else -> {
                }
            }
        })

        presenter.subsribeEventEntity<OrderPaySuccessEvent>(Consumer {
            finish()
        })

        presenter.subsribeEvent(Consumer {
            when (it.code) {
                Constants.EVENT_FORM_CLOSE,
                Constants.EVENT_CLOSE_PAY_UNKNOWN -> {
                    finish()
                }
                else -> {
                }
            }
        })

    }


    private fun initView() {
        setMainTitle("个体户银行对公账户")
        legal_person_view.apply {
            currentMold = Constants.SH1
            initHighlightTitle("法人信息")
            initNameTitle("姓名")
            initPhoneHint("联系方式")
            showGenderView(false)
            showNation(false)
            showIdNumber(false)
            showIdExpirationDate(false)
            hideAddress()
            hideShareRatio()
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

                    val fromViewId = data.getIntExtra(Matisse.MEDIA_FROM_VIEW_ID, 0)

                    if (fromViewId != 0) {
                        val fromView = findViewById<ImageView>(fromViewId)
                        if (fromView != null) {
                            Glide.with(this).load(fileDirPath).diskCacheStrategy(DiskCacheStrategy.NONE).into(fromView)
                        }
                        when (fromViewId) {
                            R.id.license_iv -> {
                                mLicensePath = fileDirPath
                            }
                            R.id.account_info_iv -> {
                                accountInfoPath = fileDirPath
                            }
                            else -> {
                            }
                        }

                    }
                    val application = application as CloudAccountApplication
                    application.getOssSecurityToken(
                        true,
                        true,
                        fileDirPath,
                        fromViewId,
                        this@FormPersonalBankActivity.javaClass
                    )
                }
            }


            Constants.REQUEST_CODE_CAMERA -> receiveOcrCamera(data)

            else -> {
            }
        }
    }

    private fun receiveOcrCamera(data: Intent) {
        val contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE)
        val realPath = data.getStringExtra(CameraActivity.KEY_CROP_VIEW_IMAGE_REAL_PATH)
        val fromViewId = data.getIntExtra(CameraActivity.KEY_FROM_VIEW_ID, 0)
        val filePath: String = FileUtil.getSaveFile(applicationContext).absolutePath

        if (filePath.isEmpty() || fromViewId == 0) {
            return
        }

        when (fromViewId) {
            R.id.legal_person_view -> {
                legal_person_view.loadResultImage(filePath)
            }
            else -> {
            }
        }

        // 上传oss
        val application = application as CloudAccountApplication
        application.getOssSecurityToken(
            true,
            isFaceUp = true,
            localFilePatch = filePath,
            fromViewId = fromViewId,
            clx = this@FormPersonalBankActivity.javaClass
        )
    }


    @OnClick(
        R.id.bottom_right_tv,
        R.id.license_iv,
        R.id.account_info_iv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.bottom_right_tv -> {
                ProductUtils.handleDoubleClick(view)
                handleNext()
            }
            R.id.license_iv,
            R.id.account_info_iv -> {
                ProductUtils.handleMediaSelect(this, Matisse.IMG, view.id)
            }

            else -> {
            }
        }
    }

    private fun handleNext() {

        val legalFrontImageUrl = legal_person_view.frontImageUrl
        if (!ProductUtils.hasIdCardUrl(legalFrontImageUrl, true, "法人")) {
            return
        }
        val legalBackNameUrl = legal_person_view.backImageUrl
        if (!ProductUtils.hasIdCardUrl(legalBackNameUrl, false, "法人")) {
            return
        }

        val legalNameValue = legal_person_view.getNameValue()
        if (legalNameValue.isBlank()) {
            ToastUtils.showShort("请输入法人姓名")
            return
        }

        val legalPhoneValue = legal_person_view.getPhoneValue()
        if (legalPhoneValue.isBlank()) {
            ToastUtils.showShort("请输入法人联系方式")
            return
        }
        if (!ProductUtils.isPhoneNumber(legalPhoneValue, "法人")) {
            return
        }


        val financeNameValue = finance_name_et.text.toString().trim()
        if (financeNameValue.isBlank()) {
            ToastUtils.showShort("请输入财务负责人姓名")
            return
        }

        val financePhoneValue = finance_phone_et.text.toString().trim()
        if (financePhoneValue.isBlank()) {
            ToastUtils.showShort("请输入财务负责人联系方式")
            return
        }
        if (!ProductUtils.isPhoneNumber(financePhoneValue, "财务负责人")) {
            return
        }


        val verificationFirstNameValue = verification_first_name_et.text.toString().trim()
        if (verificationFirstNameValue.isBlank()) {
            ToastUtils.showShort("请输入大额业务查证联系人姓名1")
            return
        }
        val verificationFirstPhoneValue = verification_first_phone_et.text.toString().trim()
        if (verificationFirstPhoneValue.isBlank()) {
            ToastUtils.showShort("请输入大额业务查证联系人联系方式1")
            return
        }
        if (!ProductUtils.isPhoneNumber(verificationFirstPhoneValue, "查证联系人联系方式1")) {
            return
        }


        val verificationSecondNameValue = verification_second_name_et.text.toString().trim()
        if (verificationSecondNameValue.isBlank()) {
            ToastUtils.showShort("请输入大额业务查证联系人姓名2")
            return
        }
        val verificationSecondPhoneValue = verification_second_phone_et.text.toString().trim()
        if (verificationSecondPhoneValue.isBlank()) {
            ToastUtils.showShort("请输入大额业务查证联系人联系方式2")
            return
        }
        if (!ProductUtils.isPhoneNumber(verificationSecondPhoneValue, "查证联系人联系方式2")) {
            return
        }

        val reconciliationNameValue = reconciliation_name_et.text.toString().trim()
        if (reconciliationNameValue.isBlank()) {
            ToastUtils.showShort("请输入对账联系人姓名")
            return
        }

        val reconciliationPhoneValue = reconciliation_phone_et.text.toString().trim()
        if (reconciliationPhoneValue.isBlank()) {
            ToastUtils.showShort("请输入对账联系人联系方式")
            return
        }
        if (!ProductUtils.isPhoneNumber(reconciliationPhoneValue)) {
            return
        }

        val model = BankPersonal().apply {
            productId = mProductId
            productPriceId = mProductPriceId
            money = mFinalMoney
            depositorName = mDepositorNameValue

            enterpriseCode = mTaxpayerNumberValue
            addressRegistered = mRegisteredAddressValue
            accountType = mAccountNatureValue
            addressPost = mMailingAddressValue
            addressDetailed = mailingDetailAddressValue

            imgsIdno = ArrayList<IdentityImg>().apply {
                clear()
                add(IdentityImg(imgUrl = legalFrontImageUrl, mold = Constants.I1))
                add(IdentityImg(imgUrl = legalBackNameUrl, mold = Constants.I2))
            }
            imgsLicense = ArrayList<IdentityImg>().apply {
                clear()
                add(IdentityImg(imgUrl = mLicenseImgUrl, mold = Constants.I1))
            }
            imgsDepositAccount = ArrayList<IdentityImg>().apply {
                clear()
                add(IdentityImg(imgUrl = accountInfoUrl, mold = Constants.I1))
            }

            personLegal = NamePhoneBean().apply {
                name = legalNameValue
                phone = legalPhoneValue
            }

            personFinance = NamePhoneBean().apply {
                name = financeNameValue
                phone = financePhoneValue
            }

            personVerification1 = NamePhoneBean().apply {
                name = verificationFirstNameValue
                phone = verificationFirstPhoneValue
            }

            personVerification2 = NamePhoneBean().apply {
                name = verificationSecondNameValue
                phone = verificationSecondPhoneValue
            }
            personReconciliation = NamePhoneBean().apply {
                name = reconciliationNameValue
                phone = reconciliationPhoneValue
            }
        }

        presenter.addLicenseChangePersonal(this, model)

    }

    override fun commitSuccess(preparePay: PreparePay) {

        startActivity(
            Intent(this, PayPrepareActivity::class.java)
                .putExtra(Constants.PARAM_ORDER_ID, preparePay.orderId)
                .putExtra(Constants.PARAM_ORDER_NUMBER, preparePay.orderNo)
                .putExtra(Constants.PARAM_MONEY, preparePay.money.stripTrailingZeros().toPlainString())
                .putExtra(Constants.PARAM_IMAGE_URL, preparePay.productLogoImgUrl)
                .putExtra(Constants.PARAM_PRODUCT_NAME, preparePay.productName)
                .putExtra(Constants.PARAM_DATE, preparePay.createDt)
        )
        finish()
    }


}