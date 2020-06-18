package com.fatcloud.account.feature.forms.personal.bookkeeping.signature

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.defray.prepare.PreparePay

/**
 * Created by Wangsw on 2020/6/13 0013 16:59.
 * </br>
 *
 */
interface SignatureView :BaseTaskView{
    fun addAgentBookkeepingSuccess(preparePay: PreparePay)
}