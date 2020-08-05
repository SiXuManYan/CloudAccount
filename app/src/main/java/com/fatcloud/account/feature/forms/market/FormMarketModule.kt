package com.fatcloud.account.feature.forms.market

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/8/5 0005 13:35.
 * </br>
 *
 */
@Module
class FormMarketModule {


    @Provides
    fun viewProvider(activity: FormMarketActivity): FormMarketView {
        return activity
    }

}