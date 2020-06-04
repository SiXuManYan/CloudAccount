package com.fatcloud.account.feature

import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Provides
    fun viewProvider(activity: MainActivity): MainView {
        return activity
    }
}