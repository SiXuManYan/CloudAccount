package com.fatcloud.account.feature.order.details.master

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.order.detail.MasterNamingDetail
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/8/20 0020 13:25.
 * </br>
 *
 */
class MasterNamingInfoPresenter @Inject constructor(private var view: MasterNamingInfoView) : BasePresenter(view)  {



    fun getDetailInfo(lifecycle: LifecycleOwner, orderId: String?) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getMasterNamingInfo(orderId),

            object : BaseHttpSubscriber<MasterNamingDetail>(view){

                override fun onSuccess(data: MasterNamingDetail?) {

                    data?.let {
                        view.bindDetailInfo(data)
                    }

                }
            }
        )

    }

}