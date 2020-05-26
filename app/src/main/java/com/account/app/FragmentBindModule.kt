package com.account.app

import com.account.feature.home.HomeFragment
import com.account.feature.home.HomeModule
import com.account.feature.my.MyPageFragment
import com.account.feature.my.MyPageModule
import com.account.feature.news.NewsFragment
import com.account.feature.news.NewsModule
import com.account.feature.services.ServiceFragment
import com.account.feature.services.ServiceModule
import com.jz.yihua.activity.scope.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * 绑定Fragment
 */
@Module
abstract class FragmentBindModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun homeFragmentInjector(): HomeFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [ServiceModule::class])
    abstract fun serviceFragmentInjector(): ServiceFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [NewsModule::class])
    abstract fun newsFragmentInjector(): NewsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [MyPageModule::class])
    abstract fun myPageFragmentInjector(): MyPageFragment


}

