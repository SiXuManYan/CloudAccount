package com.account.feature.account.password

import com.account.base.common.BaseTaskView

/**
 * Created by Wangsw on 2020/6/2 0002 15:54.
 * </br>
 *
 */
interface PasswordSetView : BaseTaskView {

    /**
     * 注册成功
     */
    fun registerSuccess()

    /**
     * 密码重设成功
     */
    fun passwordResetSuccess()
}