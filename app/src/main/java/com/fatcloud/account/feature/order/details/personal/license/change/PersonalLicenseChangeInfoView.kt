package com.fatcloud.account.feature.order.details.personal.license.change

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.order.detail.PersonalLicenseChangeDetail

/**
 * Created by Wangsw on 2020/7/22 0022 11:40.
 * </br>
 * 个体户营业执照变更
 */
interface PersonalLicenseChangeInfoView : BaseTaskView {


    fun bindDetailInfo(data: PersonalLicenseChangeDetail)

}