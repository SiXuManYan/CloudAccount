package com.fatcloud.account.feature.sheet.form

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/17 0017 20:37.
 * </br>
 *
 */
@Module
class FormSheetModule {
    @Provides
    fun viewProvider(fragment: FormSheetFragment): FormSheetView {
        return fragment
    }

}