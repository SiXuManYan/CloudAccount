package com.fatcloud.account.feature.extra

import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ColorUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.list.BaseItemViewHolder
import com.fatcloud.account.entity.commons.BusinessScope
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_business_scope.*

class BusinessScopeHolder(parent: ViewGroup?) : BaseItemViewHolder<BusinessScope>(parent, R.layout.item_business_scope), LayoutContainer {


    override val containerView: View? get() = itemView

    override fun setData(data: BusinessScope?) {

        if (data == null) {
            return
        }
        spinner_text.text = data.name

        if (data.nativeIsSelect) {
            sel_iv.visibility = View.VISIBLE
        } else {
            sel_iv.visibility = View.GONE
        }


    }
}