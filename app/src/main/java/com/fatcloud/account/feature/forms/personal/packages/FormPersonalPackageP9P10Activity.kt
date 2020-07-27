package com.fatcloud.account.feature.forms.personal.packages

import android.content.Intent
import android.view.View
import butterknife.OnClick
import com.baidu.ocr.sdk.model.IDCardParams
import com.baidu.ocr.sdk.model.IDCardResult
import com.baidu.ocr.ui.camera.CameraActivity
import com.baidu.ocr.ui.util.FileUtil
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.data.CloudDataBase
import com.fatcloud.account.entity.commons.Form
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.form.p8.NativeFormPersonalPackageP9P10
import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.event.entity.ImageUploadEvent
import com.fatcloud.account.feature.defray.prepare.PayPrepareActivity
import com.fatcloud.account.feature.extra.BusinessScopeActivity
import com.fatcloud.account.feature.ocr.RecognizeIDCardResultCallBack
import com.fatcloud.account.feature.sheet.form.FormSheetFragment
import com.fatcloud.account.view.CompanyMemberEditView
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_personal_package_common.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by Wangsw on 2020/7/23 0023 10:23.
 * </br>
 * 个体户套餐 P9
 * 个人独资企业套餐 P10 (UI完全一样，只是接口不一样)
 */
class FormPersonalPackageP9P10Activity : BaseMVPActivity<FormPersonalPackageP9P10Presenter>(), FormPersonalPackageP9P10View {

    lateinit var database: CloudDataBase @Inject set

    /** 用户选中的一级经营范围pid */
    private var selectPid = ArrayList<String>()

    /** 用户选中的一级经营范围pid名称 */
    private var selectPidNames = ArrayList<String>()

    /** 用户选中的组成形式id */
    private var selectFormId = ""

    /** 用户选中的组成形式名字 */
    private var selectFormName = ""

    /** 最终需支付金额 */
    private var mFinalMoney: String = ""

    /** 选中的产品价格id */
    private var mProductPriceId: String = ""

    /** 产品id */
    private var mProductId: String = ""

    /** 区分类型P9 P10
     * @see Constants.P9
     * @see Constants.P10
     * */
    private var mMold: String = ""


    /** 用户选中的城市信息id */
    private var mAreaId: String = ""

    /** 用户选中的城市名称 */
    private var mAreaName: String = ""

    /** 身份证正反面地址集合 */
    var mIdEntityImg: ArrayList<IdentityImg> = ArrayList()

    /** 正面 */
    var isFaceUp = false

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId(): Int = R.layout.activity_form_personal_package_common

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

        intent.extras!!.getString(Constants.PARAM_MOLD)?.let {
            mMold = it
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
                R.id.legal_person_view -> {
                    legal_person_view.setImageUrl(finalUrl)
                }
                else -> {
                }
            }

        })
    }

    private fun initView() {
        setMainTitle("企业基本信息")
        // 个人信息
        legal_person_view.apply {
            currentMold = Constants.SH1
            initHighlightTitle("法人信息（请扫描身份证正反面）")
            initNameTitle("姓名")
            showGenderView(true)
            showNation()
            showIdNumber(true)
            showPhone(true)
            showIdExpirationDate(false)
            hideAddress()

            hideShareRatio()
            hideBottomSplit()
        }

    }

    @OnClick(
        R.id.business_scope_rl,
        R.id.bottom_left_tv,
        R.id.bottom_right_tv,
        R.id.formation_rl,
        R.id.addr_rl
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.business_scope_rl -> {
                startActivityForResult(
                    Intent(this, BusinessScopeActivity::class.java).putExtra(Constants.PARAM_PRODUCT_TYPE, Constants.P1),
                    Constants.REQUEST_BUSINESS_SCOPE
                )
            }
            R.id.bottom_left_tv -> saveDraft()
            R.id.bottom_right_tv -> {
                ProductUtils.handleDoubleClick(view)
                handlePost()
            }
            R.id.formation_rl -> {
                FormSheetFragment.newInstance().apply {
                    setOnFormSelectListener(object : FormSheetFragment.OnItemSelectedListener {
                        override fun onItemSelected(currentSelected: Form) {
                            this@FormPersonalPackageP9P10Activity.selectFormId = currentSelected.id
                            this@FormPersonalPackageP9P10Activity.selectFormName = currentSelected.name
                            this@FormPersonalPackageP9P10Activity.formation_value.text = currentSelected.name
                        }
                    })
                    show(supportFragmentManager, this.tag)

                }
            }

            R.id.addr_rl -> {
                ProductUtils.showLocationPicker(this, object : OnCityItemClickListener() {
                    override fun onSelected(province: ProvinceBean, city: CityBean, district: DistrictBean) {
                        mAreaName = StringUtils.getString(
                            R.string.location_information_format,
                            province.name,
                            city.name,
                            district.name
                        )
                        addr_value_tv.text = mAreaName
                        mAreaId = district.id
                    }

                    override fun onCancel() = Unit
                })
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
            Constants.REQUEST_BUSINESS_SCOPE -> {
                // 选中的经营范围
                selectPid = data.getStringArrayListExtra(Constants.PARAM_SELECT_PID)
                selectPidNames = data.getStringArrayListExtra(Constants.PARAM_SELECT_PID_NAME)
                business_scope_value.text =
                    Arrays.toString(selectPidNames.toArray()).replace("[", "").replace("]", "")
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
        fromView.loadResultImage(filePath)
        // 上传oss
        val application = application as CloudAccountApplication
        application.getOssSecurityToken(
            true,
            isFaceUp,
            filePath,
            fromViewId,
            this@FormPersonalPackageP9P10Activity.javaClass
        )
        if (contentType.isEmpty()) {
            return
        }
        when (contentType) {
            CameraActivity.CONTENT_TYPE_ID_CARD_FRONT -> {
                // 身份证正面
                ProductUtils.recIDCard(this, IDCardParams.ID_CARD_SIDE_FRONT, filePath, object : RecognizeIDCardResultCallBack {
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
                        result.ethnic?.let {
                            fromView.setEthnicValue(it.words, true)
                        }

                    }
                })
            }
            else -> {
            }
        }


    }


    private fun saveDraft() {


    }

    private fun handlePost() {
        val zeroName = zero_choice_name_et.text.toString().trim()
        if (!ProductUtils.isThreeChineseName(zeroName)) {
            return
        }
        val firstName = first_choice_name_et.text.toString().trim()
        if (!ProductUtils.isThreeChineseName(firstName)) {
            return
        }
        val secondName = second_choice_name_et.text.toString().trim()
        if (!ProductUtils.isThreeChineseName(secondName)) {
            return
        }

        if (selectPidNames.isEmpty()) {
            ToastUtils.showShort("请选择经营范围")
            return
        }

        val employeesNumberStr = employees_number_et.text.toString().trim()
        if (!ProductUtils.isRightEmployeesNumber(employeesNumberStr)) {
            return
        }
        val amountOfFundsStr = amount_of_funds_et.text.toString().trim()
        if (amountOfFundsStr.isBlank()) {
            ToastUtils.showShort("请输入资金数额")
            return
        }
        if (formation_value.text.toString().trim().isBlank()) {
            ToastUtils.showShort("请输入组成形式")
            return
        }


        val bankPhoneValue = bank_phone_et.text.toString()
        if (bankPhoneValue.trim().isBlank()) {
            ToastUtils.showShort("请输入银行预留手机号")
            return
        }

        val bankNumberValue = bank_number_et.text.toString()
        if (bankNumberValue.trim().isBlank()) {
            ToastUtils.showShort("请输入银行卡号")
            return
        }

        if (mAreaName.isBlank()) {
            ToastUtils.showShort("请选择地址")
            return
        }


        val detailAddrStr = detail_addr_et.text.toString().trim()
        if (detailAddrStr.isBlank()) {
            ToastUtils.showShort("请输入详细地址")
            return
        }


        try {
            val amountInt = amountOfFundsStr.toInt()
            if (amountInt < 10000 || amountInt > 1000000) {
                ToastUtils.showShort("请输入大于一万元小于一百万元的资金数额")
                return
            }

        } catch (e: Exception) {

        }

        if (!legal_person_view.checkParams()) {
            return
        }

        mIdEntityImg.apply {
            clear()
            add(IdentityImg(imgUrl = legal_person_view.frontImageUrl, mold = Constants.I1))
            add(IdentityImg(imgUrl = legal_person_view.backImageUrl, mold = Constants.I2))
        }

        val model = NativeFormPersonalPackageP9P10().apply {
            addr = detailAddrStr
            area = mAreaName

            bankNo = bankNumberValue
            bankPhone = bankPhoneValue

            businessScope = selectPid
            capital = ProductUtils.getEditValueToBigDecimal(amountOfFundsStr)
            employedNum = employeesNumberStr
            form = selectFormId.toInt()

            gender = legal_person_view.getGenderValue()

            idno = legal_person_view.getIdNumberValue()
            imgs = mIdEntityImg
            money = mFinalMoney
            name0 = zeroName
            name1 = firstName
            name2 = secondName
            nation = legal_person_view.getNationValue()
            productId = mProductId
            productPriceId = mProductPriceId
            realName = legal_person_view.getNameValue()
            tel = legal_person_view.getPhoneValue()
        }


        presenter.addPersonalPackageP9P10(this, model, mMold)

    }

    override fun commitSuccess(preparePay: PreparePay) {
        ToastUtils.showShort("提交成功")
        startActivity(
            Intent(this, PayPrepareActivity::class.java)
                .putExtra(Constants.PARAM_ORDER_ID, preparePay.orderId)
                .putExtra(Constants.PARAM_ORDER_NUMBER, preparePay.orderNo)
                .putExtra(Constants.PARAM_MONEY, preparePay.money.stripTrailingZeros().toPlainString())
                .putExtra(Constants.PARAM_IMAGE_URL, preparePay.productLogoImgUrl)
                .putExtra(Constants.PARAM_PRODUCT_NAME, preparePay.productName)
                .putExtra(Constants.PARAM_DATE, preparePay.createDt)
                .putExtra(Constants.PARAM_MOLD, Constants.P1)
        )
        finish()

    }


}