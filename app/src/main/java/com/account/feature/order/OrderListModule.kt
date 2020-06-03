package com.account.feature.order

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/6/3 0003 17:43.
 * </br>
 *
 */
@Module
class OrderListModule {

    @Provides
    fun viewProvider(orderListActivity: OrderListActivity): OrderListView {
        return orderListActivity
    }


}