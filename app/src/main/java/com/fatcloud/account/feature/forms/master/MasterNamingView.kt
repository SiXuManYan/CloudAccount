package com.fatcloud.account.feature.forms.master

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.defray.prepare.PreparePay

/**
 * Created by Wangsw on 2020/7/15 0015 10:04.
 * </br>
 * 大师起名
 */
interface MasterNamingView : BaseTaskView {

    fun commitSuccess(preparePay: PreparePay)

}