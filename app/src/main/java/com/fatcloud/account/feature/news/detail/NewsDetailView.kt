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

    /**
     * 获取点赞状态
     * @param status 0 没点赞 1 已经点赞了
     */
    fun newsLikeStatus(status: Int)

    /**
     * 点赞和取消赞操作成功
     */
    fun handleLikeSuccess()
}