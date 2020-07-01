package com.fatcloud.account.feature.forms.enterprise.bank

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import butterknife.OnClick
import com.baidu.ocr.sdk.model.IDCardParams
import com.baidu.ocr.sdk.model.IDCardResult
import com.baidu.ocr.ui.camera.CameraActivity
import com.baidu.ocr.ui.util.FileUtil
import com.blankj.utilcode.util.ToastUtils
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo
import com.fatcloud.account.event.RxBus
import com.fatcloud.account.event.entity.BankFormCommitSuccessEvent
import com.fatcloud.account.event.entity.ImageUploadEvent
import com.fatcloud.account.feature.matisse.Matisse
import com.fatcloud.account.feature.ocr.RecognizeIDCardResultCallBack
import com.fatcloud.account.view.CompanyMemberEditView
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_bank.*

/**
 * Created by Wangsw on 2020/6/15 0015 15:09.
 * </br>
 * 银行对公账户表单
 */
class FormBankActivity : BaseMVPActivity<FormBankPresenter>(), FormBankView {


    /**
     * 订单流程id
     */
    var orderWorkId: String? = ""

    var companyName: String? = ""
    var companyAddress: String? = ""
    var registeredCapital: String? = ""
    var accountNatureValue: String? = ""
    var reconciliationName: String? = ""
    var reconciliationPhone: String? = ""
    var areaName: String? = ""
    var detailAddr: String? = ""

    /**
     * 营业执照url
     */
    var business_license_url = ""

    /**
     * 电子图章url
     */
    var electronic_seal_url = ""

    /**
     * 签字授权书url
     */
    var signed_authorization_url = ""


    override fun getLayoutId() = R.layout.activity_form_bank

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun initViews() {
        initExtra()
        initEvent()
        initView()
    }

    private fun initExtra() {
        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_WORK_ID)) {
            finish()
            return
        }


        orderWorkId = intent.extras!!.getString(Constants.PARAM_ORDER_WORK_ID)
        companyName = intent.extras!!.getString(Constants.PARAM_COMPANY_NAME)
        companyAddress = intent.extras!!.getString(Constants.PARAM_COMPANY_ADDRESS)
        registeredCapital = intent.extras!!.getString(Constants.PARAM_REGISTERED_CAPITAL)
        accountNatureValue = intent.extras!!.getString(Constants.PARAM_ACCOUNT_NATURE)
        reconciliationName = intent.extras!!.getString(Constants.PARAM_RECONCILIATION_NAME)
        reconciliationPhone = intent.extras!!.getString(Constants.PARAM_RECONCILIATION_PHONE)
        areaName = intent.extras!!.getString(Constants.PARAM_AREA_NAME)
        detailAddr = intent.extras!!.getString(Constants.PARAM_DETAIL_ADDRESS)

        presenter.getBankInfo(this, orderWorkId)


    }


    private fun initEvent() {

        // 图片上传成功
        presenter.subsribeEventEntity<ImageUploadEvent>(Consumer {

            if (it.formWhichClass != this.javaClass) {
                return@Consumer
            }
            val finalUrl = it.finalUrl

            val fromViewId = it.fromViewId

            val fromView = findViewById<View>(fromViewId)
            if (fromView == null) {
                return@Consumer
            }

            if (fromView is CompanyMemberEditView) {
                fromView.setImageUrl(finalUrl)

            } else {
                when (fromViewId) {
                    R.id.business_license_iv -> business_license_url = finalUrl
                    R.id.electronic_seal_iv -> electronic_seal_url = finalUrl
                    R.id.signed_authorization_iv -> signed_authorization_url = finalUrl
                    else -> {

                    }
                }
            }


        })

    }


    private fun initView() {
        setMainTitle("开立银行对公账户")

        // 财务负责人
        finance_ev.apply {
            currentMold = Constants.SH4_N
            initHighlightTitle("财务负责人信息")
            initNameTitle("财务负责人姓名")
            hideAddress()
            initPhoneHint("请输入联系电话")
            initShareRatioHint(getString(R.string.share_ratio_hint))
        }
    }

    override fun bindDetailInfo(data: EnterpriseInfo) {

        // 法人、股东相关信息
        shareholder_more_container.removeAllViews()
        data.shareholders?.forEachIndexed { index, shareholder ->
            shareholder_more_container.addView(
                presenter.getShareholderView(
                    this,
                    shareholder_more_container,
                    index,
                    shareholder
                )
            )
        }


    }


    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        when (requestCode) {
            Constants.REQUEST_MEDIA -> handleAlbumSelect(data)
            Constants.REQUEST_CODE_CAMERA -> receiveOcrCamera(data)

            else -> {
            }
        }


    }

    private fun handleAlbumSelect(data: Intent) {
        // 相册选择图片
        val elements = Matisse.obtainPathResult(data)
        if (elements.isNullOrEmpty()) {
            return
        }

        // 图片的真实路径地址
        val fileDirPath = elements[0]
        val fromViewId = data.getIntExtra(Matisse.MEDIA_FROM_VIEW_ID, 0)
        if (fromViewId == 0) {
            return
        }
        val fromView = findViewById<View>(fromViewId)
        if (fromView == null) {
            return
        }
        if (fromView is CompanyMemberEditView) {
            fromView.loadResultImage(fileDirPath)
        } else if (fromView is ImageView) {
            Glide.with(this).load(fileDirPath).into(fromView)
        }
        val application = application as CloudAccountApplication
        // isFaceUp 朝向随意，CompanyMemberEditView 会自己记录朝向
        application.getOssSecurityToken(
            isEncryptFile = true,
            isFaceUp = false,
            localFilePatch = fileDirPath,
            fromViewId = fromViewId,
            clx = this@FormBankActivity.javaClass
        )
    }


    /**
     *  ocr 识别 相机返回
     */
    private fun receiveOcrCamera(data: Intent) {

        val contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE)
        val fromViewId = data.getIntExtra(CameraActivity.KEY_FROM_VIEW_ID, 0)
        val filePath: String = FileUtil.getSaveFile(applicationContext).absolutePath

        if (filePath.isEmpty() || fromViewId == 0) {
            return
        }

        // OCR 操作来源
        val fromView = findViewById<CompanyMemberEditView>(fromViewId)
        if (fromView == null) {
            return
        }

        fromView.loadResultImage(filePath)

        // 上传oss
        val application = application as CloudAccountApplication
        application.getOssSecurityToken(
            true,
            true,
            filePath,
            fromViewId,
            this@FormBankActivity.javaClass
        )


        if (contentType.isNotEmpty()) {

            when (contentType) {
                CameraActivity.CONTENT_TYPE_ID_CARD_FRONT -> {
                    // 身份证正面
                    ProductUtils.recIDCard(this, IDCardParams.ID_CARD_SIDE_FRONT, filePath,
                        object : RecognizeIDCardResultCallBack {
                            override fun onResult(result: IDCardResult) {

                                result.name?.let {
                                    fromView.setNameValue(it.words, true)
                                }
                                result.idNumber?.let {
                                    fromView.setIdNumberValue(it.words, true)
                                }

                            }
                        })
                }
                CameraActivity.CONTENT_TYPE_ID_CARD_BACK -> {
                    // 身份证背面
                }
                else -> {
                }
            }


        }
    }


    @OnClick(
        R.id.business_license_iv,
        R.id.electronic_seal_iv,
        R.id.signed_authorization_iv,

        R.id.bottom_right_tv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {

            R.id.business_license_iv,
            R.id.electronic_seal_iv,
            R.id.signed_authorization_iv

            -> ProductUtils.handleMediaSelect(context as Activity, 1, view.id)
            R.id.bottom_right_tv -> handleCommit()

            else -> {
            }
        }
    }

    private fun handleCommit() {


        // 财务负责人
        if (!ProductUtils.hasIdCardUrl(finance_ev.frontImageUrl, true, "财务负责人")) {
            return
        }
        if (!ProductUtils.hasIdCardUrl(finance_ev.backImageUrl, false, "财务负责人")) {
            return
        }

        val nameValue = finance_ev.getNameValue()
        if (nameValue.isBlank()) {
            ToastUtils.showShort("请输入财务负责人姓名")
            return
        }
        val idNumberValue = finance_ev.getIdNumberValue()
        if (idNumberValue.isBlank()) {
            ToastUtils.showShort("请输入财务负责人身份证号")
            return
        }
        if (!ProductUtils.isIdCardNumber(idNumberValue, "财务负责人")) {
            return
        }

        val phoneValue = finance_ev.getPhoneValue()
        if (phoneValue.isBlank()) {
            ToastUtils.showShort("请输入财务负责人联系电话")
            return
        }
        if (!ProductUtils.isPhoneNumber(phoneValue, "法人")) {
            return
        }
        val shareRatioValue = finance_ev.getShareRatioValue()
        if (shareRatioValue.isBlank()) {
            ToastUtils.showShort("请输入财务负责人股份占比")
            return
        }

        if (business_license_url.isBlank()) {
            ToastUtils.showShort("请上传营业执照")
            return
        }
        if (electronic_seal_url.isBlank()) {
            ToastUtils.showShort("请上传电子公章")
            return
        }
        if (signed_authorization_url.isBlank()) {
            ToastUtils.showShort("请上传法人签字授权书")
            return
        }



        presenter.addSpecificProcessContent(
            lifecycleOwner = this,
            orderWorkId = orderWorkId,
            businessLicenseImgUrl = business_license_url,
            capital = registeredCapital,
            electronicSealImgUrl = electronic_seal_url,
            enterpriseAddr = companyAddress,
            enterpriseMold = accountNatureValue,
            enterpriseName = companyName,
            financeIdno = idNumberValue,
            financeIdnoImgUrlA = finance_ev.frontImageUrl,
            financeIdnoImgUrlB = finance_ev.backImageUrl,
            financeName = nameValue,
            financePhone = phoneValue,
            financeShares = shareRatioValue,
            legalPersonWarrantImgUrl = signed_authorization_url,
            reconciliatAddr = detailAddr,
            reconciliatArea = areaName,
            reconciliatContact = reconciliationName,
            reconciliatPhone = reconciliationPhone
        )
    }

    override fun addSuccess() {
        ToastUtils.showShort("添加成功")
        RxBus.post(BankFormCommitSuccessEvent())
        finish()
    }


}