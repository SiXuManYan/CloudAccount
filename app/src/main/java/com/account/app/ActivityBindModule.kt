package com.account.app

import com.account.feature.MainActivity
import com.account.feature.MainModule
import com.jz.yihua.activity.scope.ActivityScore
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Activity绑定Dagger2
 */
@Module
abstract class ActivityBindModule {

//    @ActivityScore
//    @ContributesAndroidInjector(modules = [ServiceModule::class])
//    abstract fun dataSyncServiceInjector(): DataService

    @ActivityScore
    @ContributesAndroidInjector(modules = [FragmentBindModule::class, MainModule::class])
    abstract fun mainActivityInjector(): MainActivity


}
