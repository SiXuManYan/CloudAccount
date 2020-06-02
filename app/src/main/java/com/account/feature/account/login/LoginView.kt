package com.account.feature.account.login

import com.account.base.common.BaseTaskView

/**
 * Created by Wangsw on 2020/6/1 0001 17:10.
 * </br>
 *
 */
interface LoginView :BaseTaskView{

    /**
     * 账户是否存在
     * @param existed 用户是否存在
     */
    fun accountExistedTag(existed: Boolean, account: String)
}