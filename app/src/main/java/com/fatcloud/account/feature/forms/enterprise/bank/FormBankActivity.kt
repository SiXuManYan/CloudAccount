package com.fatcloud.account.feature.forms.enterprise.bank

import android.text.InputType
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.event.entity.ImageUploadEvent
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_bank.*

/**
 * Created by Wangsw on 2020/6/15 0015 15:09.
 * </br>
 * 银行对公账户表单
 */
class FormBankActivity : BaseMVPActivity<FormBankPresenter>(), FormBankView {


    override fun getLayoutId() = R.layout.activity_form_bank

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun initViews() {
        initExtra()
        initEvent()
        initView()
    }

    private fun initExtra() {


    }


    private fun initEvent() {
        // 图片上传成功
        presenter.subsribeEventEntity<ImageUploadEvent>(Consumer {

            if (it.formWhichClass != this.javaClass) {
                return@Consumer
            }
//            val finalUrl = it.finalUrl
//            val fromView = findViewById<CompanyMemberEditView>(it.fromViewId)
//            if (fromView != null) {
//                fromView.setImageUrl(finalUrl)
//            }

        })
    }


    private fun initView() {
        setMainTitle("开立银行对公账户")

        // 公司名称
        company_name.setTitleAndHint("公司名称", "请输入公司名称")
        company_address.setTitleAndHint("公司地址", "请输入公司地址")
        postcode.setTitleAndHint("邮编", "请输入邮编")
        registered_capital.setTitleAndHint("注册资金", "请输入注册资金(万元)")

        // 对账服务

        reconciliation_name.setTitleAndHint("对账联系人", "请输入对账联系人姓名")
        reconciliation_phone.setTitleAndHint("对账联系方式", "请输入对账联系电话")

        // 对账单收货地址
        detail_addr.setTitleAndHint("详细地址", "请输入详细地址")

        // 法人信息
        legal_person_ev.apply {

            currentMold = Constants.SH1
            initHighlightTitle(getString(R.string.legal_person_info))

            initNameTitle(getString(R.string.legal_person_name)).setEditAble(false)
            initIdNumberTitle(getString(R.string.identity_number)).setEditAble(false)
            initIdAddressTitle(getString(R.string.id_expiration_date)).setEditAble(false)
            initPhoneTitle(getString(R.string.contact_number)).setEditAble(false)
            initShareRatioTitle(getString(R.string.share_ratio)).setEditAble(false)
        }

        // 财务负责人信息
        supervisor_ev.apply {
            currentMold = Constants.SH4_N
            initHighlightTitle("财务负责人信息")
            initNameTitle("财务负责人姓名").setEditAble(false)
            initIdNumberTitle(getString(R.string.identity_number)).setEditAble(false)
            initPhoneTitle(getString(R.string.contact_number)).setEditAble(false)
            initShareRatioTitle(getString(R.string.share_ratio)).setEditAble(false)
        }

        // 股份信息
        shareholder_ev.apply {
            currentMold = Constants.SH3
            initHighlightTitle("股份信息(法人也需要填写)")
            initNameTitle(getString(R.string.shareholder_name)).setEditAble(false)
            initIdNumberTitle(getString(R.string.identity_number)).setEditAble(false)
            initPhoneTitle(getString(R.string.contact_number)).setEditAble(false)
            initShareRatioTitle(getString(R.string.share_ratio)).setEditAble(false)
        }

//        shareholder_more_container



    }

}