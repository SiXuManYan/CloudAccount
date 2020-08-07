package com.fatcloud.account.feature.order.details.bookkeeping

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.order.detail.BookkeepingDetail
import com.fatcloud.account.entity.order.persional.PersonalInfo

/**
 * Created by Wangsw on 2020/6/19 0019 18:21.
 * </br>
 *
 */
interface BookkeepingInfoView :BaseTaskView{
    fun bindDetailInfo(data: BookkeepingDetail)
}