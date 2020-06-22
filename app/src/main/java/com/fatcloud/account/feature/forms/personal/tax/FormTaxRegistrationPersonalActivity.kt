package com.fatcloud.account.feature.forms.personal.tax

import android.content.Intent
import android.text.InputType
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
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.event.entity.ImageUploadEvent
import com.fatcloud.account.feature.defray.prepare.PayPrepareActivity
import com.fatcloud.account.feature.matisse.Matisse
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_tax_registration_personal.*

/**
 * Created by Wangsw on 2020/6/13 0013 13:47.
 * </br>
 * 个体户税务登记
 */
class FormTaxRegistrationPersonalActivity : BaseMVPActivity<FormTaxRegistrationPersonalPresenter>(), FormTaxRegistrationPersonalView {


    /**
     * 最终需支付金额
     */
    private var finalMoney: String = ""

    /**
     * 是否额外选择了刻章业务
     */
    private var addSeal: Boolean = false


    /**
     * 产品id
     */
    private var mProductId: String = "0"

    /**
     * 选中的产品价格id
     */
    private var mProductPriceId: String = "0"

    /**
     * 地址
     */
    private var address: String = ""

    /**
     * 城市编码
     */
    private var areaId: String = ""
    private var areaName: String = ""

    /**
     * 图片地址
     */
    private var businessLicenseImgUrl: String = ""


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

        intent.extras!!.getBoolean(Constants.PARAM_ADD_SEAL, false).let {
            addSeal = it
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
            businessLicenseImgUrl = it.finalUrl

        })
    }

    private fun initView() {
        setMainTitle("办理信息")
        trn_ev.setTitleAndHint(R.string.taxpayer_registration_number, R.string.taxpayer_registration_number_hint)
            .setInputType(InputType.TYPE_CLASS_NUMBER)
        legal_name.setTitleAndHint(R.string.legal_person_name, R.string.legal_person_name)
        id_number.setTitleAndHint(R.string.identity_number, R.string.identity_number_hint)
        bank_number.setTitleAndHint(R.string.bank_card_number, R.string.bank_card_number_hint).setInputType(InputType.TYPE_CLASS_NUMBER)
        bank_phone.setTitleAndHint(R.string.bank_phone, R.string.bank_phone_hint).setInputType(InputType.TYPE_CLASS_NUMBER)

        addr_rl.visibility = View.VISIBLE
        detail_addr.setTitleAndHint(R.string.detailed_address, R.string.detailed_address).visibility = View.VISIBLE


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
                    val fromViewId = data.getIntExtra(Matisse.MEDIA_FROM_VIEW_ID, 0)
                    if (fromViewId != 0) {
                        val fromView = findViewById<ImageView>(fromViewId)
                        if (fromView != null) {
                            Glide.with(this).load(fileDirPath).into(fromView)
                        }
                    }
                    val application = application as CloudAccountApplication
                    application.getOssSecurityToken(true, true, fileDirPath, fromViewId, this@FormTaxRegistrationPersonalActivity.javaClass)
                }
            }
            else -> {
            }
        }


    }

    @OnClick(
        R.id.commit_tv,
        R.id.id_card_front_iv,
        R.id.addr_rl
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.commit_tv -> {
                handleCommit()
            }
            R.id.id_card_front_iv -> {
                ProductUtils.handleMediaSelect(this, Matisse.IMG, view.id)
            }
            R.id.addr_rl -> {
                ProductUtils.showLocationPicker(this, object : OnCityItemClickListener() {
                    override fun onSelected(province: ProvinceBean, city: CityBean, district: DistrictBean) {
                        address = StringUtils.getString(R.string.location_information_format, province.name, city.name, district.name)
                        addr_value.text = address
                        areaId = district.id
                    }

                    override fun onCancel() = Unit
                })
            }
            else -> {
            }
        }
    }

    private fun handleCommit() {

        if (!ProductUtils.checkEditEmptyWithVibrate(trn_ev, legal_name, id_number, bank_number, bank_phone)) {
            return
        }
        if (TextUtils.isEmpty(businessLicenseImgUrl)) {
            ToastUtils.showShort("请上传营业执照副本")
        }


        presenter.addLicensePersonal(
            lifecycle = this,
            money = finalMoney,
            productId = mProductId,
            productPriceId = mProductPriceId,
            taxpayerNo = trn_ev.value(),
            legalPersonName = legal_name.value(),
            idno = id_number.value(),
            bankNo = bank_number.value(),
            phoneOfBank = bank_phone.value(),
            businessLicenseImgUrl = businessLicenseImgUrl,
            addr = detail_addr.value(),
            area = address
        )
    }

    override fun commitSuccess(preparePay: PreparePay) {
        ToastUtils.showShort("提交成功")
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