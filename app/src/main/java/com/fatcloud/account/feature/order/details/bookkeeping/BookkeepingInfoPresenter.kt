package com.fatcloud.account.feature.order.details.bookkeeping

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.order.detail.BookkeepingDetail
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
            apiService.getBookkeepingDetail(orderId),

            object : BaseHttpSubscriber<BookkeepingDetail>(view){

                override fun onSuccess(data: BookkeepingDetail?) {

                    data?.let {
                        view.bindDetailInfo(data)
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                }


            }
        )

    }

}