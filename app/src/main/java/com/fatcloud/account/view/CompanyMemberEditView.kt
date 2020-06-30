package com.fatcloud.account.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import com.baidu.ocr.ui.camera.CameraActivity
import com.baidu.ocr.ui.camera.CameraActivity.*
import com.baidu.ocr.ui.util.FileUtil
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

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
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
            isFaceUp = true
//            ProductUtils.handleMediaSelect(context as Activity, 1, this@CompanyMemberEditView.id)
            scanIdCardFront()
        }

        id_card_back_iv.setOnClickListener {
            isFaceUp = false
//            ProductUtils.handleMediaSelect(context as Activity, 1, this@CompanyMemberEditView.id)
            scanIdCardBack()
        }

        scan_id_card.setOnClickListener {
            isFaceUp = true
            switcher.displayedChild = 1
            scanIdCardFront()
        }


    }

    /**
     *  身份证正面拍照 识别
     */
    private fun scanIdCardFront() {
        val intent = Intent(context as Activity, CameraActivity::class.java)
            .putExtra(KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(context).absolutePath)
            .putExtra(KEY_CONTENT_TYPE, CONTENT_TYPE_ID_CARD_FRONT)
            .putExtra(KEY_FROM_VIEW_ID, this@CompanyMemberEditView.id)

        startActivityForResult(context as Activity, intent, Constants.REQUEST_CODE_CAMERA, null)
    }


    /**
     * 身份证背面拍照识别
     */
    private fun scanIdCardBack() {
        val intent = Intent(context as Activity, CameraActivity::class.java)
            .putExtra(KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(context).absolutePath)
            .putExtra(KEY_CONTENT_TYPE, CONTENT_TYPE_ID_CARD_BACK)
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
    }

    fun setNameValue(value: CharSequence) {
        ev_00_name_et.setText(value)
    }

    fun initNameTitleValue(title: CharSequence, value: CharSequence) {
        initNameTitle(title)
        setNameValue(value)
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


    // 身份证地址
    fun initIdAddressTitleHint(title: CharSequence, hint: CharSequence) {
        ev_02_id_addr_tv.text = title
        ev_02_id_addr_et.hint = hint
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

    fun initIdAddressTitleValue(title: CharSequence, value: CharSequence) {
        initIdAddressTitle(title)
        setIdAddressValue(value)

    }

    // 手机号
    fun initPhoneTitleHint(title: CharSequence, hint: CharSequence) {
        ev_03_phone_tv.text = title
        ev_03_phone_et.hint = hint
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
        ev_03_phone_et.hint = hint
    }


    // 股份占比
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
    fun getShareHolder(): Shareholder {
        return Shareholder(
            idno = getIdNumberValue(),
            idnoAddr = getIdAddressValue(),
            imgs = getImageUrls(),
            mold = currentMold,
            name = getNameValue(),
            phone = getPhoneValue(),
            shareProportion = getShareRatioValue()
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


    /**
     * 加载服务器图片地址
     */
    fun setServerImage(images: ArrayList<IdentityImg>) {
        if (images.isNullOrEmpty()) {
            return
        }

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

}