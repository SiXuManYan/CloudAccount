package com.fatcloud.account.feature.account.captcha

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/2 0002 14:30.
 * </br>
 *
 */
@Module
class CaptchaModule {

    @Provides
    fun viewProvider(captchaActivity: CaptchaActivity): CaptchaView {
        return captchaActivity
    }


}