package com.fatcloud.account.feature.forms.enterprise.license

import android.content.Intent
import android.util.Log
import android.view.View
import butterknife.OnClick
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.IDCardParams
import com.baidu.ocr.sdk.model.IDCardResult
import com.baidu.ocr.ui.camera.CameraActivity
import com.baidu.ocr.ui.util.FileUtil
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.VibrateUtils
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.event.entity.ImageUploadEvent
import com.fatcloud.account.event.entity.OrderPaySuccessEvent
import com.fatcloud.account.feature.defray.prepare.PayPrepareActivity
import com.fatcloud.account.feature.matisse.Matisse
import com.fatcloud.account.feature.ocr.RecognizeIDCardResultCallBack
import com.fatcloud.account.view.CompanyMemberEditView
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_license_enterprise.*
import java.io.File

/**
 * Created by Wangsw on 2020/6/10 0010 18:30.
 * </br>
 *  企业套餐表单
 */
class FormLicenseEnterpriseActivity : BaseMVPActivity<FormLicenseEnterprisePresenter>(),
    FormLicenseEnterpriseView {


    /**
     * 用户选中的一级经营范围pid
     */
    private var mSelectPid = ArrayList<String>()

    /**
     * 用户选中的一级经营范围pid名称
     */
    private var mSelectPidNames = ArrayList<String>()

    /**
     * 年收入
     */
    private var mIncomeMoney: String = ""


    /**
     * 最终收入
     */
    private var mFinalMoney: String = ""


    /**
     * 选中的产品价格id
     */
    private var mProductPriceId: String = "0"

    /**
     * 产品id
     */
    private var mProductId: String = "0"


    /**
     * 用户选中的位置信息
     */
    private var mAreaName: String = ""
    private var mZeroName: String = ""
    private var mFirstName: String = ""
    private var mSecondName: String = ""
    private var mInvestmentYear: String = ""
    private var mInvestMoney: String = ""
    private var mBankNumber: String = ""
    private var mBankPhone: String = ""
    private var mDetailAddress: String = ""

    var isFaceUp = false


    override fun getLayoutId() = R.layout.activity_form_license_enterprise

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

        intent.extras!!.getString(Constants.PARAM_INCOME_MONEY, "0")?.let {
            mIncomeMoney = it
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

        intent.extras!!.getStringArrayList(Constants.PARAM_SELECT_BUSINESS_SCOPE_PID)?.let {
            mSelectPid.clear()
            mSelectPid = it
        }

        intent.extras!!.getStringArrayList(Constants.PARAM_SELECT_BUSINESS_SCOPE_NAME)?.let {
            mSelectPidNames.clear()
            mSelectPidNames = it
        }

        intent.extras!!.getString(Constants.PARAM_SELECT_AREA_NAME)?.let {
            mAreaName = it
        }

        intent.extras!!.getString(Constants.PARAM_ZERO_NAME)?.let {
            mZeroName = it
        }
        intent.extras!!.getString(Constants.PARAM_FIRST_NAME)?.let {
            mFirstName = it
        }
        intent.extras!!.getString(Constants.PARAM_SECOND_NAME)?.let {
            mSecondName = it
        }
        intent.extras!!.getString(Constants.PARAM_INVEST_YEAR)?.let {
            mInvestmentYear = it
        }
        intent.extras!!.getString(Constants.PARAM_INVEST_MONEY)?.let {
            mInvestMoney = it
        }
        intent.extras!!.getString(Constants.PARAM_BANK_NUMBER)?.let {
            mBankNumber = it
        }
        intent.extras!!.getString(Constants.PARAM_BANK_PHONE)?.let {
            mBankPhone = it
        }
        intent.extras!!.getString(Constants.PARAM_DETAIL_ADDRESS)?.let {
            mDetailAddress = it
        }


    }


    private fun initEvent() {
        // 图片上传成功
        presenter.subsribeEventEntity<ImageUploadEvent>(Consumer {

            if (it.formWhichClass != this.javaClass) {
                return@Consumer
            }
            val finalUrl = it.finalUrl
            val fromView = findViewById<CompanyMemberEditView>(it.fromViewId)
            if (fromView != null) {
                fromView.setImageUrl(finalUrl)
            }
        })

        presenter.subsribeEventEntity<OrderPaySuccessEvent>(Consumer {
            finish()
        })
    }


    private fun initView() {
        setMainTitle("注册信息")


        // 法人信息
        legal_person_ev.apply {
            currentMold = Constants.SH1
            initHighlightTitle(getString(R.string.legal_person_info))
            initNameTitle(getString(R.string.legal_person_name))

            initIdAddressHint("请输入法人身份证地址")
            initPhoneHint("请输入法人联系电话")
            initShareRatioHint(getString(R.string.share_ratio_hint))

        }

        // 监事信息
        supervisor_ev.apply {
            currentMold = Constants.SH2
            initHighlightTitle(getString(R.string.supervisor_info))
            initNameTitle(getString(R.string.supervisor_name))

            initIdAddressHint("请输入监事身份证地址")
            initPhoneHint("请输入监事联系电话")
            initShareRatioHint(getString(R.string.share_ratio_hint_2))
        }

        // 默认股东信息
        shareholder_ev.apply {
            currentMold = Constants.SH3
            initHighlightTitle(getString(R.string.shareholder_info2))
            initNameTitle(getString(R.string.shareholder_name))

            initIdAddressHint("请输入股东身份证地址")
            initPhoneHint("请输入股东联系电话")
            initShareRatioHint(getString(R.string.share_ratio_hint))
            showAddActionView().setOnClickListener {
                VibrateUtils.vibrate(10)
                // it.visibility = View.GONE
                shareholder_more_container.addView(
                    presenter.getShareholderView(
                        0,
                        this@FormLicenseEnterpriseActivity,
                        shareholder_more_container
                    ), 0
                )
                scroll_nsv.smoothScrollTo(0, ScreenUtils.getScreenHeight())

            }

        }

    }


    @OnClick(
        R.id.bottom_left_tv,
        R.id.bottom_right_tv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {

            R.id.bottom_left_tv -> {
                // 保存
            }

            R.id.bottom_right_tv -> {
                presenter.handlePost(
                    this,
                    legalPersonView = legal_person_ev,
                    supervisorView = supervisor_ev,
                    shareholderView = shareholder_ev,
                    detailAddress = mDetailAddress,
                    areaName = mAreaName,
                    bankNumber = mBankNumber,
                    bankPhone = mBankPhone,
                    selectPid = mSelectPid,
                    zeroName = mZeroName,
                    firstName = mFirstName,
                    secondName = mSecondName,
                    incomeMoney = mIncomeMoney,
                    investMoney = mInvestMoney,
                    investmentYear = mInvestmentYear,
                    finalMoney = mFinalMoney,
                    productId = mProductId,
                    productPriceId = mProductPriceId,
                    shareholderMoreContainer = shareholder_more_container
                )
            }

            else -> {
            }
        }
    }


    override fun addEnterpriseSuccess(preparePay: PreparePay) {
        ToastUtils.showShort("套餐添加成功")
        startActivity(
            Intent(this, PayPrepareActivity::class.java)
                .putExtra(Constants.PARAM_ORDER_ID, preparePay.orderId)
                .putExtra(Constants.PARAM_ORDER_NUMBER, preparePay.orderNo)
                .putExtra(
                    Constants.PARAM_MONEY,
                    preparePay.money.stripTrailingZeros().toPlainString()
                ) // 使用接口返回的最终支付金额
                .putExtra(Constants.PARAM_IMAGE_URL, preparePay.productLogoImgUrl)
                .putExtra(Constants.PARAM_PRODUCT_NAME, preparePay.productName)
                .putExtra(Constants.PARAM_DATE, preparePay.createDt)
        )
        finish()
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
                        val fromView = findViewById<CompanyMemberEditView>(fromViewId)
                        if (fromView != null) {
                            fromView.loadResultImage(fileDirPath)
                        }
                    }
                    val application = application as CloudAccountApplication
                    application.getOssSecurityToken(
                        true,
                        isFaceUp,
                        fileDirPath,
                        fromViewId,
                        this@FormLicenseEnterpriseActivity.javaClass
                    )
                }
            }

            Constants.REQUEST_CODE_CAMERA -> receiveOcrCamera(data)
            else -> {
            }
        }
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
            isFaceUp,
            filePath,
            fromViewId,
            this@FormLicenseEnterpriseActivity.javaClass
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
                                result.address?.let {
                                    fromView.setIdAddressValue(it.words, true)
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


}