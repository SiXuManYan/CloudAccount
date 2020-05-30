package com.account.app

import com.account.feature.MainActivity
import com.account.feature.MainModule
import com.account.feature.news.detail.NewsDetailActivity
import com.account.feature.news.detail.NewsDetailModule
import com.account.feature.product.detail.ProductDetailActivity
import com.account.feature.product.detail.ProductDetailModule
import com.account.scope.ActivityScore
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Activity绑定Dagger2
 */
@Module
abstract class ActivityBindModule {


    @ActivityScore
    @ContributesAndroidInjector(modules = [FragmentBindModule::class, MainModule::class])
    abstract fun mainActivityInjector(): MainActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [ProductDetailModule::class])
    abstract fun productDetailActivityInjector(): ProductDetailActivity


    @ActivityScore
    @ContributesAndroidInjector(modules = [NewsDetailModule::class])
    abstract fun newsDetailActivityInjector(): NewsDetailActivity


}
