package com.fatcloud.account.feature.forms.personal.packages.common

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/7/23 0023 10:23.
 * </br>
 *
 */
@Module
class FormPersonalPackageCommonModule {

    @Provides
    fun viewProvider(activity: FormPersonalPackageCommonActivity): FormPersonalPackageCommonView {
        return activity
    }


}