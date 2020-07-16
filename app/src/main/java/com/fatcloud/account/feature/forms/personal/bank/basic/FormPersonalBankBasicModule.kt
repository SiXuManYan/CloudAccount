package com.fatcloud.account.feature.forms.personal.bank.basic

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/7/16 0016 11:53.
 * </br>
 *
 */
@Module
class FormPersonalBankBasicModule {

    @Provides
    fun viewProvider(activity:  FormPersonalBankBasicActivity):  FormPersonalBankBasicView {
        return activity
    }

}