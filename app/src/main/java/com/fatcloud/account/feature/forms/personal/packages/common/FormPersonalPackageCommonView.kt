package com.fatcloud.account.feature.forms.personal.packages.common

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.defray.prepare.PreparePay

/**
 * Created by Wangsw on 2020/7/23 0023 10:22.
 * </br>
 * 个体户套餐 P9
 */
interface FormPersonalPackageCommonView : BaseTaskView {
    fun commitSuccess(preparePay: PreparePay)


}