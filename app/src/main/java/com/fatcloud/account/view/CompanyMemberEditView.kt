package com.fatcloud.account.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.load.MultiTransformation
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
import kotlinx.android.synthetic.main.layout_image_upload.view.*
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

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init()
    }


    /**
     * todo context 类型校验 : Activity？ fragment
     *
     */
    private fun init() {

        val view = LayoutInflater.from(context).inflate(R.layout.view_company_member_edit, this, true)

        id_card_front_iv.setOnClickListener {
            isFaceUp = true
            ProductUtils.handleMediaSelect(context as Activity, 1, this@CompanyMemberEditView.id)
        }
        id_card_back_iv.setOnClickListener {
            isFaceUp = false
            ProductUtils.handleMediaSelect(context as Activity, 1, this@CompanyMemberEditView.id)
        }

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


    // 姓名
    fun initNameTitleHint(title: CharSequence, hint: String) = ev_00_name.setTitleAndHint(title, hint)
    fun initNameTitle(title: CharSequence) = ev_00_name.setTitle(title)
    fun setNameValue(value: CharSequence) = ev_00_name.setValue(value)
    fun initNameTitleValue(title: CharSequence, value: CharSequence): EditView {
        initNameTitle(title)
        setNameValue(value)
        ev_00_name.setEditAble(false)
        return ev_00_name
    }

    // 身份证号
    fun initIdNumberTitleHint(title: CharSequence, hint: CharSequence): EditView {
        ev_01_id_number.setTitleAndHint(title, hint)
        return ev_01_id_number
    }

    /**
     * 设置身份证号 值，设置不可编辑
     */
    fun initIdNumberTitleValue(title: CharSequence, value: CharSequence): EditView {
        initIdNumberTitle(title)
        setIdNumberValue(value)
        ev_01_id_number.setEditAble(false)
        return ev_01_id_number
    }

    fun initIdNumberTitle(title: CharSequence): EditView {
        ev_01_id_number.setTitle(title)
        return ev_01_id_number
    }

    fun setIdNumberValue(title: CharSequence): EditView {
        ev_01_id_number.setValue(title)
        return ev_01_id_number
    }


    // 身份证地址
    fun initIdAddressTitleHint(title: CharSequence, hint: CharSequence) = ev_02_id_addr.setTitleAndHint(title, hint)
    fun initIdAddressTitle(title: CharSequence) = ev_02_id_addr.setTitle(title)
    fun setIdAddressValue(title: CharSequence) = ev_02_id_addr.setValue(title)
    fun initIdAddressTitleValue(title: CharSequence, value: CharSequence): EditView {
        initIdAddressTitle(title)
        setIdAddressValue(value)
        ev_02_id_addr.setEditAble(false)
        return ev_02_id_addr
    }

    // 手机号
    fun initPhoneTitleHint(title: CharSequence, hint: CharSequence): EditView {
        ev_03_phone.setTitleAndHint(title, hint)
        return ev_03_phone
    }

    fun initPhoneTitleValue(title: CharSequence, value: CharSequence): EditView {
        initPhoneTitle(title)
        setPhoneValue(value)
        ev_03_phone.setEditAble(false)
        return ev_03_phone
    }

    fun initPhoneTitle(title: CharSequence): EditView {
        ev_03_phone.setTitle(title)
        return ev_03_phone
    }

    fun setPhoneValue(title: CharSequence): EditView {
        ev_03_phone.setValue(title)
        return ev_03_phone
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


    fun getNameValue() = ev_00_name.value()
    fun getIdNumberValue() = ev_01_id_number.value()
    fun getIdAddressValue() = ev_02_id_addr.value()
    fun getPhoneValue() = ev_03_phone.value()

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
            Glide.with(this).load(fileDirPath).into(getFrontImage())
        } else {
            Glide.with(this).load(fileDirPath).into(getBackImage())
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
                    ProductUtils.getRealOssUrl(context, sourceImageUrl, object : CloudAccountApplication.OssSignCallBack {
                        override fun ossUrlSignEnd(url: String) {

                            Glide.with(this@CompanyMemberEditView)
                                .load(url)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
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
                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
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