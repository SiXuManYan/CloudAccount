package com.fatcloud.account.feature.forms.enterprise.bank

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.order.enterprise.BankInfo
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo

/**
 * Created by Wangsw on 2020/6/15 0015 15:08.
 * </br>
 *
 */
interface FormBankView :BaseTaskView {
    fun bindDetailInfo(data: BankInfo)
    fun addSuccess()
}