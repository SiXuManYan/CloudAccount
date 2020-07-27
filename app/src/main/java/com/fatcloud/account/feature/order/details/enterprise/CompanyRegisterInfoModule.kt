package com.fatcloud.account.feature.order.details.enterprise

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/4 0004 14:03.
 * </br>
 *
 */
@Module
class CompanyRegisterInfoModule {
    @Provides
    fun viewProvider(activityRegister: CompanyRegisterInfoActivity): CompanyRegisterInfoView {
        return activityRegister
    }


}