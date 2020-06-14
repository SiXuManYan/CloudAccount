package com.fatcloud.account.feature.forms.personal.license

import android.content.DialogInterface
import android.content.Intent
import android.text.InputType
import android.text.TextUtils
import android.view.View
import butterknife.OnClick
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ToastUtils
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.persional.PersonalInfo
import com.fatcloud.account.event.entity.ImageUploadEvent
import com.fatcloud.account.feature.extra.BusinessScopeActivity
import com.fatcloud.account.feature.matisse.Glide4Engine
import com.fatcloud.account.feature.matisse.Matisse
import com.fatcloud.account.view.dialog.AlertDialog
import com.zhihu.matisse.MimeType
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_form_license_enterprise.*
import kotlinx.android.synthetic.main.activity_form_license_personal.*
import kotlinx.android.synthetic.main.activity_form_license_personal.amount_of_funds
import kotlinx.android.synthetic.main.activity_form_license_personal.business_scope_value
import kotlinx.android.synthetic.main.activity_form_license_personal.detail_addr
import kotlinx.android.synthetic.main.activity_form_license_personal.employees_number_tv
import kotlinx.android.synthetic.main.activity_form_license_personal.first_choice_name
import kotlinx.android.synthetic.main.activity_form_license_personal.second_choice_name
import kotlinx.android.synthetic.main.activity_form_license_personal.zero_choice_name
import kotlinx.android.synthetic.main.layout_bottom_action.*
import kotlinx.android.synthetic.main.layout_image_upload.*
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Wangsw on 2020/6/12 0012 13:33.
 * </br>
 * 个体户营业执照表单
 * @see Constants.P1
 * todo imgs 图片上传
 */
class FormLicensePersonalActivity : BaseMVPActivity<FormLicensePersonalPresenter>(), FormLicensePersonalView {


    /**
     * 用户选中的一级经营范围pid
     */
    private var selectPid = ArrayList<String>()

    /**
     * 用户选中的一级经营范围pid名称
     */
    private var selectPidNames = ArrayList<String>()

    /**
     * 用户选中的组成形式id
     */
    private var selectFormId = "0"

    /**
     * 用户选中的一级经营范围pid名称
     */
    private var selectFormIdName = ""


    /**
     * 最终需支付金额
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


    var mediaType = 0

    var identityImg: ArrayList<IdentityImg> = ArrayList()

    /**
     * 正面
     */
    var isFaceUp = false
    var faceUpUrl = ""
    var faceDownUrl = ""


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
            finalMoney = it
        }

        intent.extras!!.getString(Constants.PARAM_PRODUCT_PRICE_ID)?.let {
            mProductPriceId = it
        }

    }

    private fun initEvent() {
        // 签字版
        presenter.subsribeEventEntity<ImageUploadEvent>(Consumer {
            val finalUrl = it.finalUrl
            if (it.isFaceUp) {
                faceUpUrl = finalUrl
            } else {
                faceDownUrl = finalUrl
            }

        })
    }

    private fun initView() {
        setMainTitle("注册人信息")
        bottom_left_tv.text = getString(R.string.save)
        bottom_right_tv.text = getString(R.string.commit)
        nation_ev.setTitleAndHint("民族", "请输入民族")
        detail_addr.setTitleAndHint(getString(R.string.detailed_address), getString(R.string.detailed_address_hint))
        phone.setTitleAndHint("联系方式", "请输入联系方式").setInputType(InputType.TYPE_CLASS_NUMBER)
        id_number.setTitleAndHint(getString(R.string.identity_number), getString(R.string.identity_number_hint))
            .setInputType(InputType.TYPE_CLASS_NUMBER)
        real_name.setTitleAndHint("真实姓名", "请输入真实姓名")
        zero_choice_name.setTitleAndHint("首选名称", getString(R.string.no_less_than_3_word))
        first_choice_name.setTitleAndHint("备选名称1", getString(R.string.no_less_than_3_word))
        second_choice_name.setTitleAndHint("备选名称2", getString(R.string.no_less_than_3_word))
        employees_number_tv.setTitleAndHint("从业人数", "请输入从业人数").setInputType(InputType.TYPE_CLASS_NUMBER)
        amount_of_funds.setTitleAndHint(getString(R.string.amount_of_fund2), getString(R.string.amount_of_fund_hint2))
            .setInputType(InputType.TYPE_CLASS_NUMBER)

    }


    @OnClick(
        R.id.business_scope_rl,
        R.id.bottom_left_tv,
        R.id.bottom_right_tv,

        R.id.id_card_front_iv,
        R.id.id_card_back_iv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.business_scope_rl -> {
                // 参照 EnterpriseInfo
                startActivityForResult(Intent(this, BusinessScopeActivity::class.java), 1)
            }
            R.id.bottom_left_tv -> {
                // 保存
            }

            R.id.bottom_right_tv -> {
                handlePost()
            }

            R.id.id_card_front_iv -> {
                handleMediaSelect(Matisse.IMG)
                isFaceUp = true
            }
            R.id.id_card_back_iv -> {
                handleMediaSelect(Matisse.IMG)
                isFaceUp = false
            }

            else -> {
            }
        }
    }


    private fun handlePost() {

        if (TextUtils.isEmpty(faceUpUrl)) {
            ToastUtils.showShort("请上传正面证件图片")
            return
        }
        if (TextUtils.isEmpty(faceDownUrl)) {
            ToastUtils.showShort("请上传反面证件图片")
            return
        }

        identityImg.apply {
            clear()
            add(IdentityImg(imgUrl = faceUpUrl, mold = Constants.I1))
            add(IdentityImg(imgUrl = faceDownUrl, mold = Constants.I2))
        }
        val enterpriseInfo = PersonalInfo().apply {
            addr = detail_addr.value()
            area = ""
            businessScope = ProductUtils.stringList2IntList(selectPid)
            capital = BigDecimal(amount_of_funds.value())
            employedNum = employees_number_tv.value()
            form = selectFormId.toInt()
            gender = if (man_sex_rb.isChecked) {
                "1"
            } else {
                "2"
            }
            idno = id_number.value()
            imgs = identityImg
            money = BigDecimal(finalMoney)
            name0 = zero_choice_name.value()
            name1 = first_choice_name.value()
            name2 = second_choice_name.value()
            nation = nation_ev.value()
            productId = mProductId
            productPriceId = mProductPriceId
            realName = real_name.value()
            tel = phone.value()
        }
        presenter.addLicensePersonal(this, enterpriseInfo)
    }

    /**
     * @param mediaType
     * @see Matisse.IMG
     * @see Matisse.GIF
     * @see Matisse.VIDEO
     * @see Constants.I1
     * @see Constants.I2
     */
    private fun handleMediaSelect(mediaType: Int) {
        val isGranted = presenter.requestAlbumPermissions(this)
        if (isGranted) {
            Matisse.from(this).choose(if (mediaType == 0) MimeType.ofAll() else with(MimeType.ofImage()) {
                remove(MimeType.GIF)
                this
            }, true)
                .countable(true)
//                .originalEnable(false)
                .maxSelectable(1)
                .theme(R.style.Matisse_Dracula)
                .thumbnailScale(0.87f)
                .imageEngine(Glide4Engine())
                .forResult(Constants.REQUEST_MEDIA, mediaType)
        } else {
            AlertDialog.Builder(context).setTitle(R.string.hint)
                .setMessage(R.string.album_need_permission)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                    AppUtils.launchAppDetailsSettings()
                })
                .setNegativeButton(R.string.no, AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
                .create()
                .show()

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
                // 选中的经营范围
                selectPid = data.getStringArrayListExtra(Constants.PARAM_SELECT_PID)
                selectPidNames = data.getStringArrayListExtra(Constants.PARAM_SELECT_PID_NAME)
                business_scope_value.text = Arrays.toString(selectPidNames.toArray())
            }
            2 -> {
                // 选中的
                selectFormId = data.getStringExtra(Constants.PARAM_SELECT_FORM_PID)
                selectFormIdName = data.getStringExtra(Constants.PARAM_NAME_SELECT_FORM_PID)
                business_scope_value.text = Arrays.toString(selectPidNames.toArray())
            }
            Constants.REQUEST_MEDIA -> {
                // 相册选择图片
                mediaType = data.getIntExtra(Matisse.MEDIA_TYPE, 0)
                val elements = Matisse.obtainPathResult(data)
                if (elements.isNotEmpty()) {
                    val fileDirPath = elements[0]
                    if (isFaceUp) {
                        Glide.with(this).load(fileDirPath).into(id_card_front_iv)
                    } else {
                        Glide.with(this).load(fileDirPath).into(id_card_back_iv)
                    }
                    val application = application as CloudAccountApplication
                    application.getOssSecurityToken(true, isFaceUp, fileDirPath)

                }


            }

            else -> {
            }
        }
    }

    override fun addLicensePersonalSuccess() {
        ToastUtils.showShort("营业执照上传成功")
        finish()
    }


}