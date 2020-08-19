package com.fatcloud.account.feature.order.details.personal.bank

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.order.persional.bank.PersonalBankDetailP8
import com.fatcloud.account.entity.order.persional.bank.PersonalBankDetailP9P10
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/7/21 0021 14:49.
 * </br>
 *
 */
class PersonalBankInfoPresenter @Inject constructor(private var view: PersonalBankInfoView) : BasePresenter(view) {


    fun getDetailInfoP8(lifecycle: LifecycleOwner, orderId: String?) {


        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY, apiService.getPersonalBankInfoP8(orderId),

            object : BaseHttpSubscriber<PersonalBankDetailP8>(view) {

                override fun onSuccess(data: PersonalBankDetailP8?) {
                   data?.let {

                       view.bindDetail(it)
                   }

                }
            }
        )


    }


    fun getDetailInfoP9P10(lifecycle: LifecycleOwner, orderWorkId: String?) {


        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY, apiService.getPersonalBankInfoP9P10(orderWorkId),

            object : BaseHttpSubscriber<PersonalBankDetailP9P10>(view) {

                override fun onSuccess(data: PersonalBankDetailP9P10?) {
                    data?.let {

                        view.bindDetailP9P10(it)
                    }

                }
            }
        )


    }



}