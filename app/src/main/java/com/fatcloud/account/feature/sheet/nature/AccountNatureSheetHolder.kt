package com.fatcloud.account.feature.sheet.nature

import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ColorUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.list.BaseItemViewHolder
import com.fatcloud.account.entity.commons.AccountNature
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_product_sheet_select.*

/**
 * Created by Wangsw on 2020/6/17 0017 22:20.
 * </br>
 * 账户性质
 */
class AccountNatureSheetHolder(parent: ViewGroup?) : BaseItemViewHolder<AccountNature>(parent, R.layout.item_product_sheet_select), LayoutContainer {

    override val containerView: View? get() = itemView

    override fun setData(data: AccountNature?) {
        if (data == null) {
            return
        }

        title_tv.text = data.name
        if (data.nativeIsSelect) {
            title_tv.setTextColor(ColorUtils.getColor(R.color.color_app_red))
            title_tv.setBackgroundResource(R.drawable.shape_stock_red_match_18)
        } else {
            title_tv.setTextColor(ColorUtils.getColor(R.color.color_third_level))
            title_tv.setBackgroundResource(R.drawable.shape_stock_gray_match_18)
        }
    }
}