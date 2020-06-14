package com.fatcloud.account.feature.forms.enterprise.license

import android.content.DialogInterface
import android.content.Intent
import android.text.InputType
import android.view.View
import androidx.core.view.forEach
import butterknife.OnClick
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.VibrateUtils
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo
import com.fatcloud.account.event.entity.ImageUploadEvent
import com.fatcloud.account.feature.extra.BusinessScopeActivity
import com.fatcloud.account.feature.matisse.Glide4Engine
import com.fatcloud.account.feature.matisse.Matisse
import com.fatcloud.account.view.CompanyMemberEditView
import com.fatcloud.account.view.dialog.AlertDialog
import com.zhihu.matisse.MimeType
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_license_enterprise.*
import kotlinx.android.synthetic.main.layout_bottom_action.*
import kotlinx.android.synthetic.main.view_company_member_edit.view.*
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Wangsw on 2020/6/10 0010 18:30.
 * </br>
 *  企业营业执照表单
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

    var isFaceUp = false
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

        intent.extras!!.getString(Constants.PARAM_INCOME_MONEY)?.let {
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


    }


    private fun initEvent() {
        // 图片上传成功
        presenter.subsribeEventEntity<ImageUploadEvent>(Consumer {

            val finalUrl = it.finalUrl
            val fromView = findViewById<CompanyMemberEditView>(it.fromViewId)
            if (fromView != null) {
                fromView.setImageUrl(finalUrl)
            }

        })
    }


    private fun initView() {
        setMainTitle("注册信息")

        bottom_left_tv.apply {
            text = getString(R.string.save)
            visibility = View.GONE
        }
        bottom_right_tv.text = getString(R.string.commit)
        zero_choice_name.setTitleAndHint(getString(R.string.zero_company_name), getString(R.string.no_less_than_3_word))
        first_choice_name.setTitleAndHint(getString(R.string.first_company_name), getString(R.string.no_less_than_3_word))
        second_choice_name.setTitleAndHint(getString(R.string.second_company_name), getString(R.string.no_less_than_3_word))
        investment_period.setTitleAndHint(getString(R.string.invest_year_num), getString(R.string.invest_year_num_hint))
            .setInputType(InputType.TYPE_CLASS_NUMBER)
        amount_of_funds.setTitleAndHint(getString(R.string.amount_of_fund), getString(R.string.amount_of_fund_hint))
            .setInputType(InputType.TYPE_CLASS_NUMBER)
        bank_number.setTitleAndHint(getString(R.string.bank_card_number), getString(R.string.for_tax_registration))
            .setInputType(InputType.TYPE_CLASS_NUMBER)
        bank_phone.setTitleAndHint(getString(R.string.bank_phone), getString(R.string.for_tax_registration)).setInputType(InputType.TYPE_CLASS_NUMBER)
        detail_addr.setTitleAndHint(getString(R.string.detailed_address), getString(R.string.detailed_address_hint))

        // 法人信息
        legal_person_ev.apply {
            currentMold = Constants.SH1
            initHighlightTitle(getString(R.string.legal_person_info))
            initNameTitleHint(getString(R.string.legal_person_name), getString(R.string.legal_person_name_hint))
            initIdNumberTitleHint(
                getString(R.string.identity_number),
                getString(R.string.identity_number_hint)
            ).setInputType(InputType.TYPE_CLASS_NUMBER)
            initIdAddressTitleHint(getString(R.string.id_address), getString(R.string.id_address_hint))
            initPhoneTitleHint(getString(R.string.contact_number), getString(R.string.contact_number_hint)).setInputType(InputType.TYPE_CLASS_NUMBER)
            initShareRatioTitleHint(getString(R.string.share_ratio), getString(R.string.share_ratio_hint)).setInputType(InputType.TYPE_CLASS_NUMBER)

        }

        // 监事信息
        supervisor_ev.apply {
            currentMold = Constants.SH2
            initHighlightTitle(getString(R.string.supervisor_info))
            initNameTitleHint(getString(R.string.supervisor_name), getString(R.string.supervisor_name_hint))
            initIdNumberTitleHint(
                getString(R.string.identity_number),
                getString(R.string.identity_number_hint)
            ).setInputType(InputType.TYPE_CLASS_NUMBER)
            initIdAddressTitleHint(getString(R.string.id_address), getString(R.string.id_address_hint))
            initPhoneTitleHint(getString(R.string.contact_number), getString(R.string.contact_number_hint)).setInputType(InputType.TYPE_CLASS_NUMBER)
            initShareRatioTitleHint(getString(R.string.share_ratio), getString(R.string.share_ratio_hint_2)).setInputType(InputType.TYPE_CLASS_NUMBER)
        }

        // 默认股东信息
        shareholder_ev.apply {
            currentMold = Constants.SH3
            initHighlightTitle(getString(R.string.shareholder_info2))
            initNameTitleHint(getString(R.string.shareholder_name), getString(R.string.shareholder_name_hint))
            initIdNumberTitleHint(
                getString(R.string.identity_number),
                getString(R.string.identity_number_hint)
            ).setInputType(InputType.TYPE_CLASS_NUMBER)
            initIdAddressTitleHint(getString(R.string.id_address), getString(R.string.id_address_hint))
            initPhoneTitleHint(getString(R.string.contact_number), getString(R.string.contact_number_hint)).setInputType(InputType.TYPE_CLASS_NUMBER)
            initShareRatioTitleHint(getString(R.string.share_ratio), getString(R.string.share_ratio_hint_2)).setInputType(InputType.TYPE_CLASS_NUMBER)
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
            ).setInputType(InputType.TYPE_CLASS_NUMBER)
            initIdAddressTitleHint(getString(R.string.id_address), getString(R.string.id_address_hint))
            initPhoneTitleHint(getString(R.string.contact_number), getString(R.string.contact_number_hint)).setInputType(InputType.TYPE_CLASS_NUMBER)
            initShareRatioTitleHint(getString(R.string.share_ratio), getString(R.string.share_ratio_hint_2)).setInputType(InputType.TYPE_CLASS_NUMBER)

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
                business_scope_value.text = Arrays.toString(selectPidNames.toArray())
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
                    application.getOssSecurityToken(true, isFaceUp, fileDirPath, fromViewId)
                }
            }
            else -> {
            }
        }


    }

    @OnClick(
        R.id.business_scope_rl,
        R.id.bottom_left_tv,
        R.id.bottom_right_tv
    )
    fun onClick(view: View) {
        when (view.id) {
            R.id.business_scope_rl -> {
                // 参照 EnterpriseInfo
                startActivityForResult(BusinessScopeActivity::class.java, 1, null)
            }
            R.id.bottom_left_tv -> {
                // 保存
            }

            R.id.bottom_right_tv -> {
                handlePost()
            }
            else -> {
            }
        }
    }

    private fun handlePost() {

        // 非空校验
        if (!ProductUtils.checkEditEmptyWithVibrate(
                zero_choice_name,
                // 默认信息
                first_choice_name,
                second_choice_name,
                investment_period,
                amount_of_funds,
                bank_number,
                bank_phone,
                detail_addr,
                // 法人
                legal_person_ev.ev_00_name,
                legal_person_ev.ev_01_id_number,
                legal_person_ev.ev_02_id_addr,
                legal_person_ev.ev_03_phone,
                legal_person_ev.ev_04_share_ratio,
                // 监事
                supervisor_ev.ev_00_name,
                supervisor_ev.ev_01_id_number,
                supervisor_ev.ev_02_id_addr,
                supervisor_ev.ev_03_phone,
                supervisor_ev.ev_04_share_ratio,
                // 默认股东 shareholder_ev
                shareholder_ev.ev_00_name,
                shareholder_ev.ev_01_id_number,
                shareholder_ev.ev_02_id_addr,
                shareholder_ev.ev_03_phone,
                shareholder_ev.ev_04_share_ratio
            )
        ) {
            return
        }

        val enterpriseInfo = EnterpriseInfo().apply {
            addr = detail_addr.value()
            area = ""
            bankNo = bank_number.value()
            bankPhone = bank_phone.value()
            businessScope.addAll(ProductUtils.stringList2IntList(selectPid))
            enterpriseName0 = zero_choice_name.value()
            enterpriseName1 = first_choice_name.value()
            enterpriseName2 = second_choice_name.value()
            income = BigDecimal(incomeMoney)
            investMoney = BigDecimal(amount_of_funds.value())
            investYearNum = investment_period.value()
            money = BigDecimal(finalMoney)
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

    override fun addEnterpriseSuccess() {
        ToastUtils.showShort("套餐添加成功")
    }


}