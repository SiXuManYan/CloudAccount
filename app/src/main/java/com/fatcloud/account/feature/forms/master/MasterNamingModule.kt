package com.fatcloud.account.feature.forms.master

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/7/15 0015 10:05.
 * </br>
 *
 */
@Module
class MasterNamingModule {


    @Provides
    fun viewProvider(activity: MasterNamingActivity): MasterNamingView {
        return activity
    }

}