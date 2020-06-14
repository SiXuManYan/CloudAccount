package com.fatcloud.account.feature.forms.personal.bookkeeping.signature

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/13 0013 16:58.
 * </br>
 *
 */
@Module
class SignatureModule {

    @Provides
    fun viewProvider(activity: SignatureActivity): SignatureView {
        return activity
    }

}