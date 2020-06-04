package com.fatcloud.account.feature.home

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/5/25 0025 16:46.
 * </br>
 *
 */
@Module
class HomeModule {

    @Provides
    fun viewProvider(fragment: HomeFragment): HomeView {
        return fragment
    }

}