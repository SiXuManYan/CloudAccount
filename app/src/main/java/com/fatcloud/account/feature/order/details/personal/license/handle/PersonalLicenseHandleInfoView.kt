package com.fatcloud.account.feature.order.details.personal.license.handle

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.order.persional.PersonalInfo

/**
 * Created by Wangsw on 2020/6/4 0004 14:01.
 * </br>
 *  注册人信息
 */
interface PersonalLicenseHandleInfoView : BaseTaskView {
    fun bindDetailInfo(data: PersonalInfo)
}