package com.fatcloud.account.feature.account.captcha

import com.fatcloud.account.base.common.BaseTaskView

/**
 * Created by Wangsw on 2020/6/2 0002 14:30.
 * </br>
 *
 */
interface CaptchaView : BaseTaskView {

    /**
     * 验证码发送成功
     */
    fun captchaSendResult()

    /**
     * 验证码校验通过
     */
    fun captchaVerified(captcha: String, account: String)

    /**
     * 微信注册成功
     */
    fun wechatRegisterSuccess()
}