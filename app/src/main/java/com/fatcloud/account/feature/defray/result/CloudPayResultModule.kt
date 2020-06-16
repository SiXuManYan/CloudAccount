package com.fatcloud.account.feature.defray.result

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/16 0016 20:58.
 * </br>
 *
 */
@Module
class CloudPayResultModule {

    @Provides
    fun viewProvider(activity: CloudPayResultActivity): CloudPayResultView {
        return activity
    }

}