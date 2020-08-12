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
import com.fatcloud.account.entity.form.p9p10.NativeFormPersonalPackageP9P10Draft
import com.fatcloud.account.entity.form.p9p10.NativeFormPersonalPackageP9P10
import com.fatcloud.account.entity.order.IdentityImg
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


        ProductUtils.onlySupportChineseInput(zero_choice_name_et, first_choice_name_et, second_choice_name_et)
        restoreDraft()

    }

    private fun restoreDraft() {
        val draft = NativeFormPersonalPackageP9P10Draft.get()
        if (draft.loginPhone != User.get().username || draft.mold != mMold || draft.productId.isNullOrBlank() || draft.productId != mProductId) {
            return
        }
        draft.apply {
            detail_addr_et.setText(addr)
            mAreaName = area
            addr_value_tv.text = area

            bank_number_et.setText(bankNo)
            bank_phone_et.setText(bankPhone)



            draft.businessScopeId?.let {
                selectPid = it
            }

            draft.businessScopeName?.let {
                selectPidNames = it
                business_scope_value.text = Arrays.toString(it.toArray()).replace("[", "").replace("]", "")
            }



            amount_of_funds_et.setText(capital)
            employees_number_et.setText(employedNum)
            selectFormId = formId
            selectFormName = formName
            formation_value.text = formName

            zero_choice_name_et.setText(name0)
            first_choice_name_et.setText(name1)
            second_choice_name_et.setText(name2)

            legal_person_view.apply {
                setGenderValue(gender)
                setIdNumberValue(idNumber, true)
                setNationValue(nation)
                setNameValue(realName, true)
                setPhoneValue(tel)

            }
            idImages?.let {
                if (!it.isNullOrEmpty()) {
                    legal_person_view.setServerImage(it as ArrayList<IdentityImg>)
                }

            }


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
                val replace = Arrays.toString(selectPidNames.toArray()).replace("[", "").replace("]", "")
                business_scope_value.text = replace
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
                        result.gender?.let {
                            fromView.setGenderValue(it.words)
                        }
                        result.ethnic?.let {
                            fromView.setEthnicValue(it.words, true)
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
        application.getOssSecurityToken(true, isFaceUp, filePath, fromViewId, this@FormPersonalPackageP9P10Activity.javaClass)
    }


    private fun saveDraft() {

        val draft = NativeFormPersonalPackageP9P10Draft().apply {
            loginPhone = User.get().username
            productId = mProductId
            productPriceId = mProductPriceId
            finalMoney = mFinalMoney

            addr = detail_addr_et.text.toString().trim()
            area = mAreaName
            bankNo = bank_number_et.text.toString()
            bankPhone = bank_phone_et.text.toString()
            businessScopeId = selectPid
            businessScopeName = selectPidNames

            capital = amount_of_funds_et.text.toString().trim()

            employedNum = employees_number_et.text.toString().trim()
            formId = selectFormId
            formName = selectFormName


            gender = legal_person_view.genderIndex
            idNumber = legal_person_view.getIdNumberValue()
            idImages = mIdEntityImg
            name0 = zero_choice_name_et.text.toString().trim()
            name1 = first_choice_name_et.text.toString().trim()
            name2 = second_choice_name_et.text.toString().trim()
            nation = legal_person_view.getNationValue()
            realName = legal_person_view.getNameValue()
            tel = legal_person_view.getPhoneValue()
            mold = mMold
        }

        database.p9p10PersonalPackageDao().add(draft)
        NativeFormPersonalPackageP9P10Draft.update()

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