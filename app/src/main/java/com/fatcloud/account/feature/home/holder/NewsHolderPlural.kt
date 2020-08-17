package com.fatcloud.account.feature.home.holder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.fatcloud.account.R
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.list.BaseItemViewHolder
import com.fatcloud.account.common.TimeUtil
import com.fatcloud.account.entity.news.News
import com.fatcloud.account.extend.RoundTransFormation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_news_pural.*
import kotlinx.android.synthetic.main.item_news_pural.date_tv
import kotlinx.android.synthetic.main.item_news_pural.page_views_tv
import kotlinx.android.synthetic.main.item_news_pural.title_tv
import kotlinx.android.synthetic.main.item_news_pural.top_tv
import java.util.*

/**
 * 资讯 多图
 */
class NewsHolderPlural(parent: ViewGroup?) : BaseItemViewHolder<News>(parent, R.layout.item_news_pural), LayoutContainer {

    override val containerView: View? get() = itemView

    private val mWidth = (ScreenUtils.getScreenWidth() - SizeUtils.px2dp(30f)) / 3
    private val mHeight = (mWidth / 1.3f).toInt()  // x:y = 1.3f

    override fun setData(data: News?) {

        if (data == null) {
            return
        }

        title_tv.text = data.title
        date_tv.text = TimeUtil.getFormatTimeYMD(data.createDt)
        val readCount = data.readCount
        page_views_tv.text = readCount.toString()


        val imgUrls = data.imgUrls
        if (!imgUrls.isNullOrEmpty()) {
            image_container_ll.visibility = View.VISIBLE
            setImageContainer(imgUrls);
        } else {
            image_container_ll.visibility = View.GONE
        }

        val recommandFlag = data.recommandFlag
        if (recommandFlag == 1 || recommandFlag == 2) {
            top_tv.visibility = View.VISIBLE
            top_tv.text = data.recommandFlagText
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

            val imageView = ImageView(context)

            imageView.layoutParams = LinearLayout.LayoutParams(mWidth, mHeight).apply {
                weight = 1.0f
                when (index) {
                    0, 1 -> {
                        rightMargin = SizeUtils.dp2px(15f)
                    }
                }
            }

            Glide.with(context)
                .load(imgUrls[index])
                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                .error(R.drawable.ic_error_image_load)
                .into(imageView)
            image_container_ll.addView(imageView)

        }

    }


}
