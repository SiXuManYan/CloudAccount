package com.fatcloud.account.feature.product.holder

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.fatcloud.account.R
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.list.BaseItemViewHolder
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.entity.product.Product2
import com.fatcloud.account.extend.RoundTransFormation
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_product.*
import kotlinx.android.synthetic.main.item_product.image_iv

/**
 * Created by Wangsw on 2020/5/28 0028 9:10.
 * </br>
 * 产品列表Holder
 */
class ProductHolder(parent: ViewGroup?) : BaseItemViewHolder<Product2>(parent, R.layout.item_product), LayoutContainer {

    override val containerView: View? get() = itemView

    override fun setData(data: Product2?) {
        if (data == null) {
            return
        }
        name_tv.text = data.name
        content_tv.text = data.introduce
        sales_tv.text = StringUtils.getString(R.string.sales_format, data.orderCount.toString())
        amount_tv.text = StringUtils.getString(R.string.money_symbol_format, data.money.stripTrailingZeros().toPlainString())

        Glide.with(context)
            .load(data.imgurl)
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

        val layoutParams = card_cv.layoutParams as LinearLayout.LayoutParams
        layoutParams.apply {

            topMargin = if (adapterPosition == 0) {
                SizeUtils.dp2px(10f)
            } else {
                SizeUtils.dp2px(0f)
            }
        }


    }


}