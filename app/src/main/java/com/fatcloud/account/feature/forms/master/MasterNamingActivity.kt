package com.fatcloud.account.feature.forms.master

import android.app.DatePickerDialog
import android.content.Intent
import android.view.View
import butterknife.OnClick
import com.blankj.utilcode.util.ToastUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.order.master.MasterNaming
import com.fatcloud.account.feature.defray.prepare.PayPrepareActivity
import com.fatcloud.account.feature.extra.BusinessScopeActivity
import kotlinx.android.synthetic.main.activity_form_master_naming.*
import java.util.*

/**
 * Created by Wangsw on 2020/7/15 0015 10:05.
 * </br>
 * 大师起名
 */
class MasterNamingActivity : BaseMVPActivity<MasterNamingPresenter>(), MasterNamingView {

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
     * 生日
     */
    private var mBirthdayString: String = ""


    /**
     * 用户选中的一级经营范围pid
     */
    private var mSelectPid = ArrayList<String>()

    /**
     * 用户选中的一级经营范围pid名称
     */
    private var mSelectPidNames = ArrayList<String>()


    override fun getLayoutId() = R.layout.activity_form_master_naming

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

    }

    private fun initView() {
        setMainTitle("法人信息")
    }

    @OnClick(
        R.id.commit_tv,
        R.id.business_scope_rl,
        R.id.birthday_tv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.business_scope_rl -> {
                startActivityForResult(
                    Intent(this, BusinessScopeActivity::class.java).putExtra(Constants.PARAM_PRODUCT_TYPE, Constants.P7), Constants.REQUEST_BUSINESS_SCOPE
                )
            }
            R.id.birthday_tv -> {
                selectBirthday()
            }

            R.id.commit_tv -> {
                handleCommit()
            }
            else -> {
            }
        }
    }


    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.REQUEST_BUSINESS_SCOPE -> {
                data?.let {
                    mSelectPid = it.getStringArrayListExtra(Constants.PARAM_SELECT_PID)
                    mSelectPidNames = it.getStringArrayListExtra(Constants.PARAM_SELECT_PID_NAME)
                    business_scope_value.text = Arrays.toString(mSelectPidNames.toArray()).replace("[", "").replace("]", "")
                }
            }
            else -> {

            }
        }

    }

    private fun selectBirthday() {
        val ca = Calendar.getInstance()
        val mYear = ca[Calendar.YEAR]
        val mMonth = ca[Calendar.MONTH]
        val mDay = ca[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            mBirthdayString = getString(R.string.birthday_format, year, (month + 1), dayOfMonth)
            birthday_tv.text = mBirthdayString

        }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }


    private fun handleCommit() {
        val nameValue = legal_name_et.text.toString().trim()
        if (nameValue.isBlank()) {
            ToastUtils.showShort("请输入姓名")
            return
        }
        if (nameValue.length < 2) {
            ToastUtils.showShort("请输入不少于两个字的姓名")
            return
        }

        val phoneValue = legal_phone_et.text.toString().trim()
        if (phoneValue.isBlank()) {
            ToastUtils.showShort("请输入联系方式")
            return
        }


        if (!ProductUtils.isPhoneNumber(phoneValue)) {
            return
        }

        if (mBirthdayString.isBlank()) {
            ToastUtils.showShort("请选择生日")
            return
        }
        if (!personal_rb.isChecked && !enterprise_rb.isChecked) {
            ToastUtils.showShort("请选择经营主体")
            return
        }
        if (mSelectPidNames.isEmpty()) {
            ToastUtils.showShort("请选择经营范围")
            return
        }


        val productStr = product_et.text.toString().trim()
        if (productStr.isBlank()) {
            ToastUtils.showShort("请输入经营产品")
            return
        }
        val remarksValue = remarks_et.text.toString().trim()
        if (remarksValue.isBlank()) {
            ToastUtils.showShort("请输入备注")
            return
        }

        val model = MasterNaming().apply {
            birthday = mBirthdayString
            businessEntity = if (personal_rb.isChecked) {
                "个人"
            } else {
                "企业"
            }
            businessProduct = productStr
            businessScope = mSelectPid
            money = mFinalMoney
            name = nameValue
            phone = phoneValue
            productId = mProductId
            productPriceId = mProductPriceId
            remark = remarksValue
        }


        presenter.addMasterNaming(this, model)
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