package com.fatcloud.account.feature.order.details.bookkeeping

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.order.persional.PersonalInfo
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/19 0019 18:23.
 * </br>
 *
 */
class BookkeepingInfoPresenter @Inject constructor(private var view: BookkeepingInfoView) : BasePresenter(view) {



    fun getRegistrantInfo(lifecycle: LifecycleOwner, orderId: String?) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getPersonalOrderDetail(orderId),

            object : BaseHttpSubscriber<PersonalInfo>(view){

                override fun onSuccess(data: PersonalInfo?) {

                    data?.let {
                        view.bindDetailInfo(data)
                    }

                }
            }
        )

    }

}