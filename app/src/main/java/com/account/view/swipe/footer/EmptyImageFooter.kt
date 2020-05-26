package com.jz.yihua.activity.feature.main.explore.category.footer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.account.R
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter

/**
 * Created by Wangsw on 2019/9/9 16:46.
 * </br>
 *
 */
class EmptyImageFooter constructor(context: Context) : RecyclerArrayAdapter.ItemView {

    var mContext = context
    var emptyImageResId: Int = R.drawable.ic_empy_helpless
    var emptyText: String = mContext.getString(R.string.hint_data_no_found)

    override fun onBindView(headerView: View?) = Unit

    override fun onCreateView(parent: ViewGroup?): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.view_empty_footer_image, parent, false)
        view.findViewById<TextView>(R.id.empty_tv).text = emptyText
        view.findViewById<ImageView>(R.id.empty_iv).setImageResource(emptyImageResId)
        return view
    }

}