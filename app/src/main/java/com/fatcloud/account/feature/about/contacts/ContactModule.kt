package com.fatcloud.account.feature.about.contacts

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/1 0001 14:22.
 * </br>
 *
 */
@Module
class ContactModule {

    @Provides
    fun viewProvider(activity: ContactActivity): ContactView {
        return activity
    }


}