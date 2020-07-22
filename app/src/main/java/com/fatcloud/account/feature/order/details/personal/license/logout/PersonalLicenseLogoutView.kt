package com.fatcloud.account.feature.order.details.personal.license.logout

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.order.detail.PersonalLicenseLogoutDetail

/**
 * Created by Wangsw on 2020/7/22 0022 15:25.
 * </br>
 *
 */
interface PersonalLicenseLogoutView : BaseTaskView {
    fun bindDetailInfo(data: PersonalLicenseLogoutDetail)

}