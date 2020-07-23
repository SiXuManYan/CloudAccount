package com.fatcloud.account.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat.startActivityForResult
import com.baidu.ocr.ui.camera.CameraActivity
import com.baidu.ocr.ui.camera.CameraActivity.*
import com.baidu.ocr.ui.util.FileUtil
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.app.Glide
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.enterprise.Shareholder
import com.fatcloud.account.extend.RoundTransFormation
import kotlinx.android.synthetic.main.layout_highlight_title.view.*
import kotlinx.android.synthetic.main.view_company_member_edit.view.*

/**
 * Created by Wangsw on 2020/6/11 0001 10:29.
 * 公司相关成员信息编辑页面，显示页面
 * 可在ImageView 中 直接发起选择图片请求，如  ProductUtils.handleMediaSelect(context as Activity, 1, this.id)
 * </br>
 * 在相应宿主页 的 onActivityResult 方法内，接收相册选择信息回调
 * @see Shareholder
 */
class CompanyMemberEditView : LinearLayout {


    /**
     * 企业股东类型
     * @see Constants.SH1
     * @see Constants.SH2
     * @see Constants.SH3
     */
    var currentMold = ""

    /**
     * 是否是用户手动添加的附加的股东
     */
    var mIsExtra: Boolean? = null

    /**
     * 身份证正面url
     */
    var frontImageUrl = ""

    /**
     * 身份证背面面url
     */
    var backImageUrl = ""

    /**
     * 证件正面
     */
    var isFaceUp = false

    /**
     * 1男
     * 2女
     */
    var genderIndex = 0

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }


    /**
     * todo context 类型校验 : Activity？ fragment
     *
     */
    private fun init() {

        val view =
            LayoutInflater.from(context).inflate(R.layout.view_company_member_edit, this, true)

        id_card_front_iv.setOnClickListener {

            id_card_front_iv.isClickable = false
            id_card_back_iv.isClickable = false

            id_card_front_iv.postDelayed({

                id_card_front_iv.isClickable = true
                id_card_back_iv.isClickable = true

            }, 500)

            isFaceUp = true
            scanIdCard(isFaceUp)
        }

        id_card_back_iv.setOnClickListener {

            id_card_front_iv.isClickable = false
            id_card_back_iv.isClickable = false
            id_card_back_iv.postDelayed({
                id_card_front_iv.isClickable = true
                id_card_back_iv.isClickable = true

            }, 500)
            isFaceUp = false
            scanIdCard(isFaceUp)
        }

        scan_id_card.setOnClickListener {
            isFaceUp = true
            scanIdCard(isFaceUp)

            scan_id_card.postDelayed({
                switcher.displayedChild = 1
            }, 200)

        }

        gender_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val languages = resources.getStringArray(R.array.genderArray)
                if (position != 0) {
                    genderIndex = position
                }
            }

        }

    }

    /**
     *  身份证正面拍照 识别
     */
    private fun scanIdCard(faceUp: Boolean) {

        val intent = Intent(context as Activity, CameraActivity::class.java)
            .putExtra(KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(context).absolutePath)
            .putExtra(
                KEY_CONTENT_TYPE, if (isFaceUp) {
                    CONTENT_TYPE_ID_CARD_FRONT
                } else {
                    CONTENT_TYPE_ID_CARD_BACK
                }
            )
            .putExtra(KEY_FROM_VIEW_ID, this@CompanyMemberEditView.id)

        startActivityForResult(context as Activity, intent, Constants.REQUEST_CODE_CAMERA, null)
    }


    fun initHighlightTitle(highlightTitle: CharSequence) {
        highlight_title_tv.text = highlightTitle
    }

    fun showHighlightDeleteView(): TextView {
        highlight_title_action_tv.visibility = View.VISIBLE
        return highlight_title_action_tv
    }

    fun showAddActionView(): TextView {
        add_action_tv.visibility = View.VISIBLE
        return add_action_tv
    }


    fun initNameTitle(title: CharSequence) {
        ev_00_name_tv.text = title
        ev_00_name_et.hint = "请输入$title"
    }


    /**
     * 设置姓名
     */
    fun setNameValue(value: CharSequence?, editAble: Boolean) {
        value?.let {
            ev_00_name_et.setText(value)
            setEditAble(editAble, ev_00_name_et)
        }
    }

    fun initNameTitleValue(title: CharSequence, value: CharSequence) {
        initNameTitle(title)
        ev_00_name_et.setText(value)
        setEditAble(false, ev_00_name_et)
    }


    fun setEditAble(editAble: Boolean, editText: EditText) {
        editText.apply {
            isFocusable = editAble;
            isFocusableInTouchMode = editAble;
            isEnabled = editAble;
        }
    }


    fun setIdNumberValue(title: CharSequence) {
        ev_01_id_number_et.setText(title)
        setEditAble(false, ev_01_id_number_et)
    }

    fun setIdNumberValue(title: CharSequence?, editAble: Boolean) {
        title?.let {
            ev_01_id_number_et.setText(it)
            setEditAble(editAble, ev_01_id_number_et)
        }

    }


    // 身份证地址
    fun initIdAddressTitleHint(title: CharSequence, hint: CharSequence) {
        ev_02_id_addr_tv.text = title
        ev_02_id_addr_et.hint = hint
    }

    fun hideAddress() {
        address_rl.visibility = View.GONE
    }

    fun initIdAddressHint(hint: CharSequence) {
        ev_02_id_addr_et.hint = hint
    }


    fun initIdAddressTitle(title: CharSequence) {
        ev_02_id_addr_tv.text = title
    }

    fun setIdAddressValue(title: CharSequence) {
        ev_02_id_addr_et.setText(title)
        setEditAble(false, ev_02_id_addr_et)
    }

    fun setIdAddressValue(title: CharSequence?, editAble: Boolean) {
        title?.let {
            ev_02_id_addr_et.setText(it)
            setEditAble(editAble, ev_02_id_addr_et)
        }
    }

    fun setEthnicValue(title: CharSequence?, editAble: Boolean) {
        title?.let {
            nation_et.setText(it)
            setEditAble(editAble, nation_et)
        }
    }

    fun initIdAddressTitleValue(title: CharSequence, value: CharSequence) {
        initIdAddressTitle(title)
        setIdAddressValue(value)

    }

    // 手机号
    fun initPhoneTitleHint(title: CharSequence, hint: CharSequence) {
        ev_03_phone_tv.text = title
        ev_03_phone_et.hint = hint
    }

    fun hidePhone() {
        phone_rl.visibility = View.GONE
    }

    fun showPhone(show: Boolean) {
        phone_rl.visibility = if (show) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    fun initPhoneHint(hint: CharSequence) {
        ev_03_phone_et.hint = hint
    }

    fun initPhoneTitleValue(title: CharSequence, value: CharSequence) {
        initPhoneTitle(title)
        setPhoneValue(value)
        setEditAble(false, ev_03_phone_et)
    }

    fun initPhoneTitle(title: CharSequence) {
        ev_03_phone_tv.text = title
    }

    fun setPhoneValue(hint: CharSequence) {
        ev_03_phone_et.setText(hint)
    }


    fun setPhoneValue(title: CharSequence, editAble: Boolean) {
        title?.let {
            ev_03_phone_et.setText(title)
            setEditAble(editAble, ev_03_phone_et)
        }
    }


    // 股份占比
    fun hideShareRatio() {
        share_ratio_rl.visibility = View.GONE
    }

    fun initShareRatioHint(hint: CharSequence) {
        ev_04_share_ratio_et.hint = hint
    }

    /**
     * 设置股份占比值
     */
    fun initShareRatioValue(value: CharSequence) {

        ev_04_share_ratio_et.setText(value)
        ev_04_share_ratio_et.apply {
            isFocusable = false
            isFocusableInTouchMode = false
            isEnabled = false
        }

    }


    fun getNameValue() = ev_00_name_et.text.toString().trim()
    fun getIdNumberValue() = ev_01_id_number_et.text.toString().trim()
    fun getIdAddressValue() = ev_02_id_addr_et.text.toString().trim()
    fun getPhoneValue() = ev_03_phone_et.text.toString().trim()

    /**
     * 股份占比
     */
    fun getShareRatioValue() = ev_04_share_ratio_et.text.toString().trim()

    /**
     * 设置股份占比
     */
    fun setShareRatioValue(text: CharSequence?, editAble: Boolean) {

        text?.let {

            ev_04_share_ratio_et.setText(it)
            setEditAble(editAble, ev_04_share_ratio_et)
        }


    }


    /**
     * 获取民族
     */
    fun getNationValue() = nation_et.text.toString().trim()

//    /**
//     * 性别
//     */
//    fun getGenderValue() = gender_et.text.toString().trim()

    fun showGenderView(show: Boolean) {
        gender_rl.visibility = if (show) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    fun getFrontImage(): ImageView = id_card_front_iv
    fun getBackImage(): ImageView = id_card_back_iv

    fun getImageUrls(): ArrayList<IdentityImg> {
        return ArrayList<IdentityImg>().apply {
            add(IdentityImg(imgUrl = frontImageUrl, mold = Constants.I1))
            add(IdentityImg(imgUrl = backImageUrl, mold = Constants.I2))
        }
    }


    /**
     * 返回所有包含当前所有信息的实体
     */
    fun getShareHolder(forSaveDraft: Boolean? = false): Shareholder {
        return Shareholder(
            idno = getIdNumberValue(),
            idnoAddr = getIdAddressValue(),
            imgs = getImageUrls(),
            mold = currentMold,
            name = getNameValue(),
            phone = getPhoneValue(),
            shareProportion = getShareRatioValue(),
            idnoDate = getExpiryDateValue(),

            isExtra = if (forSaveDraft != null && forSaveDraft) {
                mIsExtra
            } else {
                null
            }
        )
    }


    /**
     * 上传图片是加载本地路径图片
     */
    fun loadResultImage(fileDirPath: String) {
        if (isFaceUp) {

            Glide.with(this).load(fileDirPath)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(getFrontImage())
        } else {
            Glide.with(this).load(fileDirPath)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(getBackImage())
        }
    }

    fun setImageUrl(finalUrl: String) {
        if (isFaceUp) {
            frontImageUrl = finalUrl
        } else {
            backImageUrl = finalUrl
        }
    }


    fun setServerImageWithOutDisplay(images: List<IdentityImg>) {
        if (images.isNullOrEmpty()) {
            return
        }
        images.forEach {
            when (it.mold) {
                Constants.I1 -> {
                    frontImageUrl = it.imgUrl
                }
                Constants.I2 -> {
                    backImageUrl = it.imgUrl
                }
                else -> {
                }
            }

        }


    }


    fun hideUploadTagImage() {
        id_card_front_tag_iv.visibility = View.GONE
        id_card_back_tag_iv.visibility = View.GONE
    }

    /**
     * 加载服务器图片地址
     */
    fun setServerImage(images: ArrayList<IdentityImg>) {
        if (images.isNullOrEmpty()) {
            return
        }


        displayImageSwitcher()

        images.forEach {

            val sourceImageUrl = it.imgUrl
            if (!sourceImageUrl.isNullOrBlank()) {
                if (ProductUtils.isOssSignUrl(sourceImageUrl)) {
                    ProductUtils.getRealOssUrl(
                        context,
                        sourceImageUrl,
                        object : CloudAccountApplication.OssSignCallBack {
                            override fun ossUrlSignEnd(url: String) {

                                Glide.with(this@CompanyMemberEditView)
                                    .load(url)
                                    .apply(
                                        RequestOptions().transform(
                                            MultiTransformation(
                                                CenterCrop(),
                                                RoundTransFormation(context, 4)
                                            )
                                        )
                                    )
                                    .error(R.drawable.ic_error_image_load)
                                    .into(
                                        when (it.mold) {
                                            Constants.I1 -> getFrontImage()
                                            else -> getBackImage()
                                        }
                                    )


                            }

                        })


                } else {
                    Glide.with(this)
                        .load(sourceImageUrl)
                        .apply(
                            RequestOptions().transform(
                                MultiTransformation(
                                    CenterCrop(),
                                    RoundTransFormation(context, 4)
                                )
                            )
                        )
                        .error(R.drawable.ic_error_image_load)
                        .into(
                            when (it.mold) {
                                Constants.I1 -> getFrontImage()
                                else -> getBackImage()
                            }
                        )
                }


            } else {
                when (it.mold) {
                    Constants.I1 -> {
                        id_card_front_iv.setImageResource(R.drawable.ic_upload_default)
                    }
                    else -> {
                        id_card_back_iv.setImageResource(R.drawable.ic_upload_default)
                    }
                }
            }




            when (it.mold) {
                Constants.I1 -> {
                    frontImageUrl = sourceImageUrl
                }
                Constants.I2 -> {
                    backImageUrl = sourceImageUrl
                }

            }
        }


    }

    fun disableImageViewClick() {
        id_card_front_iv.isClickable = false
        id_card_back_iv.isClickable = false
    }

    fun displayImageSwitcher() {
        switcher.displayedChild = 1
    }


    fun hideBottomSplit() {
        bottom_split_view.visibility = View.GONE
    }

    /**
     * 民族
     */
    fun showNation() {
        nation_rl.visibility = View.VISIBLE
    }

    /**
     * 显示民族
     */
    fun showNation(boolean: Boolean) {

        nation_rl.visibility = if (boolean) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    fun showIdNumber(boolean: Boolean) {

        id_number_rl.visibility = if (boolean) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }


    /**
     * 是否需要识别身份证背面
     */
    var scanIdCardBack = false

    /**
     * 显示身份证有效期
     */
    fun showIdExpirationDate() {
        scanIdCardBack = true
        id_expiration_date_rl.visibility = View.VISIBLE
    }

    /**
     * 显示身份证有效期
     */
    fun showIdExpirationDate(boolean: Boolean) {

        id_expiration_date_rl.visibility = if (boolean) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }


    /**
     * 设置身份证有效期
     */
    fun setExpiryDateValue(expiryDate: String, editAble: Boolean) {
        id_expiration_date_et.setText(expiryDate)

        if (!editAble) {
            setEditAble(false, id_expiration_date_et)
        }
    }

    fun getExpiryDateValue(): String {
        return id_expiration_date_et.text.toString().trim()
    }

    fun setGenderValue(it: Int) {
        if (it == 1) {
            gender_spinner.setSelection(1, true)
        } else {
            gender_spinner.setSelection(2, true)
        }
    }

    fun setNationValue(it: String) {
        nation_et.setText(it)
    }


    fun checkParams() :Boolean{

        if (frontImageUrl.isBlank()) {
            ToastUtils.showShort("请上传身份证正面照片")
            return false
        }
        if (backImageUrl.isBlank()) {
            ToastUtils.showShort("请上传身份证背面照片")
            return false
        }

        if (name_rl.visibility == View.VISIBLE && getNameValue().isBlank()) {
            ToastUtils.showShort("请输姓名")
            return false
        }

        if (gender_rl.visibility == View.VISIBLE && genderIndex == 0) {
            ToastUtils.showShort("请选择性别")
            return false
        }


        if (nation_rl.visibility == View.VISIBLE && getNationValue().isBlank()) {
            ToastUtils.showShort("请输入民族")
            return false
        }

        if (id_number_rl.visibility == View.VISIBLE && getIdNumberValue().isBlank()) {
            ToastUtils.showShort("请输入身份证号")
            return false
        }
        if (id_expiration_date_rl.visibility == View.VISIBLE && getExpiryDateValue().isBlank()) {
            ToastUtils.showShort("请输入身份证号有效期")
            return false
        }

        if (address_rl.visibility == View.VISIBLE && getIdAddressValue().isBlank()) {
            ToastUtils.showShort("请输入身份证地址")
            return false
        }

        if (phone_rl.visibility == View.VISIBLE && getPhoneValue().isBlank()) {
            ToastUtils.showShort("请输入联系电话")
            return false
        }

        if (share_ratio_rl.visibility == View.VISIBLE && getShareRatioValue().isBlank()) {
            ToastUtils.showShort("请输入股份占比")
            return false
        }

        return true


    }


}