package com.fatcloud.account.feature.order.details.personal.license.change

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.order.detail.PersonalLicenseChangeDetail
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/7/22 0022 11:41.
 * </br>
 *
 */
class PersonalLicenseChangeInfoPresenter @Inject constructor(private var view: PersonalLicenseChangeInfoView) : BasePresenter(view) {



    fun getDetailInfo(lifecycle: LifecycleOwner, orderId: String?) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getPersonalLicenseChangeInfo(orderId),

            object : BaseHttpSubscriber<PersonalLicenseChangeDetail>(view){

                override fun onSuccess(data: PersonalLicenseChangeDetail?) {

                    data?.let {
                        view.bindDetailInfo(data)
                    }

                }
            }
        )

    }


}