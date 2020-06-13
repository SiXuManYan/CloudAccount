package com.fatcloud.account.feature.forms.psrsonal.bookkeeping

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/13 0013 15:40.
 * </br>
 *
 */
@Module
class FormAgentBookkeepingPersonalModule {


    @Provides
    fun viewProvider(activity: FormAgentBookkeepingPersonalActivity): FormAgentBookkeepingPersonalView {
        return activity
    }

}