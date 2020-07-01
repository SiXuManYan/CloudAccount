package com.fatcloud.account.feature.forms.enterprise.bank.basic

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/7/1 0001 15:42.
 * </br>
 *
 */
@Module
class FormBankBasicModule {


    @Provides
    fun viewProvider(activity: FormBankBasicActivity): FormBankBasicView {
        return activity
    }


}