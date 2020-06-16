package com.fatcloud.account.wxapi

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/16 0016 15:51.
 * </br>
 *
 */
@Module
class WXEntryModule {

    @Provides
    fun viewProvider(activity: WXEntryActivity): WXEntryView {
        return activity
    }

}