package com.fatcloud.account.feature.forms.personal.packages

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/7/23 0023 10:23.
 * </br>
 *
 */
@Module
class FormPersonalPackageP9P10Module {

    @Provides
    fun viewProvider(activity: FormPersonalPackageP9P10Activity): FormPersonalPackageP9P10View {
        return activity
    }


}