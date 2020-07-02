package com.fatcloud.account.feature.forms.enterprise.bank.basic

import android.content.Intent
import android.os.Bundle
import android.view.View
import butterknife.OnClick
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.commons.AccountNature
import com.fatcloud.account.event.entity.BankFormCommitSuccessEvent
import com.fatcloud.account.feature.forms.enterprise.bank.FormBankActivity
import com.fatcloud.account.feature.sheet.nature.AccountNatureSheetFragment
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_bank_basic.*

/**
 * Created by Wangsw on 2020/7/1 0001 15:42.
 * </br>
 * 银行表单准备页
 */
class FormBankBasicActivity : BaseMVPActivity<FormBankBasicPresenter>(), FormBankBasicView {


    /**
     * 订单流程id
     */
    var orderWorkId: String? = ""


    override fun getLayoutId() = R.layout.activity_form_bank_basic

    override fun initViews() {
        initExtra()
        initEvent()
        initView()
    }

    private fun initEvent() {
        // 银行对公账户表单添加成功
        presenter.subsribeEventEntity<BankFormCommitSuccessEvent>(Consumer {
            finish()
        })
    }


    private fun initExtra() {
        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_WORK_ID)) {
            finish()
            return
        }
        orderWorkId = intent.extras!!.getString(Constants.PARAM_ORDER_WORK_ID)
    }

    private fun initView() {
        setMainTitle("开立银行对公账户")
    }


    @OnClick(
        R.id.bottom_right_tv,
        R.id.account_nature_rl,
        R.id.recipient_address_rl
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        view.postDelayed({

        }, 200)

        when (view.id) {

            R.id.bottom_right_tv -> {
                ProductUtils.handleDoubleClick(view)
                handleNext()
            }
            R.id.account_nature_rl -> {
                AccountNatureSheetFragment.newInstance().apply {
                    setOnItemSelectListener(object :
                        AccountNatureSheetFragment.OnItemSelectedListener {
                        override fun onItemSelected(currentSelected: AccountNature) {
                            this@FormBankBasicActivity.account_nature_value.text =
                                currentSelected.name
                        }
                    })

                    show(supportFragmentManager, this.tag)
                }
            }
            R.id.recipient_address_rl -> {
                ProductUtils.showLocationPicker(this, object : OnCityItemClickListener() {
                    override fun onSelected(
                        province: ProvinceBean,
                        city: CityBean,
                        district: DistrictBean
                    ) {
                        province_title_tv.text = StringUtils.getString(
                            R.string.location_information_format,
                            province.name,
                            city.name,
                            district.name
                        )
                    }

                    override fun onCancel() = Unit
                })
            }
            else -> {
            }
        }
    }


    private fun handleNext() {

        val companyName = company_name_et.text.toString().trim()
        if (companyName.isBlank()) {
            ToastUtils.showShort("请输入公司名称")
            return
        }

        val companyAddress = company_address_et.text.toString().trim()
        if (companyAddress.isBlank()) {
            ToastUtils.showShort("请输入公司地址")
            return
        }

        val registeredCapital = registered_capital_et.text.toString().trim()
        if (registeredCapital.isBlank()) {
            ToastUtils.showShort("请输入注册资金")
            return
        }

        val accountNatureValue = account_nature_value.text.toString().trim()
        if (accountNatureValue.isBlank()) {
            ToastUtils.showShort("请选择账户性质")
            return
        }

        val reconciliationName = reconciliation_name_et.text.toString().trim()
        if (reconciliationName.isBlank()) {
            ToastUtils.showShort("请输入对账联系人")
            return
        }

        val reconciliationPhone = reconciliation_phone_et.text.toString().trim()
        if (reconciliationPhone.isBlank()) {
            ToastUtils.showShort("请输入对账联系方式")
            return
        }
        if (!ProductUtils.isPhoneNumber(reconciliationPhone, "对账联系人")) {
            return
        }

        val areaName = province_title_tv.text.toString().trim()
        if (areaName.isBlank()) {
            ToastUtils.showShort("请选择省份")
            return
        }

        val detailAddr = detail_addr_et.text.toString().trim()
        if (detailAddr.isBlank()) {
            ToastUtils.showShort("请输入详细地址")
            return
        }


        val bundle = Bundle().apply {
            putString(Constants.PARAM_ORDER_WORK_ID, orderWorkId)
            putString(Constants.PARAM_COMPANY_NAME, companyName)
            putString(Constants.PARAM_COMPANY_ADDRESS, companyAddress)
            putString(Constants.PARAM_REGISTERED_CAPITAL, registeredCapital)
            putString(Constants.PARAM_ACCOUNT_NATURE, accountNatureValue)
            putString(Constants.PARAM_RECONCILIATION_NAME, reconciliationName)
            putString(Constants.PARAM_RECONCILIATION_PHONE, reconciliationPhone)
            putString(Constants.PARAM_AREA_NAME, areaName)
            putString(Constants.PARAM_DETAIL_ADDRESS, detailAddr)
        }
        startActivity(Intent(this, FormBankActivity::class.java).putExtras(bundle))

    }

}