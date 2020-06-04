package com.fatcloud.account.base.ui.list

import com.fatcloud.account.base.common.BaseView


interface BaseNoJsonListView2<T> : BaseView {

    /**
     * 绑定列表数据
     * @param list 列表数据
     * @param isFirstPage 是否是第一页
     * @param last 是否为最后一页
     */
    fun bindList(list: ArrayList<T>, isFirstPage: Boolean, last: Boolean)


    /**
     * 绑定列表数据，并且记录最后一项 itemId
     * @param list 列表数据
     * @param lastItemId 最后一项
     */
    fun bindList(list: ArrayList<T>, lastItemId: String? = null)

}