package com.fatcloud.account.feature.defray

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/16 0016 16:02.
 * </br>
 *
 */
@Module
class PayModule {

    @Provides
    fun viewProvider(activity: PayActivity): PayView {
        return activity
    }

}