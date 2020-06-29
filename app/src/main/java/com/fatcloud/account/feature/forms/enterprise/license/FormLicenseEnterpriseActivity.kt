package com.fatcloud.account.feature.forms.enterprise.license

import android.content.Intent
import android.text.InputType
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import butterknife.OnClick
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.VibrateUtils
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo
import com.fatcloud.account.event.entity.ImageUploadEvent
import com.fatcloud.account.event.entity.OrderPaySuccessEvent
import com.fatcloud.account.feature.defray.prepare.PayPrepareActivity
import com.fatcloud.account.feature.extra.BusinessScopeActivity
import com.fatcloud.account.feature.matisse.Matisse
import com.fatcloud.account.view.CompanyMemberEditView
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_license_enterprise.*
import kotlinx.android.synthetic.main.activity_form_license_enterprise.scroll_nsv
import kotlinx.android.synthetic.main.inc_title_bar.*
import kotlinx.android.synthetic.main.view_company_member_edit.view.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Wangsw on 2020/6/10 0010 18:30.
 * </br>
 *  企业套餐表单
 */
class FormLicenseEnterpriseActivity : BaseMVPActivity<FormLicenseEnterprisePresenter>(), FormLicenseEnterpriseView {


    /**
     * 用户选中的一级经营范围pid
     */
    private var selectPid = ArrayList<String>()

    /**
     * 用户选中的一级经营范围pid名称
     */
    private var selectPidNames = ArrayList<String>()

    /**
     * 年收入
     */
    private var incomeMoney: String = ""


    /**
     * 最终收入
     */
    private var finalMoney: String = ""


    /**
     * 选中的产品价格id
     */
    private var mProductPriceId: String = "0"

    /**
     * 产品id
     */
    private var mProductId: String = "0"


    /**
     * 产品类型
     * P1 P2 ....
     */
    private var mProductType: String = Constants.P2

    /**
     * 位置信息id
     */
    private var areaId: String = ""

    private var areaName: String = ""

    var isFaceUp = false


    /**
     * 是否为PAGE B
     *
     */
    private var isBMode = false



//    var faceUpUrl = ""
//    var faceDownUrl = ""

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
            incomeMoney = it
        }

        intent.extras!!.getString(Constants.PARAM_PRODUCT_ID)?.let {
            mProductId = it
        }

        intent.extras!!.getString(Constants.PARAM_FINAL_MONEY)?.let {
            finalMoney = it
        }


        intent.extras!!.getString(Constants.PARAM_PRODUCT_PRICE_ID)?.let {
            mProductPriceId = it
        }

        intent.extras!!.getString(Constants.PARAM_PRODUCT_TYPE)?.let {
            mProductType = it
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


//        zero_choice_name.setTitleAndHint(getString(R.string.zero_company_name), getString(R.string.no_less_than_3_word))
//        first_choice_name.setTitleAndHint(getString(R.string.first_company_name), getString(R.string.no_less_than_3_word))
//        second_choice_name.setTitleAndHint(getString(R.string.second_company_name), getString(R.string.no_less_than_3_word))
//        investment_period.setTitleAndHint(getString(R.string.invest_year_num), getString(R.string.invest_year_num_hint))
//            .setInputType(InputType.TYPE_CLASS_NUMBER)


//        amount_of_funds.setTitleAndHint(getString(R.string.amount_of_fund), getString(R.string.amount_of_fund_hint))
//            .setInputType(InputType.TYPE_CLASS_NUMBER)
//        bank_number.setTitleAndHint(getString(R.string.bank_card_number), getString(R.string.for_tax_registration))
//            .setInputType(InputType.TYPE_CLASS_NUMBER)
//        bank_phone.setTitleAndHint(getString(R.string.bank_phone), getString(R.string.for_tax_registration)).setInputType(InputType.TYPE_CLASS_NUMBER)
//        detail_addr.setTitleAndHint(getString(R.string.detailed_address), getString(R.string.detailed_address_hint))

        // 法人信息
        legal_person_ev.apply {
            currentMold = Constants.SH1
            initHighlightTitle(getString(R.string.legal_person_info))
            initNameTitleHint(getString(R.string.legal_person_name), getString(R.string.legal_person_name_hint))
            initIdNumberTitleHint(
                getString(R.string.identity_number),
                getString(R.string.identity_number_hint)
            )
            initIdAddressTitleHint(getString(R.string.id_address), getString(R.string.id_address_hint))
            initPhoneTitleHint(getString(R.string.contact_number), getString(R.string.contact_number_hint)).setInputType(InputType.TYPE_CLASS_NUMBER)
            initShareRatioHint(getString(R.string.share_ratio_hint))

        }

        // 监事信息
        supervisor_ev.apply {
            currentMold = Constants.SH2
            initHighlightTitle(getString(R.string.supervisor_info))
            initNameTitleHint(getString(R.string.supervisor_name), getString(R.string.supervisor_name_hint))
            initIdNumberTitleHint(
                getString(R.string.identity_number),
                getString(R.string.identity_number_hint)
            )
            initIdAddressTitleHint(getString(R.string.id_address), getString(R.string.id_address_hint))
            initPhoneTitleHint(getString(R.string.contact_number), getString(R.string.contact_number_hint)).setInputType(InputType.TYPE_CLASS_NUMBER)
            initShareRatioHint(getString(R.string.share_ratio_hint_2))
        }

        // 默认股东信息
        shareholder_ev.apply {
            currentMold = Constants.SH3
            initHighlightTitle(getString(R.string.shareholder_info2))
            initNameTitleHint(getString(R.string.shareholder_name), getString(R.string.shareholder_name_hint))
            initIdNumberTitleHint(
                getString(R.string.identity_number),
                getString(R.string.identity_number_hint)
            )
            initIdAddressTitleHint(getString(R.string.id_address), getString(R.string.id_address_hint))
            initPhoneTitleHint(getString(R.string.contact_number), getString(R.string.contact_number_hint)).setInputType(InputType.TYPE_CLASS_NUMBER)
            initShareRatioHint(getString(R.string.share_ratio_hint))
            showAddActionView().setOnClickListener {
                VibrateUtils.vibrate(10)
                // it.visibility = View.GONE
                shareholder_more_container.addView(getShareholderView(0), 0)
                scroll_nsv.smoothScrollTo(0, ScreenUtils.getScreenHeight())

            }

        }

    }

    private fun getShareholderView(index: Int): CompanyMemberEditView {
        return CompanyMemberEditView(this).apply {
            id = index + 1 // 保证id 不从0开始
            currentMold = Constants.SH3
            // 坐标
            tag = index
            initHighlightTitle(getString(R.string.shareholder_info2))
            initNameTitleHint(getString(R.string.shareholder_name), getString(R.string.shareholder_name_hint))
            initIdNumberTitleHint(
                getString(R.string.identity_number),
                getString(R.string.identity_number_hint)
            )
            initIdAddressTitleHint(getString(R.string.id_address), getString(R.string.id_address_hint))
            initPhoneTitleHint(getString(R.string.contact_number), getString(R.string.contact_number_hint)).setInputType(InputType.TYPE_CLASS_NUMBER)
            initShareRatioHint(getString(R.string.share_ratio_hint))

            //  添加股东
            showAddActionView().setOnClickListener {
                VibrateUtils.vibrate(10)
                it.visibility = View.GONE
                shareholder_more_container.addView(getShareholderView(index + 1), index + 1)
                scroll_nsv.smoothScrollTo(0, ScreenUtils.getScreenHeight())
            }

            // 移除当前股东
            showHighlightDeleteView().setOnClickListener {
                shareholder_more_container.removeViewAt(index)
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
            1 -> {
                selectPid = data.getStringArrayListExtra(Constants.PARAM_SELECT_PID)
                selectPidNames = data.getStringArrayListExtra(Constants.PARAM_SELECT_PID_NAME)
                business_scope_value.text = Arrays.toString(selectPidNames.toArray()).replace("[", "").replace("]", "")
            }

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
                    application.getOssSecurityToken(true, isFaceUp, fileDirPath, fromViewId, this@FormLicenseEnterpriseActivity.javaClass)
                }
            }
            else -> {
            }
        }


    }

    @OnClick(
        R.id.business_scope_rl,
        R.id.bottom_left_tv,
        R.id.bottom_right_tv,
        R.id.addr_rl
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.business_scope_rl -> {
                // 参照 EnterpriseInfo
                startActivityForResult(Intent(this, BusinessScopeActivity::class.java).putExtra(Constants.PARAM_PRODUCT_TYPE, mProductType), 1)
            }
            R.id.bottom_left_tv -> {
                // 保存
            }

            R.id.bottom_right_tv -> {
//                handlePost()
                changUi()

            }
            R.id.addr_rl -> {
                ProductUtils.showLocationPicker(this, object : OnCityItemClickListener() {
                    override fun onSelected(province: ProvinceBean, city: CityBean, district: DistrictBean) {
                        areaName = StringUtils.getString(R.string.location_information_format, province.name, city.name, district.name)
                        addr_value_iv.text = areaName
                        areaId = district.id
                    }

                    override fun onCancel() = Unit
                })
            }

            else -> {
            }
        }
    }

    private fun changUi() {
        val animation = if (isBMode) {
            AnimationUtils.loadAnimation(context, R.anim.form_out_to_left)
        } else {
            AnimationUtils.loadAnimation(context, R.anim.form_in_from_left)
        }
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) = Unit

            override fun onAnimationEnd(p0: Animation?) {
                if (isBMode) {
                    view_switcher.displayedChild = 1
                }else{
                    view_switcher.displayedChild = 0
                }
            }

            override fun onAnimationStart(p0: Animation?) = Unit
        })
        isBMode = !isBMode
        scroll_nsv.startAnimation(animation)
        title_container_rl.startAnimation(animation)
        bottom_ll.startAnimation(animation)


    }

    private fun handlePost() {

        // 非空校验
        if (!ProductUtils.checkEditEmptyWithVibrate(

                // 默认信息
//                first_choice_name,
//                second_choice_name,
//                investment_period,
//                amount_of_funds,
//                bank_number,
//                bank_phone,
//                detail_addr,
                // 法人
                legal_person_ev.ev_00_name,
                legal_person_ev.ev_01_id_number,
                legal_person_ev.ev_02_id_addr,
                legal_person_ev.ev_03_phone,

                // 监事
                supervisor_ev.ev_00_name,
                supervisor_ev.ev_01_id_number,
                supervisor_ev.ev_02_id_addr,
                supervisor_ev.ev_03_phone,

                // 默认股东 shareholder_ev
                shareholder_ev.ev_00_name,
                shareholder_ev.ev_01_id_number,
                shareholder_ev.ev_02_id_addr,
                shareholder_ev.ev_03_phone
            )
        ) {
            return
        }
        val zeroName = zero_choice_name_et.text.toString()
        if (zeroName.isBlank()) {
            ToastUtils.showShort("请输入首选公司名称")
            return
        }

        val firstName = first_choice_name_et.text.toString()
        if (firstName.isBlank()) {
            ToastUtils.showShort("请输入备选公司名称1")
            return
        }
        val secondName = second_choice_name_et.text.toString()
        if (secondName.isBlank()) {
            ToastUtils.showShort("请输入备选公司名称2")
            return
        }

        if (investment_period_et.text.toString().isBlank()) {
            ToastUtils.showShort("请输入出资年限")
            return
        }
        if (amount_of_funds_et.text.toString().isBlank()) {
            ToastUtils.showShort("请输入资金数额 ")
            return
        }
        if (bank_number_et.text.toString().isBlank()) {
            ToastUtils.showShort("请输入银行卡号 ")
            return
        }
        if (bank_phone_et.text.toString().isBlank()) {
            ToastUtils.showShort("请输入银行预留手机号 ")
            return
        }

        if (detail_addr_et.text.toString().isBlank()) {
            ToastUtils.showShort("请输入详细地址 ")
            return
        }


        if (legal_person_ev.getShareRatioValue().isBlank()) {
            ToastUtils.showShort("请输入法人股份占比")
            return
        }
        if (shareholder_ev.getShareRatioValue().isBlank()) {
            ToastUtils.showShort("请输入股东股份占比")
            return
        }


        val enterpriseInfo = EnterpriseInfo().apply {
            addr = detail_addr_et.text.toString().trim()
            area = areaName
            bankNo = bank_number_et.text.toString().trim()
            bankPhone = bank_phone_et.text.toString().trim()
            businessScope?.addAll(ProductUtils.stringList2IntList(selectPid))
            enterpriseName0 = zero_choice_name_et.text.toString().trim()
            enterpriseName1 = first_choice_name_et.text.toString().trim()
            enterpriseName2 = second_choice_name_et.text.toString().trim()
            income = ProductUtils.getEditValueToBigDecimal(incomeMoney)
            investMoney = ProductUtils.getEditValueToBigDecimal(amount_of_funds_et.text.toString().trim())
            investYearNum = investment_period_et.text.toString().trim()
            money = ProductUtils.getEditValueToBigDecimal(finalMoney)
            productId = mProductId
            productPriceId = mProductPriceId.toInt()
            shareholders = presenter.getShareHolders(
                legal_person_ev.getShareHolder(),
                supervisor_ev.getShareHolder(),
                shareholder_ev.getShareHolder(),
                shareholder_more_container
            )
        }
        presenter.addEnterprise(this, enterpriseInfo)

    }

    override fun addEnterpriseSuccess(preparePay: PreparePay) {
        ToastUtils.showShort("套餐添加成功")
        startActivity(
            Intent(this, PayPrepareActivity::class.java)
                .putExtra(Constants.PARAM_ORDER_ID, preparePay.orderId)
                .putExtra(Constants.PARAM_ORDER_NUMBER, preparePay.orderNo)
                .putExtra(Constants.PARAM_MONEY, preparePay.money.stripTrailingZeros().toPlainString())
//                .putExtra(Constants.PARAM_MONEY, finalMoney)
                .putExtra(Constants.PARAM_IMAGE_URL, preparePay.productLogoImgUrl)
                .putExtra(Constants.PARAM_PRODUCT_NAME, preparePay.productName)
                .putExtra(Constants.PARAM_DATE, preparePay.createDt)
        )
        finish()

    }


}