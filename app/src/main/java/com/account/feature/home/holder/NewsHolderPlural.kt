package com.account.feature.home.holder

import android.view.View
import android.view.ViewGroup
import com.account.R
import com.account.app.Glide
import com.account.base.ui.list.BaseItemViewHolder
import com.account.common.TimeUtil
import com.account.entity.home.News
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_news_pural.*
import kotlinx.android.synthetic.main.item_news_pural.date_tv
import kotlinx.android.synthetic.main.item_news_pural.page_views_tv
import kotlinx.android.synthetic.main.item_news_pural.title_tv
import kotlinx.android.synthetic.main.item_news_single.*

/**
 * 资讯
 */
class NewsHolderPlural(parent: ViewGroup?) : BaseItemViewHolder<News>(parent, R.layout.item_news_pural), LayoutContainer {

    override val containerView: View? get() = itemView

    override fun setData(data: News?) {

        if (data == null) {
            return
        }


        title_tv.text = data.title
        date_tv.text = TimeUtil.getTimeLag(data.createDt)
        page_views_tv.text = data.readCount.toString()


        val imgUrls = data.imgUrls
        if (!imgUrls.isNullOrEmpty()) {
            //  TODO url请求
            Glide.with(context).load(imgUrls[0]).apply(
                RequestOptions().transform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundedCornersTransformation(SizeUtils.dp2px(4f), 0, RoundedCornersTransformation.CornerType.TOP)
                    )
                )
            ).into(image_iv)
        }


    }


}
