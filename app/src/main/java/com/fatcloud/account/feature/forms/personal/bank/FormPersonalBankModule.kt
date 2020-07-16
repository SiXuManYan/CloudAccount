package com.fatcloud.account.feature.forms.personal.bank

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/7/16 0016 11:02.
 * </br>
 *
 */
@Module
class FormPersonalBankModule {

    @Provides
    fun viewProvider(activity:  FormPersonalBankActivity):  FormPersonalBankView {
        return activity
    }


}