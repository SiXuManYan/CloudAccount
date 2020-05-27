package com.account.feature.home.holder

import android.view.View
import android.view.ViewGroup
import com.account.R
import com.account.R2.drawable.ic_error_image_load
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
import kotlinx.android.synthetic.main.item_news_single.*

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
        date_tv.text = TimeUtil.getTimeLag(data.createDt)
        page_views_tv.text = data.readCount.toString()


        val imgUrls = data.imgUrls
        if (!imgUrls.isNullOrEmpty()) {
            //  TODO url请求
            Glide.with(context)
                .load(imgUrls[0])
                .apply(
                    RequestOptions().transform(
                        MultiTransformation(
                            CenterCrop(),
                            RoundedCornersTransformation(SizeUtils.dp2px(4f), 0, RoundedCornersTransformation.CornerType.TOP)
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