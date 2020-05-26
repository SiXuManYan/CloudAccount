package com.jz.yihua.activity.base

import com.account.base.common.BaseView


interface BaseNoJsonListView<T> : BaseView {
    fun bindList(list: ArrayList<T>, isFirstPage: Boolean, last: Boolean)
    fun onRefresh()
    fun onLoadMore()

}