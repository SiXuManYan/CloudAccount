package com.fatcloud.account.feature.order.details.personal.bank

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.order.persional.bank.PersonalBankDetailP8
import com.fatcloud.account.entity.order.persional.bank.PersonalBankDetailP9P10

/**
 * Created by Wangsw on 2020/7/21 0021 14:48.
 * </br>
 *
 */
interface PersonalBankInfoView : BaseTaskView {

    fun bindDetail(data: PersonalBankDetailP8)
    fun bindDetailP9P10(it: PersonalBankDetailP9P10)

}