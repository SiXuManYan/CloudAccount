package com.fatcloud.account.feature.order.progress

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/4 0004 11:59.
 * </br>
 *
 */
@Module
class BusinessProgressModule {


    @Provides
    fun viewProvider(activity: BusinessProgressActivity): BusinessProgressView {
        return activity
    }
}