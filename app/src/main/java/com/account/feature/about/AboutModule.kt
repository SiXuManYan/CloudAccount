package com.account.feature.about

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/1 0001 13:26.
 * </br>
 *
 */
@Module
class AboutModule {

    @Provides
    fun viewProvider(activity: AboutActivity): AboutView {
        return activity
    }

}