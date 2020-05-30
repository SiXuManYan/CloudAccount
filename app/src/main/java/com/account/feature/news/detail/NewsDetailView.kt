package com.account.feature.news.detail

import com.account.base.common.BaseView
import com.account.entity.news.NewDetail

/**
 * Created by Wangsw on 2020/5/30 0030 16:40.
 * </br>
 *
 */
interface NewsDetailView : BaseView {
    fun bindDetailData(it: NewDetail)
}