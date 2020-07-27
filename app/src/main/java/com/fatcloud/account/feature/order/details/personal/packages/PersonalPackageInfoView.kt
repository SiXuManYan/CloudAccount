package com.fatcloud.account.feature.order.details.personal.packages

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.order.detail.PersonalPackageDetail

/**
 * Created by Wangsw on 2020/7/27 0027 13:56.
 * </br>
 * P9 P10回显页
 */
interface PersonalPackageInfoView : BaseTaskView {
    fun bindDetailInfo(data: PersonalPackageDetail)


}