package com.fatcloud.account.feature.upgrade

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/16 0016 21:50.
 * </br>
 *
 */
@Module
class UpgradeModule {


    @Provides
    fun viewProvider(activity: UpgradeActivity): UpgradeView {
        return activity
    }


}