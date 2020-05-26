package com.jz.yihua.activity.base

import com.account.base.common.BaseView


interface BaseNoJsonListView2<T> : BaseView {
    fun bindList(list: ArrayList<T>, isFirstPage: Boolean, last: Boolean)

}