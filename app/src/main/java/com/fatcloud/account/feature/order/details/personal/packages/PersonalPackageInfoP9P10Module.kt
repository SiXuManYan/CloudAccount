package com.fatcloud.account.feature.order.details.personal.packages

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/7/27 0027 13:56.
 * </br>
 *
 */
@Module
class PersonalPackageInfoP9P10Module {

    @Provides
    fun viewProvider(activity: PersonalPackageInfoP9P10Activity): PersonalPackageInfoP9P10View {
        return activity
    }

}