package com.fatcloud.account.base.ui.list

import com.fatcloud.account.base.common.BaseView


interface BaseNoJsonListView<T> : BaseView {
    fun bindList(list: ArrayList<T>, isFirstPage: Boolean, last: Boolean)
    fun onRefresh()
    fun onLoadMore()

}