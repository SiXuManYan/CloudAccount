package com.fatcloud.account.feature.order.details.personal.license.logout

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.order.detail.PersonalLicenseLogoutDetail
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/7/22 0022 15:26.
 * </br>
 *
 */
class PersonalLicenseLogoutPresenter  @Inject constructor(private var view: PersonalLicenseLogoutView) : BasePresenter(view){
    fun getDetailInfo(lifecycle: LifecycleOwner, orderId: String?) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getPersonalLicenseLogout(orderId),

            object : BaseHttpSubscriber<PersonalLicenseLogoutDetail>(view){

                override fun onSuccess(data: PersonalLicenseLogoutDetail?) {

                    data?.let {
                        view.bindDetailInfo(data)
                    }

                }
            }
        )

    }

}