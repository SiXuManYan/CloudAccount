package com.fatcloud.account.feature.order.details.personal

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.base.net.BaseJsonArrayHttpSubscriber
import com.fatcloud.account.entity.order.Order
import com.fatcloud.account.entity.order.PersonalOrderDetail
import com.google.gson.JsonArray
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/4 0004 14:03.
 * </br>
 *
 */
class RegistrantInfoPresenter @Inject constructor(private var registrantInfoView: RegistrantInfoView) :
    BasePresenter(registrantInfoView) {



     fun getRegistrantInfo(lifecycle: LifecycleOwner, orderId: String?) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getPersonalOrderDetail(orderId),

            object :BaseHttpSubscriber<PersonalOrderDetail>(registrantInfoView){

                override fun onSuccess(data: PersonalOrderDetail?) {

                    data?.let {
                        registrantInfoView.bindDetailInfo(data)
                    }

                }
            }
        )

    }


}