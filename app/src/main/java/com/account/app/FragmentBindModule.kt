package com.account.app

import com.account.feature.home.HomeFragment
import com.account.feature.home.HomeModule
import com.account.feature.my.MyPageFragment
import com.account.feature.my.MyPageModule
import com.account.feature.news.NewsFragment
import com.account.feature.news.NewsModule
import com.account.feature.news.child.NewsChildFragment
import com.account.feature.news.child.NewsChildModule
import com.account.feature.product.ProductFragment
import com.account.feature.product.ProductModule
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
    @ContributesAndroidInjector(modules = [ProductModule::class])
    abstract fun serviceFragmentInjector(): ProductFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [NewsModule::class])
    abstract fun newsFragmentInjector(): NewsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [MyPageModule::class])
    abstract fun myPageFragmentInjector(): MyPageFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [NewsChildModule::class])
    abstract fun newsChildFragmentInjector(): NewsChildFragment


}

