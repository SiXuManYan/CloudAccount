package com.fatcloud.account.feature.forms.personal.change

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.defray.prepare.PreparePay

/**
 * Created by Wangsw on 2020/7/10 0010 15:37.
 * </br>
 *
 */
interface FormLicenseChangeView :BaseTaskView{
    fun commitSuccess(it: PreparePay)
}