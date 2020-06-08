package com.fatcloud.account.feature.order.progress

import android.text.TextUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseJsonArrayHttpSubscriber
import com.fatcloud.account.entity.order.progress.BusinessProgress
import com.google.gson.JsonArray
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by Wangsw on 2020/6/4 0004 11:59.
 * </br>
 *
 */
class SchedulePresenter @Inject constructor(private var scheduleView: ScheduleView) : BasePresenter(scheduleView) {

    fun getProgressData(lifecycleOwner: LifecycleOwner, orderId: String?) {

        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.getBusinessProgress(orderId),

            object : BaseJsonArrayHttpSubscriber<BusinessProgress>(scheduleView, false) {

                override fun onSuccess(jsonArray: JsonArray?, list: ArrayList<BusinessProgress>, lastItemId: String?) {

                    val newList: ArrayList<BusinessProgress> = ArrayList()

                    list.forEach {
                        if (!TextUtils.isEmpty(it.productWorkName)) {
                            newList.add(it)
                        }
                    }
                    scheduleView.bindList(newList, null)
                }
            }
        )


    }


}