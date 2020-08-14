package com.fatcloud.account.feature.forms.personal.logout

import android.content.Intent
import android.view.View
import android.widget.ImageView
import butterknife.OnClick
import com.baidu.ocr.sdk.model.IDCardParams
import com.baidu.ocr.sdk.model.IDCardResult
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
import com.fatcloud.account.entity.order.persional.PersonalLicenseLogout
import com.fatcloud.account.event.entity.ImageUploadEvent
import com.fatcloud.account.feature.defray.prepare.PayPrepareActivity
import com.fatcloud.account.feature.matisse.Matisse
import com.fatcloud.account.feature.ocr.RecognizeIDCardResultCallBack
import com.fatcloud.account.view.CompanyMemberEditView
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_license_logout_personal.*

/**
 * Created by Wangsw on 2020/7/13 0013 16:38.
 * </br>
 * 个体户营业执照注销
 */
class FormLicenseLogoutActivity : BaseMVPActivity<FormLicenseLogoutPresenter>(), FormLicenseLogoutView {

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

    /**
     * 选中的产品价格id
     */
    private var mIndex: Int = 0


    /**
     * 身份证正反面路径集合
     */
    var mIdImageUrlList: ArrayList<IdentityImg> = ArrayList()

    /**
     * 营业执照正反面路径集合
     */
    var mLicenseImagesUrlList: ArrayList<IdentityImg> = ArrayList()

    /**
     * 承诺书集合
     */
    var mCommitmentImagesUrlList: ArrayList<IdentityImg> = ArrayList()

//
//    private var idImageFrontPath: String = ""
//    private var idImageFrontUrl: String = ""
//    private var idImageBackPath: String = ""
//    private var idImageBackUrl: String = ""

    private var licenseImageFrontPath: String = ""
    private var licenseImageFrontUrl: String = ""
    private var licenseImageBackPath: String = ""
    private var licenseImageBackUrl: String = ""

    private var commitmentImagePath: String = ""
    private var commitmentImageUrl: String = ""

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId() = R.layout.activity_form_license_logout_personal

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
        intent.extras!!.getInt(Constants.PARAM_INDEX, 0)?.let {
            mIndex = it
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
                R.id.id_license_front_iv -> licenseImageFrontUrl = finalUrl
                R.id.id_license_back_iv -> licenseImageBackUrl = finalUrl
                R.id.commitment_upload_iv -> commitmentImageUrl = finalUrl
                R.id.legal_person_view -> legal_person_view.setImageUrl(finalUrl)
                else -> {
                }
            }

        })
    }


    private fun initView() {
        setMainTitle("办理信息")
        if (mIndex == 0) {
            // 选择第一项 （营业执照注销时，才显示承诺书）
            commitment_container_ll.visibility = View.VISIBLE
        } else {
            commitment_container_ll.visibility = View.GONE
        }


        legal_person_view.apply {
            currentMold = Constants.SH1
            initHighlightTitle("上传白底身份证照片")
            initNameTitle("姓名")
            initPhoneHint("请输入法人联系方式")
            showIdNumber(true)
            showGenderView(false)
            showNation(false)
            showIdExpirationDate(false)
            hideAddress()
            hideShareRatio()
        }

    }


    @OnClick(
        R.id.commit_tv,
        R.id.id_license_front_iv,
        R.id.id_license_back_iv,
        R.id.commitment_upload_iv,
        R.id.commitment_download_iv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {

            R.id.commit_tv -> {
                ProductUtils.handleDoubleClick(view)
                handleCommit()
            }
            R.id.id_license_front_iv,
            R.id.id_license_back_iv,
            R.id.commitment_upload_iv -> ProductUtils.handleMediaSelect(this, Matisse.IMG, view.id)
            R.id.commitment_download_iv -> {
                presenter.downLoadImage(this)
            }

            else -> {

            }
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
                    if (fromViewId == 0) {
                        return
                    }

                    when (fromViewId) {
                        R.id.id_license_front_iv -> licenseImageFrontPath = fileDirPath
                        R.id.id_license_back_iv -> licenseImageBackPath = fileDirPath
                        R.id.commitment_upload_iv -> commitmentImagePath = fileDirPath
                        else -> {
                        }
                    }

                    val fromView = findViewById<ImageView>(fromViewId)
                    if (fromView == null) {
                        return
                    }

                    Glide.with(this).load(fileDirPath).diskCacheStrategy(DiskCacheStrategy.NONE).into(fromView)
                    val application = application as CloudAccountApplication
                    application.getOssSecurityToken(true, true, fileDirPath, fromViewId, this@FormLicenseLogoutActivity.javaClass)

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
        // OCR 操作来源
        val fromView = findViewById<CompanyMemberEditView>(fromViewId)
        if (fromView == null) {
            return
        }

        // 上传oss
        val application = application as CloudAccountApplication

        if (contentType.isEmpty()) {
            return
        }

        when (contentType) {
            CameraActivity.CONTENT_TYPE_ID_CARD_FRONT -> {
                // 身份证正面
                ProductUtils.recIDCard(this, IDCardParams.ID_CARD_SIDE_FRONT, filePath, object : RecognizeIDCardResultCallBack {
                    override fun onResult(result: IDCardResult) {

                        loadOcrLocalAndUploadOss(fromView, filePath, application, fromViewId)

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

                loadOcrLocalAndUploadOss(fromView, filePath, application, fromViewId)
            }
            else -> {
            }
        }


    }

    private fun loadOcrLocalAndUploadOss(
        fromView: CompanyMemberEditView,
        filePath: String,
        application: CloudAccountApplication,
        fromViewId: Int
    ) {
        fromView.loadResultImage(filePath)
        application.getOssSecurityToken(true, false, filePath, fromViewId, this@FormLicenseLogoutActivity.javaClass)
    }


    private fun handleCommit() {


        val legalFrontImageUrl = legal_person_view.frontImageUrl
        if (!ProductUtils.hasIdCardUrl(legalFrontImageUrl, true, "法人")) {
            return
        }
        val legalBackNameUrl = legal_person_view.backImageUrl
        if (!ProductUtils.hasIdCardUrl(legalBackNameUrl, false, "法人")) {
            return
        }


        val nameValue = legal_person_view.getNameValue()
        if (nameValue.isBlank()) {
            ToastUtils.showShort("请输入法人姓名")
            return
        }
        if (nameValue.length < 2) {
            ToastUtils.showShort("请输入不少于两个字的姓名")
            return
        }


        val idNumberValue = legal_person_view.getIdNumberValue()
        if (idNumberValue.isBlank()) {
            ToastUtils.showShort("身份证号")
            return
        }
        if (!ProductUtils.isIdCardNumber(idNumberValue)) {
            return
        }


        val phoneStr = legal_person_view.getPhoneValue()
        if (phoneStr.isBlank()) {
            ToastUtils.showShort("请输入联系方式")
            return
        }

        if (!ProductUtils.isPhoneNumber(phoneStr)) {
            return
        }


        val organizationFullName = organization_name_et.text.toString().trim()
        if (organizationFullName.isBlank()) {
            ToastUtils.showShort("请输入机构全称")
            return
        }

        val taxpayerNumber = trn_et.text.toString().trim()
        if (!ProductUtils.is18TaxNumber(taxpayerNumber)) {
            return
        }


        val logoutReason = logout_et.text.toString().trim()
        if (logoutReason.isBlank()) {
            ToastUtils.showShort("请输入注销原因")
            return
        }



        if (mIndex == 0 && commitmentImagePath.isBlank()) {
            ToastUtils.showShort("请上传注销承诺书")
            return
        }


        if (licenseImageFrontPath.isBlank()) {
            ToastUtils.showShort("请上传营业执照正本")
            return
        }
        if (licenseImageBackPath.isBlank()) {
            ToastUtils.showShort("请上传营业执照副本")
            return
        }

        val model = PersonalLicenseLogout().apply {
            idno = idNumberValue
            imgsIdno = mIdImageUrlList.apply {
                clear()
                add(IdentityImg(imgUrl = legal_person_view.frontImageUrl, mold = Constants.I1))
                add(IdentityImg(imgUrl = legal_person_view.backImageUrl, mold = Constants.I2))
            }
            imgsLicense = mLicenseImagesUrlList.apply {
                clear()
                add(IdentityImg(imgUrl = licenseImageFrontUrl, mold = Constants.I3))
                add(IdentityImg(imgUrl = licenseImageBackUrl, mold = Constants.I4))
            }

            if (mIndex == 0) {
                imgsCommitment = mCommitmentImagesUrlList.apply {
                    clear()
                    add(IdentityImg(imgUrl = commitmentImageUrl, mold = Constants.I6))
                }
            }

            legalPersonName = nameValue
            money = mFinalMoney
            phone = phoneStr
            productId = mProductId
            productPriceId = mProductPriceId

            enterpriseCode = taxpayerNumber
            enterpriseName = organizationFullName
            reason = logoutReason
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
                .putExtra(Constants.PARAM_MOLD, Constants.P6)
        )
        finish()
    }


}