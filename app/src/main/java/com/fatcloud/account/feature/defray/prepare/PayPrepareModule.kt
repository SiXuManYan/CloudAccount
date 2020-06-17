package com.fatcloud.account.feature.defray.prepare

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/17 0017 18:49.
 * </br>
 *
 */
@Module
class PayPrepareModule {

    @Provides
    fun viewProvider(activity: PayPrepareActivity): PayPrepareView {
        return activity
    }

}