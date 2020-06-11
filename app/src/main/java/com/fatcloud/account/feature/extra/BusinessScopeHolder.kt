package com.fatcloud.account.feature.extra

import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ColorUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.list.BaseItemViewHolder
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.commons.BusinessScope
import com.fatcloud.account.entity.order.progress.BusinessProgress
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_schedule.*
import kotlinx.android.synthetic.main.item_spinner_view.*

class BusinessScopeHolder(parent: ViewGroup?) : BaseItemViewHolder<BusinessScope>(parent, R.layout.item_spinner_view), LayoutContainer {


    override val containerView: View? get() = itemView

    override fun setData(data: BusinessScope?) {

        if (data == null) {
            return
        }
        spinner_text.text = data.name

        if (data.nativeIsSelect) {
            spinner_text.setBackgroundResource(R.drawable.shape_stock_red)
            spinner_text.setTextColor(ColorUtils.getColor(R.color.color_app_red))
        } else {
            spinner_text.setBackgroundResource(R.drawable.shape_stock_gray)
            spinner_text.setTextColor(ColorUtils.getColor(R.color.color_third_level))
        }


    }
}