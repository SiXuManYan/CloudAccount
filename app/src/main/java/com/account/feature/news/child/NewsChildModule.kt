package com.account.feature.news.child

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/5/25 0025 17:01.
 * </br>
 *
 */
@Module
class NewsChildModule {

    @Provides
    fun viewProvider(fragment: NewsChildFragment): NewsChildView {
        return fragment
    }

}