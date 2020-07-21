package com.fatcloud.account.feature.order.details.personal.bank

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.order.persional.PersonalInfo
import com.fatcloud.account.entity.order.persional.bank.PersonalBankDetail
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/7/21 0021 14:49.
 * </br>
 *
 */
class PersonalBankInfoPresenter @Inject constructor(private var view: PersonalBankInfoView) : BasePresenter(view) {


    fun getDetailInfo(lifecycle: LifecycleOwner, orderId: String?) {


        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getPersonalBankInfo(orderId),

            object : BaseHttpSubscriber<PersonalBankDetail>(view) {

                override fun onSuccess(data: PersonalBankDetail?) {
                   data?.let {

                       view.bindDetail(it)
                   }

                }
            }
        )


    }


}