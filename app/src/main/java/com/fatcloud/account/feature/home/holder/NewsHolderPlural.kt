package com.fatcloud.account.feature.home.holder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import kotlinx.android.synthetic.main.item_news_pural.*
import java.util.*

/**
 * 资讯 多图
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
            image_container_ll.visibility = View.VISIBLE
            setImageContainer(imgUrls);
        } else {
            image_container_ll.visibility = View.GONE
        }

        if (data.recommandFlag == 1) {
            top_tv.visibility = View.VISIBLE
        } else {
            top_tv.visibility = View.GONE
        }

    }

    private fun setImageContainer(imgUrls: ArrayList<String>) {

        image_container_ll.removeAllViews()
        for ((index, discount) in imgUrls.withIndex()) {
            if (index > 2) {
                break
            }
            val itemView = View.inflate(context, R.layout.item_news_pural_image, null)
            val image_iv = itemView.findViewById<ImageView>(R.id.image_iv)
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
            image_container_ll.addView(itemView)


        }

    }


}
