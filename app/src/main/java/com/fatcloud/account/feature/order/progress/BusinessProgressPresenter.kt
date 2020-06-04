package com.fatcloud.account.feature.order.progress

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseListHttpSubscriber
import com.fatcloud.account.entity.order.progress.BusinessProgress
import com.google.gson.JsonObject
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/4 0004 11:59.
 * </br>
 *
 */
class BusinessProgressPresenter @Inject constructor(private var businessProgressView: BusinessProgressView) : BasePresenter(businessProgressView) {

    fun getProgressData(lifecycleOwner: LifecycleOwner, orderId: String?) {

        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.getBusinessProgress(orderId),

            object : BaseListHttpSubscriber<BusinessProgress>("orderWorks", businessProgressView, false) {
                override fun onSuccess(jsonObject: JsonObject, list: ArrayList<BusinessProgress>, lastItemId: String?) {
                    businessProgressView.bindProgressData(list);
                }
            }
        )


    }


}