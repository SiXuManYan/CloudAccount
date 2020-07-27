package com.fatcloud.account.feature.order.details.personal.packages

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/7/27 0027 13:56.
 * </br>
 *
 */
@Module
class PersonalPackageInfoModule {

    @Provides
    fun viewProvider(activity: PersonalPackageInfoActivity): PersonalPackageInfoView {
        return activity
    }

}