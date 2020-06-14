package com.fatcloud.account.app

import com.fatcloud.account.feature.MainActivity
import com.fatcloud.account.feature.MainModule
import com.fatcloud.account.feature.about.AboutActivity
import com.fatcloud.account.feature.about.AboutModule
import com.fatcloud.account.feature.about.contacts.ContactActivity
import com.fatcloud.account.feature.about.contacts.ContactModule
import com.fatcloud.account.feature.account.captcha.CaptchaActivity
import com.fatcloud.account.feature.account.captcha.CaptchaModule
import com.fatcloud.account.feature.account.login.LoginActivity
import com.fatcloud.account.feature.account.login.LoginModule
import com.fatcloud.account.feature.account.password.PasswordSetActivity
import com.fatcloud.account.feature.account.password.PasswordSetModule
import com.fatcloud.account.feature.account.password.login.PasswordLoginActivity
import com.fatcloud.account.feature.account.password.login.PasswordLoginModule
import com.fatcloud.account.feature.extra.BusinessScopeActivity
import com.fatcloud.account.feature.extra.BusinessScopeModule
import com.fatcloud.account.feature.forms.enterprise.license.FormLicenseEnterpriseActivity
import com.fatcloud.account.feature.forms.enterprise.license.FormLicenseEnterpriseModule
import com.fatcloud.account.feature.forms.personal.bookkeeping.FormAgentBookkeepingPersonalActivity
import com.fatcloud.account.feature.forms.personal.bookkeeping.FormAgentBookkeepingPersonalModule
import com.fatcloud.account.feature.forms.personal.bookkeeping.signature.SignatureActivity
import com.fatcloud.account.feature.forms.personal.bookkeeping.signature.SignatureModule
import com.fatcloud.account.feature.forms.personal.license.FormLicensePersonalActivity
import com.fatcloud.account.feature.forms.personal.license.FormLicensePersonalModule
import com.fatcloud.account.feature.forms.personal.tax.FormTaxRegistrationPersonalActivity
import com.fatcloud.account.feature.forms.personal.tax.FormTaxRegistrationPersonalModule
import com.fatcloud.account.feature.news.detail.NewsDetailActivity
import com.fatcloud.account.feature.news.detail.NewsDetailModule
import com.fatcloud.account.feature.order.details.enterprise.company.CompanyRegisterInfoModule
import com.fatcloud.account.feature.order.details.enterprise.company.CompanyRegisterInfoActivity
import com.fatcloud.account.feature.order.details.personal.RegistrantInfoActivity
import com.fatcloud.account.feature.order.details.personal.RegistrantInfoModule
import com.fatcloud.account.feature.order.lists.OrderListActivity
import com.fatcloud.account.feature.order.lists.OrderListModule
import com.fatcloud.account.feature.order.progress.ScheduleActivity
import com.fatcloud.account.feature.order.progress.ScheduleModule
import com.fatcloud.account.feature.product.detail.ProductDetailActivity
import com.fatcloud.account.feature.product.detail.ProductDetailModule
import com.fatcloud.account.feature.webs.WebCommonActivity
import com.fatcloud.account.feature.webs.WebCommonModule
import com.fatcloud.account.scope.ActivityScore
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
    @ContributesAndroidInjector(modules = [FragmentBindModule::class, ProductDetailModule::class])
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

    @ActivityScore
    @ContributesAndroidInjector(modules = [CompanyRegisterInfoModule::class])
    abstract fun companyRegisterRegisterInfoActivity(): CompanyRegisterInfoActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [ScheduleModule::class])
    abstract fun businessProgressActivityInjector(): ScheduleActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [RegistrantInfoModule::class])
    abstract fun registrantInfoActivityInjector(): RegistrantInfoActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [FormLicenseEnterpriseModule::class])
    abstract fun formLicenseEnterpriseActivityInjector(): FormLicenseEnterpriseActivity


    @ActivityScore
    @ContributesAndroidInjector(modules = [FormLicensePersonalModule::class])
    abstract fun formLicensePersonalActivityInjector(): FormLicensePersonalActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [BusinessScopeModule::class])
    abstract fun businessScopeActivityInjector(): BusinessScopeActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [FormTaxRegistrationPersonalModule::class])
    abstract fun formTaxRegistrationPersonalActivityInjector(): FormTaxRegistrationPersonalActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [FormAgentBookkeepingPersonalModule::class])
    abstract fun formAgentBookkeepingPersonalActivityInjector(): FormAgentBookkeepingPersonalActivity


    @ActivityScore
    @ContributesAndroidInjector(modules = [FragmentBindModule::class, SignatureModule::class])
    abstract fun signatureActivityActivityInjector(): SignatureActivity


}