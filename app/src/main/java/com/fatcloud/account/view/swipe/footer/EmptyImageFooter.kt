package com.fatcloud.account.view.swipe.footer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.fatcloud.account.R
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter

/**
 * Created by Wangsw on 2019/9/9 16:46.
 * </br>
 *
 */
class EmptyImageFooter constructor(context: Context) : RecyclerArrayAdapter.ItemView {

    var mContext = context
    var emptyImageResId: Int = R.drawable.img_data_no_found
    var emptyText: String = ""

    override fun onBindView(headerView: View?) = Unit

    override fun onCreateView(parent: ViewGroup?): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.view_empty_footer_image, parent, false)
        view.findViewById<TextView>(R.id.empty_tv).text = emptyText
        view.findViewById<ImageView>(R.id.empty_iv).setImageResource(emptyImageResId)
        return view
    }

}