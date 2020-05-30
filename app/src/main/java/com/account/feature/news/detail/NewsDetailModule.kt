package com.account.feature.news.detail

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/5/30 0030 16:34.
 * </br>
 *
 */
@Module
class NewsDetailModule {

    @Provides
    fun viewProvider(activity: NewsDetailActivity): NewsDetailView {
        return activity
    }


}