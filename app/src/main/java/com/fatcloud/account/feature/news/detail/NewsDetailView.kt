package com.fatcloud.account.feature.news.detail

import com.fatcloud.account.base.common.BaseView
import com.fatcloud.account.entity.news.NewDetail

/**
 * Created by Wangsw on 2020/5/30 0030 16:40.
 * </br>
 *
 */
interface NewsDetailView : BaseView {
    fun bindDetailData(it: NewDetail)
}