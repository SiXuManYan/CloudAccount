package com.fatcloud.account.view.swipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fatcloud.account.R
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter

class NoMoreItemView : RecyclerArrayAdapter.ItemView {

    override fun onBindView(headerView: View?) {
    }

    override fun onCreateView(parent: ViewGroup?): View {
        return LayoutInflater.from(parent!!.context).inflate(R.layout.view_load_no_more, parent,false)
    }

}