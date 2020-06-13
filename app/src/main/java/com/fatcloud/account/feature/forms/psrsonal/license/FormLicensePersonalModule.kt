package com.fatcloud.account.feature.forms.psrsonal.license

import com.fatcloud.account.feature.forms.psrsonal.license.FormLicensePersonalActivity
import com.fatcloud.account.feature.forms.psrsonal.license.FormLicensePersonalView
import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/12 0012 13:33.
 * </br>
 *
 */
@Module
class FormLicensePersonalModule {

    @Provides
    fun viewProvider(activity: FormLicensePersonalActivity): FormLicensePersonalView {
        return activity
    }
}