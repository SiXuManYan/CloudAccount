package com.fatcloud.account.feature.forms.personal.tax

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/13 0013 13:46.
 * </br>
 *
 */
@Module
class FormTaxRegistrationPersonalModule {

    @Provides
    fun viewProvider(activity: FormTaxRegistrationPersonalActivity): FormTaxRegistrationPersonalView {
        return activity
    }


}