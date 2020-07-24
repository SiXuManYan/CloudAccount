package com.fatcloud.account.feature.forms.personal.bank

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.google.gson.JsonElement

/**
 * Created by Wangsw on 2020/7/16 0016 10:59.
 * </br>
 * 个体户银行对公账户
 */
interface FormPersonalBankView : BaseTaskView {
    fun commitSuccess(preparePay: PreparePay)
    fun commitSuccessP9()
}