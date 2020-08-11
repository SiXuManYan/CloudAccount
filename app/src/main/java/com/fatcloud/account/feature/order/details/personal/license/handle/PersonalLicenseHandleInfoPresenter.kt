package com.fatcloud.account.feature.order.details.personal.license.handle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.order.detail.PersonalLicenseHandleDetail
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/4 0004 14:03.
 * </br>
 *
 */
class PersonalLicenseHandleInfoPresenter @Inject constructor(private var personalLicenseHandleInfoView: PersonalLicenseHandleInfoView) : BasePresenter(personalLicenseHandleInfoView) {



     fun getRegistrantInfo(lifecycle: LifecycleOwner, orderId: String?) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getPersonalLicenseDetail(orderId),

            object :BaseHttpSubscriber<PersonalLicenseHandleDetail>(personalLicenseHandleInfoView){

                override fun onSuccess(data: PersonalLicenseHandleDetail?) {

                    data?.let {
                        personalLicenseHandleInfoView.bindDetailInfo(data)
                    }

                }
            }
        )

    }


}