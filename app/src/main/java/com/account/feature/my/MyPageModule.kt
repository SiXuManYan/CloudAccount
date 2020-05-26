package com.account.feature.my

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/5/25 0025 17:06.
 * </br>
 *
 */
@Module
class MyPageModule {

    @Provides
    fun viewProvider(fragment: MyPageFragment): MyPageView {
        return fragment
    }


}