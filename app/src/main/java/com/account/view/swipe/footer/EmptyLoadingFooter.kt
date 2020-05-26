package com.jz.yihua.activity.feature.main.explore.category.footer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.account.R
import com.account.view.error.AccidentView
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter

/**
 * Created by Wangsw on 2019/9/9 16:47.
 * </br>
 *
 */

class EmptyLoadingFooter constructor(context: Context) : RecyclerArrayAdapter.ItemView {

    var mContext = context

    val accident: AccidentView? = null

    override fun onBindView(headerView: View?) = Unit

    override fun onCreateView(parent: ViewGroup?): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.view_empty_footer_retry, parent, false)
        val accident = view.findViewById<AccidentView>(R.id.accident)
        accident.showLoading()
        return view
    }
}