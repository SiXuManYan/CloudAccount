package com.fatcloud.account.feature.forms.enterprise.bank

import android.app.Activity
import android.content.Intent
import android.text.InputType
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
import com.fatcloud.account.entity.commons.AccountNature
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo
import com.fatcloud.account.event.RxBus
import com.fatcloud.account.event.entity.BankFormCommitSuccessEvent
import com.fatcloud.account.event.entity.ImageUploadEvent
import com.fatcloud.account.feature.matisse.Matisse
import com.fatcloud.account.feature.sheet.nature.AccountNatureSheetFragment
import com.fatcloud.account.view.CompanyMemberEditView
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_bank.*

/**
 * Created by Wangsw on 2020/6/15 0015 15:09.
 * </br>
 * 银行对公账户表单
 */
class FormBankActivity : BaseMVPActivity<FormBankPresenter>(), FormBankView {


    /**
     * 订单流程id
     */
    var orderWorkId: String? = ""

    /**
     * 营业执照url
     */
    var business_license_url = ""

    /**
     * 电子图章url
     */
    var electronic_seal_url = ""

    /**
     * 签字授权书url
     */
    var signed_authorization_url = ""

    /**
     * 财务负责人身份证正面
     */
    var finance_card_front_url = ""

    /**
     * 财务负责任呢身份证背面
     */
    var finance_card_back_url = ""

    /**
     * 企业的账户性质
     */
    var account_nature = ""

    /**
     * 队长收货单地址区域
     */
    var areaCodeId = ""
    var areaName = ""

    override fun getLayoutId() = R.layout.activity_form_bank

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun initViews() {
        initExtra()
        initEvent()
        initView()
    }

    private fun initExtra() {
        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_WORK_ID)) {
            finish()
            return
        }
        orderWorkId = intent.extras!!.getString(Constants.PARAM_ORDER_WORK_ID)
        presenter.getBankInfo(this, orderWorkId)
    }


    private fun initEvent() {

        // 图片上传成功
        presenter.subsribeEventEntity<ImageUploadEvent>(Consumer {

            if (it.formWhichClass != this.javaClass) {
                return@Consumer
            }
            val finalUrl = it.finalUrl

            val fromViewId = it.fromViewId

            val fromView = findViewById<View>(fromViewId)
            if (fromView == null) {
                return@Consumer
            }

            if (fromView is CompanyMemberEditView) {
                fromView.setImageUrl(finalUrl)

            } else {
                when (fromViewId) {
                    R.id.business_license_iv -> business_license_url = finalUrl
                    R.id.electronic_seal_iv -> electronic_seal_url = finalUrl
                    R.id.signed_authorization_iv -> signed_authorization_url = finalUrl
                    R.id.finance_card_front_iv -> finance_card_front_url = finalUrl
                    R.id.finance_card_back_iv -> finance_card_back_url = finalUrl

                }
            }


        })

    }


    private fun initView() {
        setMainTitle("开立银行对公账户")

        // 公司名称
        company_name.setTitleAndHint("公司名称", "请输入公司名称")
        company_address.setTitleAndHint("公司地址", "请输入公司地址")
        postcode.setTitleAndHint("邮编", "请输入邮编").setInputType(InputType.TYPE_CLASS_NUMBER)
        registered_capital.setTitleAndHint("注册资金(万元)", "请输入注册资金(万元)").setInputType(InputType.TYPE_CLASS_NUMBER)

        // 对账服务
        reconciliation_name.setTitleAndHint("对账联系人", "请输入对账联系人姓名")
        reconciliation_phone.setTitleAndHint("对账联系方式", "请输入对账联系电话").setInputType(InputType.TYPE_CLASS_NUMBER)

        // 对账单收货地址
        detail_addr.setTitleAndHint("详细地址", "请输入详细地址")

        // 财务负责人
        finance_name.setTitleAndHint("财务负责人姓名", "请输入财务负责人姓名")
        finance_id_number.setTitleAndHint("身份证号", "请输入身份证号").setInputType(InputType.TYPE_CLASS_NUMBER)
        finance_phone.setTitleAndHint("联系电话", "请输入联系电话").setInputType(InputType.TYPE_CLASS_NUMBER)
        finance_share_ratio.setTitleAndHint("股份占比", "请输入股份占比 %").setInputType(InputType.TYPE_CLASS_NUMBER)
    }

    override fun bindDetailInfo(data: EnterpriseInfo) {

        // 法人、股东相关信息
        shareholder_more_container.removeAllViews()
        data.shareholders?.forEachIndexed { index, shareholder ->
            shareholder_more_container.addView(presenter.getShareholderView(this, shareholder_more_container, index, shareholder))
        }


    }


    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        when (requestCode) {
            Constants.REQUEST_MEDIA -> handleAlbumSelect(data)
            else -> {
            }
        }


    }

    private fun handleAlbumSelect(data: Intent) {
        // 相册选择图片
        val elements = Matisse.obtainPathResult(data)
        if (elements.isNullOrEmpty()) {
            return
        }

        // 图片的真实路径地址
        val fileDirPath = elements[0]
        val fromViewId = data.getIntExtra(Matisse.MEDIA_FROM_VIEW_ID, 0)
        if (fromViewId == 0) {
            return
        }
        val fromView = findViewById<View>(fromViewId)
        if (fromView == null) {
            return
        }
        if (fromView is CompanyMemberEditView) {
            fromView.loadResultImage(fileDirPath)
        } else if (fromView is ImageView) {
            Glide.with(this).load(fileDirPath).into(fromView)
        }
        val application = application as CloudAccountApplication
        // isFaceUp 朝向随意，CompanyMemberEditView 会自己记录朝向
        application.getOssSecurityToken(
            isEncryptFile = true,
            isFaceUp = false,
            localFilePatch = fileDirPath,
            fromViewId = fromViewId,
            clx = this@FormBankActivity.javaClass
        )
    }


    @OnClick(
        R.id.business_license_iv,
        R.id.electronic_seal_iv,
        R.id.signed_authorization_iv,
        R.id.finance_card_front_iv,
        R.id.finance_card_back_iv,
        R.id.bottom_right_tv,
        R.id.account_nature_rl,
        R.id.recipient_address_rl
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {

            R.id.business_license_iv,
            R.id.electronic_seal_iv,
            R.id.signed_authorization_iv,
            R.id.finance_card_front_iv,
            R.id.finance_card_back_iv
            -> ProductUtils.handleMediaSelect(context as Activity, 1, view.id)
            R.id.bottom_right_tv -> handleCommit()
            R.id.account_nature_rl -> {
                AccountNatureSheetFragment.newInstance().apply {
                    setOnItemSelectListener(object : AccountNatureSheetFragment.OnItemSelectedListener {
                        override fun onItemSelected(currentSelected: AccountNature) {
                            this@FormBankActivity.account_nature = currentSelected.value
                            this@FormBankActivity.business_scope_value.text = currentSelected.name
                        }
                    })

                    show(supportFragmentManager, this.tag)
                }
            }
            R.id.recipient_address_rl -> {
                ProductUtils.showLocationPicker(this, object : OnCityItemClickListener() {
                    override fun onSelected(province: ProvinceBean, city: CityBean, district: DistrictBean) {
                        areaName = StringUtils.getString(R.string.location_information_format, province.name, city.name, district.name)
                        province_title_tv.text = areaName
                        areaCodeId = district.id
                    }

                    override fun onCancel() = Unit
                })
            }
            else -> {
            }
        }
    }

    private fun handleCommit() {
        if (!ProductUtils.checkEditEmptyWithVibrate(
                company_name,
                company_address,
                postcode,
                registered_capital,
                reconciliation_name,
                reconciliation_phone,
                detail_addr,
                finance_name,
                finance_id_number,
                finance_phone,
                finance_share_ratio
            )
        ) {
            return
        }

        if (
            !ProductUtils.checkViewValueEmpty(business_scope_value.text.toString(), business_scope_value) ||
            !ProductUtils.checkViewValueEmpty(province_title_tv.text.toString(), province_title_tv) ||
            !ProductUtils.checkViewValueEmpty(business_license_url, business_license_iv) ||
            !ProductUtils.checkViewValueEmpty(electronic_seal_url, electronic_seal_iv) ||
            !ProductUtils.checkViewValueEmpty(signed_authorization_url, signed_authorization_iv)
        ) {
            return
        }
        presenter.addSpecificProcessContent(
            lifecycleOwner = this,
            orderWorkId = orderWorkId,
            businessLicenseImgUrl = business_license_url,
            capital = registered_capital.value(),
            electronicSealImgUrl = electronic_seal_url,
            enterpriseAddr = company_address.value(),
            enterpriseMold = account_nature,
            enterpriseName = company_name.value(),
            financeIdno = finance_id_number.value(),
            financeIdnoImgUrlA = finance_card_front_url,
            financeIdnoImgUrlB = finance_card_back_url,
            financeName = finance_name.value(),
            financePhone = finance_phone.value(),
            financeShares = finance_share_ratio.value(),
            legalPersonWarrantImgUrl = signed_authorization_url,
            reconciliatAddr = areaName + detail_addr.value(),
            reconciliatArea = areaCodeId,
            reconciliatContact = reconciliation_name.value(),
            reconciliatPhone = reconciliation_phone.value()
        )
    }

    override fun addSuccess() {
        ToastUtils.showShort("添加成功")
        RxBus.post(BankFormCommitSuccessEvent())
        finish()
    }


}