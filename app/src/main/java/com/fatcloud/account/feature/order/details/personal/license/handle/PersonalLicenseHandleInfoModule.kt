package com.fatcloud.account.feature.order.details.personal.license.handle

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/4 0004 14:03.
 * </br>
 *
 */
@Module
class PersonalLicenseHandleInfoModule {

    @Provides
    fun viewProvider(activity: PersonalLicenseHandleInfoActivity): PersonalLicenseHandleInfoView {
        return activity
    }


}