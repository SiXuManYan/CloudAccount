package com.fatcloud.account.feature.forms.enterprise.license.basic

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/29 0029 17:04.
 * </br>
 *
 */
@Module
class FormEnterpriseBasicModule {

    @Provides
    fun viewProvider(activity: FormEnterpriseBasicActivity): FormEnterpriseBasicView {
        return activity
    }


}