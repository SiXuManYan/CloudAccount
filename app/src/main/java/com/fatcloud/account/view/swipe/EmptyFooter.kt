package com.fatcloud.account.view.swipe

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import com.blankj.utilcode.util.SizeUtils
import com.fatcloud.account.R
import com.fatcloud.account.view.error.AccidentView
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter

/**
 * Created by Wangsw on 2019/8/21 10:58.
 * </br>
 *
 */
class EmptyFooter constructor(context: Context) : RecyclerArrayAdapter.ItemView {


    var mContext = context
    var emptyText: String = ""
    var emptyImageResId: Int = R.drawable.img_data_no_found

    var accidentView: AccidentView = AccidentView(context)

    override fun onBindView(headerView: View?) = Unit

    override fun onCreateView(parent: ViewGroup?): View {

        if (accidentView.parent != null) {
            val viewGroup = accidentView.parent as ViewGroup
            viewGroup.removeView(accidentView)
        }
        accidentView.showEmpty(emptyText, emptyImageResId)

        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        layoutParams.gravity = Gravity.CENTER_VERTICAL

        val linearLayout = LinearLayout(parent?.context)
        linearLayout.layoutParams = layoutParams
        linearLayout.orientation = VERTICAL
        linearLayout.setBackgroundColor(Color.WHITE)
        linearLayout.addView(accidentView)
        linearLayout.setPadding(0, SizeUtils.dp2px(200f), 0, SizeUtils.dp2px(200f))

        return linearLayout
    }

}