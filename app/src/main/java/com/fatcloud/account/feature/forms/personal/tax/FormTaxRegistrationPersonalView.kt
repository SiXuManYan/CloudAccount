package com.fatcloud.account.feature.forms.personal.tax

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.defray.prepare.PreparePay

/**
 * Created by Wangsw on 2020/6/13 0013 13:45.
 * </br>
 * 个体户税务登记
 */
interface FormTaxRegistrationPersonalView : BaseTaskView {
    fun commitSuccess(preparePay: PreparePay)
}