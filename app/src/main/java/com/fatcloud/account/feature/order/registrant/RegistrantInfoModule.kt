package com.fatcloud.account.feature.order.registrant

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/4 0004 14:03.
 * </br>
 *
 */
@Module
class RegistrantInfoModule {
    @Provides
    fun viewProvider(activity: RegistrantInfoActivity): RegistrantInfoView {
        return activity
    }


}