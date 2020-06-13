package com.fatcloud.account.feature.forms.psrsonal.bookkeeping.wordpad

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/13 0013 18:27.
 * </br>
 *
 */
@Module
class WordpadModule {


    @Provides
    fun viewProvider(fragment: WordpadFragment): WordpadView {
        return fragment
    }


}