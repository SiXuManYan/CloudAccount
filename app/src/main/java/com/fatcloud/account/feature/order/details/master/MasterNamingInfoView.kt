package com.fatcloud.account.feature.order.details.master

import com.fatcloud.account.base.common.BaseView
import com.fatcloud.account.entity.order.detail.MasterNamingDetail

/**
 * Created by Wangsw on 2020/8/20 0020 13:24.
 * </br>
 *
 */
interface MasterNamingInfoView :BaseView {
    fun bindDetailInfo(data: MasterNamingDetail)
}