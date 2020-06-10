package com.fatcloud.account.feature.forms.enterprise.license

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/10 0010 18:30.
 * </br>
 *
 */
@Module
class FormLicenseEnterpriseModule {


    @Provides
    fun viewProvider(activity: FormLicenseEnterpriseActivity): FormLicenseEnterpriseView {
        return activity
    }

}