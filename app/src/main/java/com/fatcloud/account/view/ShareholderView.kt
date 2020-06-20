package com.fatcloud.account.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
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
import kotlinx.android.synthetic.main.view_shareholder.view.*

/**
 * Created by Wangsw on 2020/6/9 0001 10:29.
 * 股权人信息view
 * @see Shareholder
 */
class ShareholderView : LinearLayout {


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
        val view = LayoutInflater.from(context).inflate(R.layout.view_shareholder, this, true)

    }

    public fun setShareHolderView(data: Shareholder) {

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
                title_tv.text = context.getString(R.string.shareholder_info)
                name_title_tv.text = context.getString(R.string.shareholder_name)
            }
        }
        name_tv.text = data.name
        id_number_tv.text = data.idno
        id_address_tv.text = data.idnoAddr
        phone_tv.text = data.phone
        share_ratio_tv.text = "${data.shareProportion}%"


        data.imgs.forEachIndexed { index, identityImg ->

            val imgUrl = identityImg.imgUrl
            if (!imgUrl.isNullOrEmpty()) {

                if (ProductUtils.isOssSignUrl(imgUrl)) {
                    ProductUtils.getRealOssUrl(context, imgUrl, object : CloudAccountApplication.OssSignCallBack {
                        override fun ossUrlSignEnd(url: String) {

                            Glide.with(this@ShareholderView)
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
                                    if (index == 0) {
                                        id_card_front_iv
                                    } else {
                                        id_card_obverse_iv
                                    }
                                )


                        }

                    })

                } else {
                    Glide.with(this@ShareholderView)
                        .load(imgUrl)
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
                            if (index == 0) {
                                id_card_front_iv
                            } else {
                                id_card_obverse_iv
                            }
                        )
                }


            }


        }
    }

}