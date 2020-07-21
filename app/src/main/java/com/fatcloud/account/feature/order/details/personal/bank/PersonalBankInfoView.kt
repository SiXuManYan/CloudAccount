package com.fatcloud.account.feature.order.details.personal.bank

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.order.persional.bank.PersonalBankDetail

/**
 * Created by Wangsw on 2020/7/21 0021 14:48.
 * </br>
 *
 */
interface PersonalBankInfoView : BaseTaskView {

    fun bindDetail(data: PersonalBankDetail)

}