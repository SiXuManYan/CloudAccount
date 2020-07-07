package com.fatcloud.account.feature.forms.personal.tax

import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import butterknife.OnClick
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.data.CloudDataBase
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.local.form.PersonalTaxDraft
import com.fatcloud.account.entity.users.User
import com.fatcloud.account.event.entity.ImageUploadEvent
import com.fatcloud.account.feature.defray.prepare.PayPrepareActivity
import com.fatcloud.account.feature.matisse.Matisse
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_tax_registration_personal.*
import kotlinx.android.synthetic.main.layout_image_upload_single.*
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/13 0013 13:47.
 * </br>
 * 个体户税务登记
 */
class FormTaxRegistrationPersonalActivity : BaseMVPActivity<FormTaxRegistrationPersonalPresenter>(),
    FormTaxRegistrationPersonalView {


    lateinit var database: CloudDataBase @Inject set

    /**
     * 最终需支付金额
     */
    private var finalMoney: String = ""


    /**
     * 产品id
     */
    private var mProductId: String = "0"

    /**
     * 选中的产品价格id
     */
    private var mProductPriceId: String = "0"

    /**
     * 用户选中的省份地址
     */
    private var address: String = ""


    /**
     * 营业执照图片地址
     */
    private var mBusinessLicenseImgUrl: String = ""
    private var mBusinessLicensePath: String = ""






    override fun getLayoutId() = R.layout.activity_form_tax_registration_personal

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
            finalMoney = it
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
    }

    private fun initView() {
        setMainTitle("办理信息")
        restoreDraft()
    }

    private fun restoreDraft() {
        val draft = PersonalTaxDraft.get()
        if (draft.loginPhone != User.get().username || draft.productId.isNullOrBlank() || draft.productId != mProductId) {
            return
        }

        trn_et.setText(draft.taxpayerNo)
        legal_name_et.setText(draft.legalPersonName)
        id_number_tv_et.setText(draft.idNumber)
        bank_number_et.setText(draft.bankNumber)
        bank_phone_et.setText(draft.bankPhone)
        bank_phone_et.setText(draft.area)
        draft.area?.let {
            address = it
            addr_value.text = it
        }
        draft.detailAddress?.let {
            detail_addr_et.setText(it)
        }
        draft.businessLicenseImgUrl?.let {
            mBusinessLicenseImgUrl = it
        }
        draft.businessLicenseImgFilePath?.let {

            Glide.with(this).load(it).into(id_card_front_iv)
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
                    mBusinessLicensePath = fileDirPath
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
                        true,
                        fileDirPath,
                        fromViewId,
                        this@FormTaxRegistrationPersonalActivity.javaClass
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
        R.id.addr_rl,
        R.id.bottom_left_tv
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
            R.id.id_card_front_iv -> {
                ProductUtils.handleMediaSelect(this, Matisse.IMG, view.id)
            }
            R.id.addr_rl -> {
                ProductUtils.showLocationPicker(this, object : OnCityItemClickListener() {
                    override fun onSelected(
                        province: ProvinceBean,
                        city: CityBean,
                        district: DistrictBean
                    ) {
                        address = StringUtils.getString(
                            R.string.location_information_format,
                            province.name,
                            city.name,
                            district.name
                        )
                        addr_value.text = address
                    }

                    override fun onCancel() = Unit
                })
            }
            R.id.bottom_left_tv -> {
                saveDraft()
            }
            else -> {
            }
        }
    }


    private fun handleCommit() {
        val trnValue = trn_et.text.toString().trim()
        if (trnValue.isBlank()) {
            ToastUtils.showShort("请输入纳税人识别号")
            return
        }

        val legalNameValue = legal_name_et.text.toString().trim()
        if (legalNameValue.isBlank()) {
            ToastUtils.showShort("请输入法人姓名")
            return
        }

        if (legalNameValue.length < 2) {
            ToastUtils.showShort("请输入不少于两个字的姓名")
            return
        }


        val idNumberValue = id_number_tv_et.text.toString().trim()
        if (idNumberValue.isBlank()) {
            ToastUtils.showShort("请输入身份证号")
            return
        }
        if (!ProductUtils.isIdCardNumber(idNumberValue)) {
            return
        }

        val bankNumber = bank_number_et.text.toString()
        if (bankNumber.isBlank()) {
            ToastUtils.showShort("请输入银行卡号")
            return
        }
        if (!ProductUtils.isBankCardNumber(bankNumber)) {
            return
        }

        val bankPhoneValue = bank_phone_et.text.toString()
        if (bankPhoneValue.isBlank()) {
            ToastUtils.showShort("请输入银行预留手机号 ")
            return
        }
        if (!ProductUtils.isPhoneNumber(bankPhoneValue, "银行预留")) {
            return
        }

        if (addr_value.text.toString().trim().isBlank()) {
            ToastUtils.showShort("请选择地址")
            return
        }
        val detailAddress = detail_addr_et.text.toString()
        if (detailAddress.isBlank()) {
            ToastUtils.showShort("请输入详细地址 ")
            return
        }



        if (TextUtils.isEmpty(mBusinessLicenseImgUrl)) {
            ToastUtils.showShort("请上传营业执照副本")
            return
        }


        presenter.addLicensePersonal(
            lifecycle = this,
            money = finalMoney,
            productId = mProductId,
            productPriceId = mProductPriceId,
            taxpayerNo = trnValue,
            legalPersonName = legalNameValue,
            idno = idNumberValue,
            bankNo = bankNumber,
            phoneOfBank = bankPhoneValue,
            businessLicenseImgUrl = mBusinessLicenseImgUrl,
            addr = detailAddress,
            area = address
        )
    }


    private fun saveDraft() {

        val personalTaxDraft = PersonalTaxDraft().apply {
            taxpayerNo = trn_et.text.toString().trim()
            legalPersonName = legal_name_et.text.toString().trim()
            idNumber = id_number_tv_et.text.toString().trim()
            bankNumber = bank_number_et.text.toString().trim()
            bankPhone = bank_phone_et.text.toString().trim()
            area = address
            detailAddress = detail_addr_et.text.toString()
            loginPhone = User.get().username
            productId = mProductId
            productPriceId = mProductPriceId
            businessLicenseImgUrl = mBusinessLicenseImgUrl
            businessLicenseImgFilePath = mBusinessLicensePath
        }
        database.personalTaxDraftDao().add(personalTaxDraft)
        PersonalTaxDraft.update()

    }

    override fun commitSuccess(preparePay: PreparePay) {
        ToastUtils.showShort("提交成功")
        startActivity(
            Intent(this, PayPrepareActivity::class.java)
                .putExtra(Constants.PARAM_ORDER_ID, preparePay.orderId)
                .putExtra(Constants.PARAM_ORDER_NUMBER, preparePay.orderNo)
                .putExtra(
                    Constants.PARAM_MONEY,
                    preparePay.money.stripTrailingZeros().toPlainString()
                )
//                .putExtra(Constants.PARAM_MONEY, finalMoney)
                .putExtra(Constants.PARAM_IMAGE_URL, preparePay.productLogoImgUrl)
                .putExtra(Constants.PARAM_PRODUCT_NAME, preparePay.productName)
                .putExtra(Constants.PARAM_DATE, preparePay.createDt)
        )
        finish()
    }


}