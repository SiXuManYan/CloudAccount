package com.fatcloud.account.feature.message

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/7/30 0030 16:00.
 * </br>
 *
 */
@Module
class MessageModule {

    @Provides
    fun viewProvider(activity: MessageActivity): MessageView {
        return activity
    }


}