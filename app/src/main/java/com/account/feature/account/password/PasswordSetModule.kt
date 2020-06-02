package com.account.feature.account.password

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/2 0002 15:54.
 * </br>
 *
 */
@Module
class PasswordSetModule {

    @Provides
    fun viewProvider(activity: PasswordSetActivity): PasswordSetView {
        return activity
    }

}