package com.fatcloud.account.feature.forms.personal.license

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.defray.prepare.PreparePay

/**
 * Created by Wangsw on 2020/6/12 0012 13:32.
 * </br>
 * 个体户营业执照
 */
interface FormLicensePersonalView :BaseTaskView{
    fun addLicensePersonalSuccess(preparePay: PreparePay)
}