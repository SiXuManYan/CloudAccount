package com.fatcloud.account.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.AppUtils
import com.fatcloud.account.R
import com.fatcloud.account.app.Glide
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.enterprise.Shareholder
import com.fatcloud.account.feature.matisse.Glide4Engine
import com.fatcloud.account.feature.matisse.Matisse
import com.fatcloud.account.view.dialog.AlertDialog
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhihu.matisse.MimeType
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.layout_highlight_title.view.*
import kotlinx.android.synthetic.main.layout_image_upload.view.*
import kotlinx.android.synthetic.main.view_company_member_edit.view.*

/**
 * Created by Wangsw on 2020/6/11 0001 10:29.
 * 公司相关成员信息编辑页面
 * @see Shareholder
 */
class CompanyMemberEditView : LinearLayout {

    //Reactive收集
    private var compositeDisposable: CompositeDisposable? = null

    /**
     * 添加订阅
     * @param subscription 订阅
     */
    protected fun addSubscribe(subscription: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(subscription)
    }

    /**
     * 相册权限申请
     */
    fun requestAlbumPermissions(activity: Activity): Boolean {
        var isGranted = false
        addSubscribe(
            RxPermissions(activity)
                .request(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .subscribe {
                    isGranted = true
                })
        return isGranted
    }


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


    private fun init() {
        val view = LayoutInflater.from(context).inflate(R.layout.view_company_member_edit, this, true)


        id_card_front_iv.setOnClickListener {
            isFaceUp = true
            handleMediaSelect(context as Activity, 1)
        }
        id_card_back_iv.setOnClickListener {
            isFaceUp = false
            handleMediaSelect(context as Activity, 1)
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


    /**
     * 相册选择
     * @param mediaType
     * @see Matisse.IMG
     * @see Matisse.GIF
     * @see Matisse.VIDEO
     * @see Constants.I1
     * @see Constants.I2
     */
    private fun handleMediaSelect(activity: Activity, mediaType: Int) {

        val isGranted = requestAlbumPermissions(activity)

        if (isGranted) {
            Matisse.from(activity).choose(if (mediaType == 0) MimeType.ofAll() else with(MimeType.ofImage()) {
                remove(MimeType.GIF)
                this
            }, true)
                .countable(true)
//                .originalEnable(false)
                .maxSelectable(1)
                .theme(R.style.Matisse_Dracula)
                .thumbnailScale(0.87f)
                .imageEngine(Glide4Engine())
                .forResult(Constants.REQUEST_MEDIA, mediaType, this.id)

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

    fun loadResultImage(fileDirPath: String) {
        if (isFaceUp) {
            frontImageUrl = fileDirPath
            Glide.with(this).load(fileDirPath).into(getFrontImage())
        } else {
            backImageUrl = fileDirPath
            Glide.with(this).load(fileDirPath).into(getBackImage())
        }


    }


}