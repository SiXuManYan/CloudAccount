package com.fatcloud.account.feature.forms.personal.license

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
import com.fatcloud.account.entity.local.form.PersonalLicenseDraft
import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.persional.PersonalInfo
import com.fatcloud.account.entity.users.User
import com.fatcloud.account.event.entity.ImageUploadEvent
import com.fatcloud.account.extend.LimitInputTextWatcher
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
import kotlinx.android.synthetic.main.activity_form_license_personal.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


/**
 * Created by Wangsw on 2020/6/12 0012 13:33.
 * </br>
 * 个体户营业执照表单
 * @see Constants.P1
 */
class FormLicensePersonalActivity : BaseMVPActivity<FormLicensePersonalPresenter>(), FormLicensePersonalView {

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
    private var mProductPriceId: String = "0"


    /** 产品id */
    private var mProductId: String = "0"

    /** 用户选中的城市信息id */
    private var mAreaId: String = ""

    /** 用户选中的城市名称 */
    private var mAreaName: String = ""


    var mediaType = 0

    /** 身份证正反面地址集合 */
    var mIdEntityImg: ArrayList<IdentityImg> = ArrayList()

    /** 正面 */
    var isFaceUp = false


    override fun getLayoutId() = R.layout.activity_form_license_personal

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

        intent.extras!!.getString(Constants.PARAM_PRODUCT_ID)?.let {
            mProductId = it
        }

        intent.extras!!.getString(Constants.PARAM_FINAL_MONEY)?.let {
            mFinalMoney = it
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
            val finalUrl = it.finalUrl
            val fromView = findViewById<CompanyMemberEditView>(it.fromViewId)
            if (fromView != null) {
                fromView.setImageUrl(finalUrl)
            }
        })
    }

    private fun initView() {

        setMainTitle("注册人信息")

        // 个人信息
        legal_person_ev.apply {
            currentMold = Constants.SH1
            initHighlightTitle("个人信息（请扫描身份证正反面）")
            initNameTitle("姓名")
            showNation()
            showGenderView(true)
            hideAddress()
            showPhone(true)
            hideShareRatio()
            hideBottomSplit()
        }

        zero_choice_name_et.addTextChangedListener(LimitInputTextWatcher(zero_choice_name_et))
        first_choice_name_et.addTextChangedListener(LimitInputTextWatcher(first_choice_name_et))
        second_choice_name_et.addTextChangedListener(LimitInputTextWatcher(second_choice_name_et))


        restoreDraft()
    }

    private fun restoreDraft() {
        val draft = PersonalLicenseDraft.get()
        if (draft.loginPhone != User.get().username || draft.productId.isNullOrBlank() || draft.productId != mProductId) {
            return
        }
        draft.zeroName?.let {
            zero_choice_name_et.setText(it)
        }
        draft.firstName?.let {
            first_choice_name_et.setText(it)
        }
        draft.secondName?.let {
            second_choice_name_et.setText(it)
        }

        draft.businessScopeId?.let {
            selectPid = it
        }

        draft.businessScopeName?.let {
            selectPidNames = it
            business_scope_value.text = Arrays.toString(it.toArray()).replace("[", "").replace("]", "")
        }

        draft.employedNum?.let {
            employees_number_et.setText(it)
        }
        draft.capital?.let {
            amount_of_funds_et.setText(it.stripTrailingZeros().toPlainString())
        }

        draft.formId?.let {
            selectFormId = it
        }

        draft.formName?.let {
            selectFormName = it
            formation_value.text = it
        }

        draft.area?.let {
            mAreaName = it
            addr_value.text = it
        }

        draft.detailAddress?.let {
            detail_addr_et.setText(it)
        }

        draft.realName?.let {
            legal_person_ev.setNameValue(it, false)
        }

        draft.gender?.let {
            legal_person_ev.genderIndex = it
            legal_person_ev.setGenderValue(it)
        }

        draft.idno?.let {
            legal_person_ev.setIdNumberValue(it)
        }
        draft.phone?.let {
            legal_person_ev.setPhoneValue(it)
        }

        draft.nation?.let {
            legal_person_ev.setNationValue(it)
        }
    }


    @OnClick(
        R.id.business_scope_rl,
        R.id.bottom_left_tv,
        R.id.bottom_right_tv,
        R.id.formation_rl,
        R.id.city_rl
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
                            this@FormLicensePersonalActivity.selectFormId = currentSelected.id
                            this@FormLicensePersonalActivity.selectFormName = currentSelected.name
                            this@FormLicensePersonalActivity.formation_value.text = currentSelected.name
                        }
                    })
                    show(supportFragmentManager, this.tag)

                }
            }
            R.id.city_rl -> {
                ProductUtils.showLocationPicker(this, object : OnCityItemClickListener() {
                    override fun onSelected(province: ProvinceBean, city: CityBean, district: DistrictBean) {
                        mAreaName = StringUtils.getString(
                            R.string.location_information_format,
                            province.name,
                            city.name,
                            district.name
                        )
                        addr_value.text = mAreaName
                        mAreaId = district.id
                    }

                    override fun onCancel() = Unit
                })
            }
            else -> {
            }
        }
    }

    private fun saveDraft() {
        val draft = PersonalLicenseDraft().apply {
            loginPhone = User.get().username
            finalMoney = mFinalMoney
            productId = mProductId
            productPriceId = mProductPriceId
            mold = Constants.P1

            zeroName = zero_choice_name_et.text.toString().trim()
            firstName = first_choice_name_et.text.toString().trim()
            secondName = second_choice_name_et.text.toString().trim()
            businessScopeId = selectPid
            businessScopeName = selectPidNames
            employedNum = employees_number_et.text.toString().trim()
            capital = ProductUtils.getEditValueToBigDecimal(amount_of_funds_et.text.toString().trim())
            formId = selectFormId
            formName = selectFormName

            area = mAreaName
            detailAddress = detail_addr_et.text.toString().trim()
            // 法人信息
            realName = legal_person_ev.getNameValue()
            gender = legal_person_ev.genderIndex
            nation = legal_person_ev.getNationValue()
            idno = legal_person_ev.getIdNumberValue()
            phone = legal_person_ev.getPhoneValue()
            identityImg = mIdEntityImg
        }
        database.personalLicenseDraftDao().add(draft)
        PersonalLicenseDraft.update()
        ToastUtils.showShort(R.string.save_success)
    }


    private fun handlePost() {

        val zeroName = zero_choice_name_et.text.toString().trim()
        if (!ProductUtils.isThreeChineseName(zeroName, getString(R.string.zero_choice_name))) {
            return
        }
        val firstName = first_choice_name_et.text.toString().trim()
        if (!ProductUtils.isThreeChineseName(firstName, getString(R.string.first_choice_name))) {
            return
        }
        val secondName = second_choice_name_et.text.toString().trim()
        if (!ProductUtils.isThreeChineseName(secondName, getString(R.string.second_choise_name))) {
            return
        }


        if (selectPidNames.isEmpty()) {
            ToastUtils.showShort("请选择经营范围")
            return
        }


        val employeesNumberStr = employees_number_et.text.toString().trim()
        if (employeesNumberStr.isBlank()) {
            ToastUtils.showShort("请输入从业人数")
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

        // 法人
        if (!ProductUtils.hasIdCardUrl(legal_person_ev.frontImageUrl, true)) {
            return
        }
        if (!ProductUtils.hasIdCardUrl(legal_person_ev.backImageUrl, false)) {
            return
        }


        val nameValue = legal_person_ev.getNameValue()
        if (nameValue.isBlank()) {
            ToastUtils.showShort("请输入姓名")
            return
        }
        if (legal_person_ev.genderIndex == 0) {
            ToastUtils.showShort("请选择性别")
            return
        }


        if (nameValue.length < 2) {
            ToastUtils.showShort("请输入不少于两个字的姓名")
            return
        }

        val idNumberValue = legal_person_ev.getIdNumberValue()
        if (idNumberValue.isBlank()) {
            ToastUtils.showShort("身份证号")
            return
        }
        if (!ProductUtils.isIdCardNumber(idNumberValue)) {
            return
        }

        val nationValue = legal_person_ev.getNationValue()
        if (nationValue.isBlank()) {
            ToastUtils.showShort("请输入民族")
            return
        }


        val phoneStr = legal_person_ev.getPhoneValue()
        if (phoneStr.isBlank()) {
            ToastUtils.showShort("请输入联系方式")
            return
        }



        mIdEntityImg.apply {
            clear()
            add(IdentityImg(imgUrl = legal_person_ev.frontImageUrl, mold = Constants.I1))
            add(IdentityImg(imgUrl = legal_person_ev.backImageUrl, mold = Constants.I2))
        }
        val enterpriseInfo = PersonalInfo().apply {
            addr = detailAddrStr
            area = mAreaName
            businessScope = selectPid
            capital = ProductUtils.getEditValueToBigDecimal(amountOfFundsStr)
            income = capital
            employedNum = employeesNumberStr
            form = selectFormId.toInt()
            gender = legal_person_ev.genderIndex.toString()
            idno = idNumberValue
            imgs = mIdEntityImg
            money = ProductUtils.getEditValueToBigDecimal(mFinalMoney)
            name0 = zeroName
            name1 = firstName
            name2 = secondName
            nation = nationValue
            productId = mProductId
            productPriceId = mProductPriceId
            realName = nameValue
            tel = phoneStr
        }
        presenter.addLicensePersonal(this, enterpriseInfo)
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
            this@FormLicensePersonalActivity.javaClass
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

    override fun addLicensePersonalSuccess(preparePay: PreparePay) {
        ToastUtils.showShort("营业执照上传成功")
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