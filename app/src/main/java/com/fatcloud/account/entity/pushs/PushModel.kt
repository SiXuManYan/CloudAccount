package com.fatcloud.account.entity.pushs

/**
 * Created by Wangsw on 2020/7/29 0029 14:51.
 *
 */
class PushModel {


    /** 推送类型（1=订单推送；2=资讯推送；3=分销推送）  */
    private val pushType: String? = null
    private val order: PushOrder? = null
    private val news: PushNews? = null
    private val distribution: PushDistribution? = null

    companion object {
        const val PUSH_TYPE_ORDER = "1"
        const val PUSH_TYPE_NEWS = "2"
        const val PUSH_TYPE_DISTRIBUTION = "3"
    }
}