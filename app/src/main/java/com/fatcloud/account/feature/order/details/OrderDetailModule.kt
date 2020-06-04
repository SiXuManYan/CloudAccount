package com.fatcloud.account.feature.order.details

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/4 0004 10:14.
 * </br>
 *
 */
@Module
class OrderDetailModule {

    @Provides
    fun viewProvider(orderDetailActivity: OrderDetailActivity): OrderDetailView {
        return orderDetailActivity
    }



}