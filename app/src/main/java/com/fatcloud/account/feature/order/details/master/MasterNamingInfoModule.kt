package com.fatcloud.account.feature.order.details.master

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/8/20 0020 13:25.
 * </br>
 *
 */
@Module
class MasterNamingInfoModule {

    @Provides
    fun viewProvider(activity: MasterNamingInfoActivity): MasterNamingInfoView {
        return activity
    }




}