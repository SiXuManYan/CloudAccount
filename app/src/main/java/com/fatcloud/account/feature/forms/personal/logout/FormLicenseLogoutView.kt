package com.fatcloud.account.feature.forms.personal.logout

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.defray.prepare.PreparePay

/**
 * Created by Wangsw on 2020/7/13 0013 16:34.
 * </br>
 * 个体户营业执照注销
 */
interface FormLicenseLogoutView : BaseTaskView {

    /**
     * 注销成功
     */
    fun commitSuccess(preparePay: PreparePay)


}