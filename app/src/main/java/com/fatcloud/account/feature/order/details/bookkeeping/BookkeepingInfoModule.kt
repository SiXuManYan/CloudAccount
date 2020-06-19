package com.fatcloud.account.feature.order.details.bookkeeping

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/19 0019 18:23.
 * </br>
 *
 */
@Module
class BookkeepingInfoModule {

    @Provides
    fun viewProvider(activity: BookkeepingInfoActivity): BookkeepingInfoView {
        return activity
    }


}