package com.account.feature.services

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/5/25 0025 16:57.
 * </br>
 *
 */
@Module
class ServiceModule {

    @Provides
    fun viewProvider(fragment: ServiceFragment): ServiceView {
        return fragment
    }

}