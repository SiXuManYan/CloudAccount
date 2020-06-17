package com.fatcloud.account.feature.sheet.nature

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/17 0017 22:20.
 * </br>
 *
 */
@Module
class AccountNatureSheetModule {

    @Provides
    fun viewProvider(fragment: AccountNatureSheetFragment): AccountNatureSheetView {
        return fragment
    }

}