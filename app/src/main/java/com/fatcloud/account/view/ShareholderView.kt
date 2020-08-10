package com.fatcloud.account.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.app.Glide
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.order.enterprise.Shareholder
import com.fatcloud.account.extend.RoundTransFormation
import com.fatcloud.account.feature.gallery.GalleryActivity
import kotlinx.android.synthetic.main.view_shareholder.view.*

/**
 * Created by Wangsw on 2020/6/9 0001 10:29.
 * 股权人信息view
 * @see Shareholder
 */
class ShareholderView : LinearLayout {


    private var frontUrl = ""
    private var backUrl = ""

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        val view = LayoutInflater.from(context).inflate(R.layout.view_shareholder, this, true)

        id_card_front_iv.setOnClickListener {
            lookGallery(frontUrl)
        }

        id_card_obverse_iv.setOnClickListener {
            lookGallery(backUrl)
        }

    }

    public fun setShareHolderView(data: Shareholder, productWorkType: String?) {

        when (data.mold) {
            Constants.SH1 -> {
                title_tv.text = context.getString(R.string.legal_person_info)
                name_title_tv.text = context.getString(R.string.legal_person_name)
            }
            Constants.SH2 -> {
                title_tv.text = context.getString(R.string.supervisor_info)
                name_title_tv.text = context.getString(R.string.supervisor_name)
            }
            Constants.SH3 -> {
                title_tv.text = context.getString(R.string.shareholder_info2)
                name_title_tv.text = context.getString(R.string.shareholder_name)
            }
            Constants.SH4 -> {
                title_tv.text = "财务负责人信息"
                name_title_tv.text = "财务负责人姓名"
            }

        }
        name_tv.text = data.name
        id_number_tv.text = data.idno
        id_address_tv.text = data.idnoAddr
        if (productWorkType == Constants.PW3) {
            id_address_ll.visibility = View.GONE

            val idnoDate = data.idnoDate
            if (!idnoDate.isNullOrBlank()) {
                id_expiry_date_ll.visibility = View.VISIBLE
                id_expiry_date_tv.text = idnoDate
            }
        } else {
            id_address_ll.visibility = View.VISIBLE
            id_expiry_date_ll.visibility = View.GONE
        }
        phone_tv.text = data.phone

        val shareProportion = data.shareProportion
        if (shareProportion.isNullOrBlank()) {
            share_ratio_tv.text = "0%"
        } else {
            share_ratio_tv.text = "$shareProportion%"
        }


        data.imgs.forEachIndexed { index, identityImg ->

            val imgUrl = identityImg.imgUrl
            if (!imgUrl.isNullOrEmpty()) {

                if (ProductUtils.isOssSignUrl(imgUrl)) {
                    ProductUtils.getRealOssUrl(context, imgUrl, object : CloudAccountApplication.OssSignCallBack {
                        override fun ossUrlSignEnd(url: String) {

                            Glide.with(this@ShareholderView)
                                .load(url)
                                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                                .error(R.drawable.ic_error_image_load)
                                .into(
                                    if (index == 0) {
                                        id_card_front_iv
                                    } else {
                                        id_card_obverse_iv
                                    }
                                )

                            if (index == 0) {
                                frontUrl = url
                            } else {
                                backUrl = url
                            }

                        }

                    })

                } else {
                    Glide.with(this@ShareholderView)
                        .load(imgUrl)
                        .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                        .error(R.drawable.ic_error_image_load)
                        .into(
                            if (index == 0) {
                                id_card_front_iv
                            } else {
                                id_card_obverse_iv
                            }
                        )

                    if (index == 0) {
                        frontUrl = imgUrl
                    } else {
                        backUrl = imgUrl
                    }

                }
            }
        }
    }


    private fun lookGallery(url: String) {

        if (url.isBlank()) {
            return
        }

        val imageList = ArrayList<String>()
        imageList.add(url)
        val bundle = Bundle()
        bundle.putStringArrayList(Constants.PARAM_LIST, imageList)
        bundle.putInt(Constants.PARAM_INDEX, 0)
        context.startActivity(Intent(context, GalleryActivity::class.java).putExtras(bundle))

    }

}