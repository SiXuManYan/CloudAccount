package com.fatcloud.account.feature.order.lists

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseJsonArrayHttpSubscriber
import com.fatcloud.account.entity.order.Order
import com.fatcloud.account.feature.order.lists.OrderListView
import com.google.gson.JsonArray
import java.util.*
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/3 0003 17:44.
 * </br>
 *
 */
class OrderListPresenter @Inject constructor(private var orderListView: OrderListView) : BasePresenter(orderListView) {

    override fun loadList(lifecycle: LifecycleOwner, page: Int, pageSize: Int, lastItemId: String?) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getOrderList(pageSize, lastItemId),

            object : BaseJsonArrayHttpSubscriber<Order>(orderListView, false) {

                override fun onSuccess(jsonArray: JsonArray?, list: ArrayList<Order>, lastItemId: String?) {
                    orderListView.bindList(list, lastItemId)
                }
            }
        )

    }

}