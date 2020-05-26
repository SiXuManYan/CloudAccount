package com.account.feature.home.header

import android.view.View
import android.view.ViewGroup
import com.account.R
import com.account.app.Glide
import com.account.base.ui.list.BaseItemViewHolder
import com.account.entity.home.Product
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_hot_product.*

/**
 * 热门产品
 */
class HotProductHolder(parent: ViewGroup?) : BaseItemViewHolder<Product>(parent, R.layout.item_hot_product), LayoutContainer {


    override val containerView: View? get() = itemView


    init {
        //  宽高比4:3
        val layoutParams = product_iv.layoutParams
        val screenWidth = (ScreenUtils.getScreenWidth() / 2) - SizeUtils.dp2px(16f)
        layoutParams.height = screenWidth * 3 / 4
        product_iv.layoutParams = layoutParams

    }


    override fun setData(data: Product?) {

        if (data == null) {
            return
        }

        //  TODO url请求
        Glide.with(context).load(data.imgUrl).apply(
            RequestOptions().transform(
                MultiTransformation(
                    CenterCrop(),
                    RoundedCornersTransformation(SizeUtils.dp2px(4f), 0, RoundedCornersTransformation.CornerType.TOP)
                )
            )
        ).into(product_iv)


    }


}
