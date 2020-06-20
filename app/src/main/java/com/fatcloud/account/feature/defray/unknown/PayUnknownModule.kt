package com.fatcloud.account.feature.defray.unknown

import com.fatcloud.account.feature.defray.PayActivity
import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/20 0020 9:06.
 * </br>
 *
 */
@Module
class PayUnknownModule {
    @Provides
    fun viewProvider(activity: PayUnknownActivity): PayUnknownView {
        return activity
    }

}