package com.fatcloud.account.feature.forms.enterprise.bank

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/15 0015 15:09.
 * </br>
 *
 */
@Module
class FormBankModule {

    @Provides
    fun viewProvider(activity: FormBankActivity): FormBankView {
        return activity
    }


}