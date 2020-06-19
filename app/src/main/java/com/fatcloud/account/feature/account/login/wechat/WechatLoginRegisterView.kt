package com.fatcloud.account.feature.account.login.wechat

import com.fatcloud.account.base.common.BaseTaskView

/**
 * Created by Wangsw on 2020/6/18 0018 20:42.
 * </br>
 *
 */
interface WechatLoginRegisterView : BaseTaskView {
    /**
     * 账户是否存在
     * @param existed 用户是否存在
     */
    fun accountExistedTag(existed: Boolean, account: String)
}