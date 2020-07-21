package com.fatcloud.account.feature.order.details.personal.bank

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/7/21 0021 14:50.
 * </br>
 *
 */
@Module
class PersonalBankInfoModule {

    @Provides
    fun viewProvider(activity: PersonalBankInfoActivity): PersonalBankInfoView {
        return activity
    }

}