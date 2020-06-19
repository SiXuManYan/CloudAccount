package com.fatcloud.account.feature.account.login.wechat

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/18 0018 20:43.
 * </br>
 *
 */
@Module
class WechatLoginRegisterModule {

    @Provides
    fun viewProvider(loginActivity: WechatLoginRegisterActivity): WechatLoginRegisterView {
        return loginActivity
    }

}