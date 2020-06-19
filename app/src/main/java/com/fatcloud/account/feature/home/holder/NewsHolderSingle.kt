package com.fatcloud.account.feature.home.holder

import android.view.View
import android.view.ViewGroup
import com.fatcloud.account.R
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.list.BaseItemViewHolder
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.TimeUtil
import com.fatcloud.account.entity.news.News
import com.fatcloud.account.extend.RoundTransFormation
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_news_single.*
import kotlinx.android.synthetic.main.item_news_single.date_tv
import kotlinx.android.synthetic.main.item_news_single.page_views_tv
import kotlinx.android.synthetic.main.item_news_single.title_tv
import kotlinx.android.synthetic.main.item_news_single.top_tv

/**
 * 资讯 ，单张图
 */
class NewsHolderSingle(parent: ViewGroup?) : BaseItemViewHolder<News>(parent, R.layout.item_news_single), LayoutContainer {

    override val containerView: View? get() = itemView

    override fun setData(data: News?) {

        if (data == null) {
            return
        }


        title_tv.text = data.title
        date_tv.text = TimeUtil.getFormatTimeYMD(data.createDt)
        page_views_tv.text = data.readCount.toString()

        if (data.recommandFlag == 1) {
            top_tv.visibility = View.VISIBLE
        } else {
            top_tv.visibility = View.GONE
        }


        val imgUrls = data.imgUrls
        if (!imgUrls.isNullOrEmpty()) {
            //  TODO url请求
            Glide.with(context)
                .load(imgUrls[0])
                .apply(
                    RequestOptions().transform(
                        MultiTransformation(
                            CenterCrop(),
                            RoundTransFormation(context, 4)
                        )
                    )
                )
                .error(R.drawable.ic_error_image_load)
                .into(image_iv)
            image_iv.visibility = View.VISIBLE
        } else {
            image_iv.visibility = View.GONE
        }


    }


}
