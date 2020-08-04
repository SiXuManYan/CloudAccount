package com.fatcloud.account.wxapi.pay

import com.fatcloud.account.wxapi.WXPayEntryActivity
import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/16 0016 15:51.
 * </br>
 *
 */
@Module
class WXPayEntryModule {

    @Provides
    fun viewProvider(activity: WXPayEntryActivity): WXPayEntryView {
        return activity
    }

}