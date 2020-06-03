package com.account.app

import com.account.feature.MainActivity
import com.account.feature.MainModule
import com.account.feature.about.AboutActivity
import com.account.feature.about.AboutModule
import com.account.feature.about.contacts.ContactActivity
import com.account.feature.about.contacts.ContactModule
import com.account.feature.account.captcha.CaptchaActivity
import com.account.feature.account.captcha.CaptchaModule
import com.account.feature.account.login.LoginActivity
import com.account.feature.account.login.LoginModule
import com.account.feature.account.password.PasswordSetActivity
import com.account.feature.account.password.PasswordSetModule
import com.account.feature.account.password.login.PasswordLoginActivity
import com.account.feature.account.password.login.PasswordLoginModule
import com.account.feature.news.detail.NewsDetailActivity
import com.account.feature.news.detail.NewsDetailModule
import com.account.feature.order.OrderListActivity
import com.account.feature.order.OrderListModule
import com.account.feature.product.detail.ProductDetailActivity
import com.account.feature.product.detail.ProductDetailModule
import com.account.feature.webs.WebCommonActivity
import com.account.feature.webs.WebCommonModule
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

    @ActivityScore
    @ContributesAndroidInjector(modules = [AboutModule::class])
    abstract fun aboutActivityInjector(): AboutActivity


    @ActivityScore
    @ContributesAndroidInjector(modules = [WebCommonModule::class])
    abstract fun webCommonActivityInjector(): WebCommonActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [ContactModule::class])
    abstract fun contactActivityInjector(): ContactActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun loginActivityInjector(): LoginActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [CaptchaModule::class])
    abstract fun captchaActivityInjector(): CaptchaActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [PasswordSetModule::class])
    abstract fun passwordSetActivityInjector(): PasswordSetActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [PasswordLoginModule::class])
    abstract fun passwordLoginActivityInjector(): PasswordLoginActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [OrderListModule::class])
    abstract fun orderListActivityInjector(): OrderListActivity


}
