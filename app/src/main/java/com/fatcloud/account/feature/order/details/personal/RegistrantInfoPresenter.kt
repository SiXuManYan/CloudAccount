package com.fatcloud.account.feature.order.details.personal

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.order.persional.PersonalInfo
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

            object :BaseHttpSubscriber<PersonalInfo>(registrantInfoView){

                override fun onSuccess(data: PersonalInfo?) {

                    data?.let {
                        registrantInfoView.bindDetailInfo(data)
                    }

                }
            }
        )

    }


}