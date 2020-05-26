package com.account.feature.news

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/5/25 0025 17:01.
 * </br>
 *
 */
@Module
class NewsModule {

    @Provides
    fun viewProvider(fragment: NewsFragment): NewsView {
        return fragment
    }

}