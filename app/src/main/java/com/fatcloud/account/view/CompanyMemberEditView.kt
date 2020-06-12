package com.fatcloud.account.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.fatcloud.account.R
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.enterprise.Shareholder
import kotlinx.android.synthetic.main.layout_highlight_title.view.*
import kotlinx.android.synthetic.main.layout_image_upload.view.*
import kotlinx.android.synthetic.main.view_company_member_edit.view.*

/**
 * Created by Wangsw on 2020/6/11 0001 10:29.
 * 公司相关成员信息编辑页面
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

    private fun init() {
        val view = LayoutInflater.from(context).inflate(R.layout.view_company_member_edit, this, true)

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


    fun initNameTitleHint(title: CharSequence, hint: String) = ev_00_name.setTitleAndHint(title, hint)
    fun initIdNumberTitleHint(title: CharSequence, hint: CharSequence): EditView {
        ev_01_id_number.setTitleAndHint(title, hint)
        return ev_01_id_number
    }

    fun initIdAddressTitleHint(title: CharSequence, hint: CharSequence) = ev_02_id_addr.setTitleAndHint(title, hint)
    fun initPhoneTitleHint(title: CharSequence, hint: CharSequence): EditView {
        ev_03_phone.setTitleAndHint(title, hint)
        return ev_03_phone
    }

    fun initShareRatioTitleHint(title: CharSequence, hint: CharSequence): EditView {
        ev_04_share_ratio.setTitleAndHint(title, hint)
        return ev_04_share_ratio
    }

    fun getNameValue() = ev_00_name.value()
    fun getIdNumberValue() = ev_01_id_number.value()
    fun getIdAddressValue() = ev_02_id_addr.value()
    fun getPhoneValue() = ev_03_phone.value()
    fun getShareRatioValue() = ev_04_share_ratio.value()

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

}