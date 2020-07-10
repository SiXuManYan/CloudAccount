package com.fatcloud.account.feature.forms.personal.change

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/7/10 0010 15:37.
 * </br>
 *
 */
@Module
class FormLicenseChangeModule {

    @Provides
    fun viewProvider(activity: FormLicenseChangeActivity): FormLicenseChangeView {
        return activity
    }
}
