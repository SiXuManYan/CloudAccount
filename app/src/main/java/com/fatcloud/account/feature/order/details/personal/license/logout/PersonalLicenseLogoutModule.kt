package com.fatcloud.account.feature.order.details.personal.license.logout

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/7/22 0022 15:28.
 * </br>
 *
 */
@Module
class PersonalLicenseLogoutModule {


    @Provides
    fun viewProvider(activity: PersonalLicenseLogoutActivity): PersonalLicenseLogoutView {
        return activity
    }

}