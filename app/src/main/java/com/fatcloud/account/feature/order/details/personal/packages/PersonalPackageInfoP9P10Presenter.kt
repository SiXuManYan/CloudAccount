package com.fatcloud.account.feature.order.details.personal.packages

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.order.detail.PersonalPackageDetail
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/7/27 0027 13:56.
 * </br>
 *
 */
class PersonalPackageInfoP9P10Presenter @Inject constructor(private var p9P10View: PersonalPackageInfoP9P10View) : BasePresenter(p9P10View) {


    fun getDetailInfo(lifecycle: LifecycleOwner, orderId: String?) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getP9P10PersonalPackageInfo(orderId),

            object : BaseHttpSubscriber<PersonalPackageDetail>(p9P10View) {

                override fun onSuccess(data: PersonalPackageDetail?) {

                    data?.let {
                        p9P10View.bindDetailInfo(data)
                    }

                }
            }
        )

    }

}