package com.fatcloud.account.app

import com.fatcloud.account.feature.sheet.form.FormSheetFragment
import com.fatcloud.account.feature.sheet.form.FormSheetModule
import com.fatcloud.account.feature.forms.personal.bookkeeping.wordpad.WordpadFragment
import com.fatcloud.account.feature.forms.personal.bookkeeping.wordpad.WordpadModule
import com.fatcloud.account.feature.home.HomeFragment
import com.fatcloud.account.feature.home.HomeModule
import com.fatcloud.account.feature.my.MyPageFragment
import com.fatcloud.account.feature.my.MyPageModule
import com.fatcloud.account.feature.news.NewsFragment
import com.fatcloud.account.feature.news.NewsModule
import com.fatcloud.account.feature.news.child.NewsChildFragment
import com.fatcloud.account.feature.news.child.NewsChildModule
import com.fatcloud.account.feature.product.ProductFragment
import com.fatcloud.account.feature.product.ProductModule
import com.fatcloud.account.feature.product.detail.check.ProductCheckFragment
import com.fatcloud.account.feature.product.detail.check.ProductCheckModule
import com.fatcloud.account.feature.product.detail.input.ProductInputFragment
import com.fatcloud.account.feature.product.detail.input.ProductInputModule
import com.fatcloud.account.feature.product.detail.share.ShareFragment
import com.fatcloud.account.feature.product.detail.share.ShareModule
import com.fatcloud.account.feature.product.detail.sheet.ProductSheetFragment
import com.fatcloud.account.feature.product.detail.sheet.ProductSheetModule
import com.fatcloud.account.feature.product.detail.spinners.ProductSpinnerFragment
import com.fatcloud.account.feature.product.detail.spinners.ProductSpinnerModule
import com.fatcloud.account.feature.sheet.nature.AccountNatureSheetFragment
import com.fatcloud.account.feature.sheet.nature.AccountNatureSheetModule
import com.fatcloud.account.scope.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * dagger 绑定Fragment
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


    @FragmentScope
    @ContributesAndroidInjector(modules = [ProductSheetModule::class])
    abstract fun productSheetFragmentInjector(): ProductSheetFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [ProductSpinnerModule::class])
    abstract fun productSpinnerFragmentInjector(): ProductSpinnerFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [WordpadModule::class])
    abstract fun wordpadFragmentInjector(): WordpadFragment


    @FragmentScope
    @ContributesAndroidInjector(modules = [FormSheetModule::class])
    abstract fun formSheetFragmentInjector(): FormSheetFragment


    @FragmentScope
    @ContributesAndroidInjector(modules = [AccountNatureSheetModule::class])
    abstract fun accountNatureSheetFragmentInjector(): AccountNatureSheetFragment


    @FragmentScope
    @ContributesAndroidInjector(modules = [ProductCheckModule::class])
    abstract fun productCheckFragmentInjector(): ProductCheckFragment


    @FragmentScope
    @ContributesAndroidInjector(modules = [ProductInputModule::class])
    abstract fun productInputFragmentInjector(): ProductInputFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [ShareModule::class])
    abstract fun shareFragmentInjector(): ShareFragment


}

