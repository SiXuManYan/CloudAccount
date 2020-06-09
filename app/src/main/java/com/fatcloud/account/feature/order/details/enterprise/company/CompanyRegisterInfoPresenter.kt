package com.fatcloud.account.feature.order.details.enterprise.company

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo
import com.fatcloud.account.entity.order.persional.PersonalInfo
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/4 0004 14:03.
 * </br>
 *  公司详情信息
 */
class CompanyRegisterInfoPresenter @Inject constructor(private var companyRegisterInfoView: CompanyRegisterInfoView) :
    BasePresenter(companyRegisterInfoView) {


    fun getEnterpriseInfo(
        lifecycleOwner: LifecycleOwner,
        orderWorkId: String?,
        productWorkType: String?,
        orderId: String?
    ) {
        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.getEnterpriseOrderDetail(orderWorkId),

            object : BaseHttpSubscriber<EnterpriseInfo>(companyRegisterInfoView) {



                override fun onSuccess(data: EnterpriseInfo?) {

                    data?.let {
                        companyRegisterInfoView.bindDetailInfo(data)
                    }

                    if (productWorkType == Constants.PW3) {
                        getRegistrantInfo(lifecycleOwner, orderId)
                    }


                }
            }
        )


    }


    fun getRegistrantInfo(lifecycle: LifecycleOwner, orderId: String?) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getPersonalOrderDetail(orderId),

            object : BaseHttpSubscriber<PersonalInfo>(companyRegisterInfoView) {

                override fun onSuccess(data: PersonalInfo?) {

                    data?.let {
                        companyRegisterInfoView.bindShareholdersInfo(data)
                    }

                }
            }
        )

    }


}