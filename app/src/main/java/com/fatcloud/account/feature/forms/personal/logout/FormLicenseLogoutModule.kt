package com.fatcloud.account.feature.forms.personal.logout

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/7/13 0013 16:38.
 * </br>
 *
 */
@Module
class FormLicenseLogoutModule {

    @Provides
    fun viewProvider(activity: FormLicenseLogoutActivity): FormLicenseLogoutView {
        return activity
    }

}