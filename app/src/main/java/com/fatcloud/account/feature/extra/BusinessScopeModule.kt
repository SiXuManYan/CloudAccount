package com.fatcloud.account.feature.extra

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/10 0010 18:30.
 * </br>
 *
 */
@Module
class BusinessScopeModule {


    @Provides
    fun viewProvider(activity: BusinessScopeActivity): BusinessScopeView {
        return activity
    }

}