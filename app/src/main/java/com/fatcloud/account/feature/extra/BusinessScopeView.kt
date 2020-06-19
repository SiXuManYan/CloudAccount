package com.fatcloud.account.feature.extra

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.commons.Commons

/**
 * Created by Wangsw on 2020/6/10 0010 18:30.
 * </br>
 *
 */
interface BusinessScopeView : BaseTaskView {
    fun receiveCommonData(data: Commons)
}