package com.fatcloud.account.feature.order.details.personal.license.change

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/7/22 0022 11:41.
 * </br>
 *
 */
@Module
class PersonalLicenseChangeInfoModule {


    @Provides
    fun viewProvider(activity:  PersonalLicenseChangeInfoActivity): PersonalLicenseChangeInfoView {
        return activity
    }

}