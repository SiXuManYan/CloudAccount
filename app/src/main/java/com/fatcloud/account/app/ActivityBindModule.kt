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
import com.fatcloud.account.feature.account.login.wechat.WechatLoginRegisterActivity
import com.fatcloud.account.feature.account.login.wechat.WechatLoginRegisterModule
import com.fatcloud.account.feature.account.password.PasswordSetActivity
import com.fatcloud.account.feature.account.password.PasswordSetModule
import com.fatcloud.account.feature.account.password.login.PasswordLoginActivity
import com.fatcloud.account.feature.account.password.login.PasswordLoginModule
import com.fatcloud.account.feature.defray.PayActivity
import com.fatcloud.account.feature.defray.PayModule
import com.fatcloud.account.feature.defray.prepare.PayPrepareActivity
import com.fatcloud.account.feature.defray.prepare.PayPrepareModule
import com.fatcloud.account.feature.defray.result.CloudPayResultActivity
import com.fatcloud.account.feature.defray.result.CloudPayResultModule
import com.fatcloud.account.feature.defray.unknown.PayUnknownActivity
import com.fatcloud.account.feature.defray.unknown.PayUnknownModule
import com.fatcloud.account.feature.extra.BusinessScopeActivity
import com.fatcloud.account.feature.extra.BusinessScopeModule
import com.fatcloud.account.feature.forms.enterprise.bank.FormBankActivity
import com.fatcloud.account.feature.forms.enterprise.bank.FormBankModule
import com.fatcloud.account.feature.forms.enterprise.bank.basic.FormBankBasicActivity
import com.fatcloud.account.feature.forms.enterprise.bank.basic.FormBankBasicModule
import com.fatcloud.account.feature.forms.enterprise.license.FormLicenseEnterpriseActivity
import com.fatcloud.account.feature.forms.enterprise.license.FormLicenseEnterpriseModule
import com.fatcloud.account.feature.forms.enterprise.license.basic.FormEnterpriseBasicActivity
import com.fatcloud.account.feature.forms.enterprise.license.basic.FormEnterpriseBasicModule
import com.fatcloud.account.feature.forms.master.MasterNamingActivity
import com.fatcloud.account.feature.forms.master.MasterNamingModule
import com.fatcloud.account.feature.forms.personal.bank.FormPersonalBankActivity
import com.fatcloud.account.feature.forms.personal.bank.FormPersonalBankModule
import com.fatcloud.account.feature.forms.personal.bank.basic.FormPersonalBankBasicActivity
import com.fatcloud.account.feature.forms.personal.bank.basic.FormPersonalBankBasicModule
import com.fatcloud.account.feature.forms.personal.bookkeeping.FormAgentBookkeepingPersonalActivity
import com.fatcloud.account.feature.forms.personal.bookkeeping.FormAgentBookkeepingPersonalModule
import com.fatcloud.account.feature.forms.personal.bookkeeping.signature.SignatureActivity
import com.fatcloud.account.feature.forms.personal.bookkeeping.signature.SignatureModule
import com.fatcloud.account.feature.forms.personal.change.FormLicenseChangeActivity
import com.fatcloud.account.feature.forms.personal.change.FormLicenseChangeModule
import com.fatcloud.account.feature.forms.personal.license.FormLicensePersonalActivity
import com.fatcloud.account.feature.forms.personal.license.FormLicensePersonalModule
import com.fatcloud.account.feature.forms.personal.logout.FormLicenseLogoutActivity
import com.fatcloud.account.feature.forms.personal.logout.FormLicenseLogoutModule
import com.fatcloud.account.feature.forms.personal.packages.FormPersonalPackageP9P10Activity
import com.fatcloud.account.feature.forms.personal.packages.FormPersonalPackageP9P10Module
import com.fatcloud.account.feature.forms.personal.tax.FormTaxRegistrationPersonalActivity
import com.fatcloud.account.feature.forms.personal.tax.FormTaxRegistrationPersonalModule
import com.fatcloud.account.feature.news.detail.NewsDetailActivity
import com.fatcloud.account.feature.news.detail.NewsDetailModule
import com.fatcloud.account.feature.order.details.bookkeeping.BookkeepingInfoActivity
import com.fatcloud.account.feature.order.details.bookkeeping.BookkeepingInfoModule
import com.fatcloud.account.feature.order.details.enterprise.CompanyRegisterInfoActivity
import com.fatcloud.account.feature.order.details.enterprise.CompanyRegisterInfoModule
import com.fatcloud.account.feature.order.details.personal.license.handle.PersonalLicenseHandleInfoActivity
import com.fatcloud.account.feature.order.details.personal.license.handle.PersonalLicenseHandleInfoModule
import com.fatcloud.account.feature.order.details.personal.bank.PersonalBankInfoActivity
import com.fatcloud.account.feature.order.details.personal.bank.PersonalBankInfoModule
import com.fatcloud.account.feature.order.details.personal.license.change.PersonalLicenseChangeInfoActivity
import com.fatcloud.account.feature.order.details.personal.license.change.PersonalLicenseChangeInfoModule
import com.fatcloud.account.feature.order.details.personal.license.logout.PersonalLicenseLogoutActivity
import com.fatcloud.account.feature.order.details.personal.license.logout.PersonalLicenseLogoutModule
import com.fatcloud.account.feature.order.details.personal.packages.PersonalPackageInfoP9P10Activity
import com.fatcloud.account.feature.order.details.personal.packages.PersonalPackageInfoP9P10Module
import com.fatcloud.account.feature.order.lists.OrderListActivity
import com.fatcloud.account.feature.order.lists.OrderListModule
import com.fatcloud.account.feature.order.progress.ScheduleActivity
import com.fatcloud.account.feature.order.progress.ScheduleModule
import com.fatcloud.account.feature.product.detail.ProductDetailActivity
import com.fatcloud.account.feature.product.detail.ProductDetailModule
import com.fatcloud.account.feature.upgrade.UpgradeActivity
import com.fatcloud.account.feature.upgrade.UpgradeModule
import com.fatcloud.account.feature.webs.WebCommonActivity
import com.fatcloud.account.feature.webs.WebCommonModule
import com.fatcloud.account.scope.ActivityScore
import com.fatcloud.account.wxapi.WXEntryActivity
import com.fatcloud.account.wxapi.WXPayEntryActivity
import com.fatcloud.account.wxapi.WXPayEntryModule
import com.fatcloud.account.wxapi.entry.WXEntryModule
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
    @ContributesAndroidInjector(modules = [PersonalLicenseHandleInfoModule::class])
    abstract fun registrantInfoActivityInjector(): PersonalLicenseHandleInfoActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [FormLicenseEnterpriseModule::class])
    abstract fun formLicenseEnterpriseActivityInjector(): FormLicenseEnterpriseActivity


    @ActivityScore
    @ContributesAndroidInjector(modules = [FragmentBindModule::class, FormLicensePersonalModule::class])
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


    @ActivityScore
    @ContributesAndroidInjector(modules = [FragmentBindModule::class, FormBankModule::class])
    abstract fun formBankActivityInjector(): FormBankActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [WXPayEntryModule::class])
    abstract fun wXPayEntryActivityInjector(): WXPayEntryActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [PayModule::class])
    abstract fun payActivityInjector(): PayActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [CloudPayResultModule::class])
    abstract fun payResultActivityInjector(): CloudPayResultActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [UpgradeModule::class])
    abstract fun upgradeActivityInjector(): UpgradeActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [WXEntryModule::class])
    abstract fun wXEntryActivityInjector(): WXEntryActivity


    @ActivityScore
    @ContributesAndroidInjector(modules = [PayPrepareModule::class])
    abstract fun payPrepareActivityInjector(): PayPrepareActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [WechatLoginRegisterModule::class])
    abstract fun wechatLoginRegisterActivityInjector(): WechatLoginRegisterActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [BookkeepingInfoModule::class])
    abstract fun bookkeepingInfoActivityInjector(): BookkeepingInfoActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [PayUnknownModule::class])
    abstract fun payUnknownActivityInjector(): PayUnknownActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [FormEnterpriseBasicModule::class])
    abstract fun formEnterpriseBasicActivityInjector(): FormEnterpriseBasicActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [FragmentBindModule::class, FormBankBasicModule::class])
    abstract fun formBankBasicActivityInjector(): FormBankBasicActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [FragmentBindModule::class, FormLicenseChangeModule::class])
    abstract fun formLicenseChangeActivityInjector(): FormLicenseChangeActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [FragmentBindModule::class, FormLicenseLogoutModule::class])
    abstract fun formLicenseLogoutActivityInjector(): FormLicenseLogoutActivity


    @ActivityScore
    @ContributesAndroidInjector(modules = [FragmentBindModule::class, MasterNamingModule::class])
    abstract fun masterNamingActivityInjector(): MasterNamingActivity


    @ActivityScore
    @ContributesAndroidInjector(modules = [FragmentBindModule::class, FormPersonalBankModule::class])
    abstract fun formPersonalBankActivityInjector(): FormPersonalBankActivity


    @ActivityScore
    @ContributesAndroidInjector(modules = [FragmentBindModule::class, FormPersonalBankBasicModule::class])
    abstract fun formPersonalBankBasicActivityInjector(): FormPersonalBankBasicActivity


    @ActivityScore
    @ContributesAndroidInjector(modules = [FragmentBindModule::class, PersonalBankInfoModule::class])
    abstract fun personalBankInfoActivityInjector(): PersonalBankInfoActivity


    @ActivityScore
    @ContributesAndroidInjector(modules = [PersonalLicenseChangeInfoModule::class])
    abstract fun personalLicenseChangeInfoActivityInjector(): PersonalLicenseChangeInfoActivity

    @ActivityScore
    @ContributesAndroidInjector(modules = [PersonalLicenseLogoutModule::class])
    abstract fun personalLicenseLogoutActivityInjector(): PersonalLicenseLogoutActivity


    @ActivityScore
    @ContributesAndroidInjector(modules = [FragmentBindModule::class, FormPersonalPackageP9P10Module::class])
    abstract fun formPersonalPackageCommonActivityInjector(): FormPersonalPackageP9P10Activity

    @ActivityScore
    @ContributesAndroidInjector(modules = [FragmentBindModule::class, PersonalPackageInfoP9P10Module::class])
    abstract fun personalPackageInfoActivityInjector(): PersonalPackageInfoP9P10Activity


}
