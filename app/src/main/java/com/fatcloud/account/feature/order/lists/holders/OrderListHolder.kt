package com.fatcloud.account.feature.order.lists.holders

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.fatcloud.account.R
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.list.BaseItemViewHolder
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.entity.order.persional.Order
import com.fatcloud.account.extend.RoundTransFormation
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_order.*

/**
 * Created by Wangsw on 2020/6/3 0003 18:06.
 * </br>
 * 订单列表
 */
class OrderListHolder(parent: ViewGroup?) : BaseItemViewHolder<Order>(parent, R.layout.item_order), LayoutContainer {

    override val containerView: View? get() = itemView

    override fun setData(data: Order?) {
        if (data == null) {
            return
        }
        order_id_tv.text = StringUtils.getString(R.string.order_id_format,data.productId)
        Glide.with(context)
//            .load(data.imgUrl)
            .load(CommonUtils.getTestUrl())
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


        content_tv.text = data.productName
        amount_tv.text = StringUtils.getString(R.string.money_symbol_format, data.money.stripTrailingZeros().toPlainString())
        time_tv.text = data.createDt
        order_status_tv.text = data.stateText

        val layoutParams = card_cv.layoutParams as LinearLayout.LayoutParams
        layoutParams.apply {

            topMargin = if (adapterPosition == 0) {
                SizeUtils.dp2px(10f)
            } else {
                SizeUtils.dp2px(0f)
            }
        }


        // todo  订单有效期倒计时 ,


    }


}