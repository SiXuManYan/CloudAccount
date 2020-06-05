package com.fatcloud.account.feature.order.details.personal

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/4 0004 14:03.
 * </br>
 *
 */
@Module
class PersonalOrderDetailModule {
    @Provides
    fun viewProvider(activity: PersonalOrderDetailActivity): PersonalOrderDetailView {
        return activity
    }


}