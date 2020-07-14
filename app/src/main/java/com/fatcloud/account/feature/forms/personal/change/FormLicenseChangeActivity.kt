package com.fatcloud.account.feature.forms.personal.change

import android.content.Intent
import android.view.View
import android.widget.ImageView
import butterknife.OnClick
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.persional.PersonalLicenseChange
import com.fatcloud.account.event.entity.ImageUploadEvent
import com.fatcloud.account.feature.defray.prepare.PayPrepareActivity
import com.fatcloud.account.feature.extra.BusinessScopeActivity
import com.fatcloud.account.feature.matisse.Matisse
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_license_change_personal.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Wangsw on 2020/7/10 0010 15:37.
 * </br>
 *  个体户营业执照变更
 *   @see  Constants.P5
 */
class FormLicenseChangeActivity : BaseMVPActivity<FormLicenseChangePresenter>(), FormLicenseChangeView {


    companion object {
        /**
         * 店铺名称变更
         */
        val TYPE_CHANGE_NAME = 0

        /**
         * 经营范围变更
         */
        val TYPE_CHANGE_SCOPE = 1

        /**
         * 店铺名称 + 经营范围变更
         */
        val TYPE_CHANGE_NAME_AND_SCOPE = 2
    }

    var changeType = TYPE_CHANGE_NAME

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
     * 用户选中的一级经营范围pid名称
     */
    private var selectPidNames = ArrayList<String>()


    /**
     * 用户选中的一级经营范围pid
     */
    private var selectPid = ArrayList<String>()


    /**
     * 身份证正反面路径集合
     */
    var mIdImageUrlList: ArrayList<IdentityImg> = ArrayList()

    /**
     * 营业执照正反面路径集合
     */
    var mLicenseImagesUrlList: ArrayList<IdentityImg> = ArrayList()


    private var idImageFrontPath: String = ""
    private var idImageFrontUrl: String = ""
    private var idImageBackPath: String = ""
    private var idImageBackUrl: String = ""

    private var licenseImageFrontPath: String = ""
    private var licenseImageFrontUrl: String = ""
    private var licenseImageBackPath: String = ""
    private var licenseImageBackUrl: String = ""


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId() = R.layout.activity_form_license_change_personal

    override fun initViews() {
        initExtra()
        initEvent()
        initView()
    }

    private fun initView() {
        setMainTitle("办理信息")
        when (changeType) {
            TYPE_CHANGE_NAME -> {
                name_change_ll.visibility = View.VISIBLE
                business_scope_change_rl.visibility = View.GONE
            }

            TYPE_CHANGE_SCOPE -> {
                name_change_ll.visibility = View.GONE
                business_scope_change_rl.visibility = View.VISIBLE
            }

            TYPE_CHANGE_NAME_AND_SCOPE -> {
                name_change_ll.visibility = View.VISIBLE
                business_scope_change_rl.visibility = View.VISIBLE
            }
            else -> {
            }
        }
    }

    private fun initEvent() {
        // 图片上传成功
        presenter.subsribeEventEntity<ImageUploadEvent>(Consumer {

            if (it.formWhichClass != this.javaClass) {
                return@Consumer
            }
            when (it.fromViewId) {
                R.id.id_card_front_iv -> {
                    idImageFrontUrl = it.finalUrl
                }
                R.id.id_card_back_iv -> {
                    idImageBackUrl = it.finalUrl
                }
                R.id.id_license_front_iv -> {
                    licenseImageFrontUrl = it.finalUrl
                }
                R.id.id_license_back_iv -> {
                    licenseImageBackUrl = it.finalUrl
                }
                else -> {
                }
            }
        })
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

        intent.extras!!.getInt(Constants.PARAM_CHANGE_TYPE, 0).let {
            changeType = it
        }


    }

    @OnClick(
        R.id.commit_tv,
        R.id.id_card_front_iv,
        R.id.id_card_back_iv,
        R.id.business_scope_change_rl,
        R.id.id_license_front_iv,
        R.id.id_license_back_iv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {

            R.id.commit_tv -> {
                handleCommit()
            }
            R.id.id_card_front_iv,
            R.id.id_card_back_iv,
            R.id.id_license_front_iv,
            R.id.id_license_back_iv -> ProductUtils.handleMediaSelect(this, Matisse.IMG, view.id)
            R.id.business_scope_change_rl -> {
                startActivityForResult(Intent(this, BusinessScopeActivity::class.java), Constants.REQUEST_BUSINESS_SCOPE)
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

            Constants.REQUEST_MEDIA -> {
                // 相册选择图片
                val elements = Matisse.obtainPathResult(data)
                if (elements.isNotEmpty()) {
                    val fileDirPath = elements[0]

                    val fromViewId = data.getIntExtra(Matisse.MEDIA_FROM_VIEW_ID, 0)
                    if (fromViewId == 0) {
                        return
                    }

                    when (fromViewId) {
                        R.id.id_card_front_iv -> idImageFrontPath = fileDirPath
                        R.id.id_card_back_iv -> idImageBackPath = fileDirPath
                        R.id.id_license_front_iv -> licenseImageFrontPath = fileDirPath
                        R.id.id_license_back_iv -> licenseImageBackPath = fileDirPath
                        else -> {
                        }
                    }

                    val fromView = findViewById<ImageView>(fromViewId)
                    if (fromView == null) {
                        return
                    }

                    Glide.with(this).load(fileDirPath).diskCacheStrategy(DiskCacheStrategy.NONE).into(fromView)

                    val application = application as CloudAccountApplication
                    application.getOssSecurityToken(true, true, fileDirPath, fromViewId, this@FormLicenseChangeActivity.javaClass)

                }
            }
            Constants.REQUEST_BUSINESS_SCOPE -> {
                // 选中的经营范围
                selectPid = data.getStringArrayListExtra(Constants.PARAM_SELECT_PID)
                selectPidNames = data.getStringArrayListExtra(Constants.PARAM_SELECT_PID_NAME)
                business_scope_value.text =
                    Arrays.toString(selectPidNames.toArray()).replace("[", "").replace("]", "")
            }
            else -> {
            }
        }


    }


    private fun handleCommit() {

        val nameValue = legal_name_et.text.toString().trim()
        if (nameValue.isBlank()) {
            ToastUtils.showShort("请输入法人姓名")
            return
        }
        if (nameValue.length < 2) {
            ToastUtils.showShort("请输入不少于两个字的姓名")
            return
        }

        val phoneStr = legal_phone_et.text.toString().trim()
        if (phoneStr.isBlank()) {
            ToastUtils.showShort("请输入联系方式")
            return
        }

        if (!ProductUtils.isPhoneNumber(phoneStr)) {
            return
        }

        val idNumberValue = id_number_tv_et.text.toString().trim()
        if (idNumberValue.isBlank()) {
            ToastUtils.showShort("身份证号")
            return
        }
        if (!ProductUtils.isIdCardNumber(idNumberValue)) {
            return
        }

        var zeroName = ""
        var firstName = ""
        var secondName = ""

        if (name_change_ll.visibility == View.VISIBLE) {
            zeroName = zero_choice_name_et.text.toString().trim()
            if (zeroName.isBlank()) {
                ToastUtils.showShort("请输入变更首选名称")
                return
            }

            firstName = first_choice_name_et.text.toString().trim()
            if (firstName.isBlank()) {
                ToastUtils.showShort("请输入变更备选名称1")
                return
            }

            secondName = second_choice_name_et.text.toString().trim()
            if (secondName.isBlank()) {
                ToastUtils.showShort("请输入变更备选名称2")
                return
            }
        }

        if (business_scope_change_rl.visibility == View.VISIBLE) {
            if (selectPidNames.isEmpty()) {
                ToastUtils.showShort("请选择经营范围")
                return
            }
        }

        if (idImageFrontPath.isBlank()) {
            ToastUtils.showShort("请上传身份证人像面")
            return
        }
        if (idImageBackPath.isBlank()) {
            ToastUtils.showShort("请上传身份证国徽面")
            return
        }
        if (licenseImageFrontPath.isBlank()) {
            ToastUtils.showShort("请上传营业执照正本")
            return
        }
        if (licenseImageBackPath.isBlank()) {
            ToastUtils.showShort("请上传营业执照副本")
            return
        }

        val model = PersonalLicenseChange().apply {
            if (business_scope_change_rl.visibility == View.VISIBLE) {
                businessScope = selectPid
            }

            if (name_change_ll.visibility == View.VISIBLE) {
                enterpriseName0 = zeroName
                enterpriseName1 = firstName
                enterpriseName2 = secondName
            }
            idno = idNumberValue
            imgsIdno = mIdImageUrlList.apply {
                clear()
                add(IdentityImg(imgUrl = idImageFrontUrl, mold = Constants.I1))
                add(IdentityImg(imgUrl = idImageBackUrl, mold = Constants.I2))
            }
            imgsLicense = mLicenseImagesUrlList.apply {
                clear()
                add(IdentityImg(imgUrl = licenseImageFrontUrl, mold = Constants.I1))
                add(IdentityImg(imgUrl = licenseImageBackUrl, mold = Constants.I2))
            }
            legalPersonName = nameValue
            money = mFinalMoney
            phone = phoneStr
            productId = mProductId
            productPriceId = mProductPriceId
        }

        presenter.addLicenseChangePersonal(this, model)
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