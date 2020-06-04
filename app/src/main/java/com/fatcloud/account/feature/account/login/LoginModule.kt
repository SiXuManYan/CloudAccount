package com.fatcloud.account.feature.account.login

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/1 0001 17:10.
 * </br>
 *
 */
@Module
class LoginModule {

    @Provides
    fun viewProvider(loginActivity: LoginActivity): LoginView {
        return loginActivity
    }


}