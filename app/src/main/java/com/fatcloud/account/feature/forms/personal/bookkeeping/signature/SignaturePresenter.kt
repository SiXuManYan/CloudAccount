package com.fatcloud.account.feature.forms.personal.bookkeeping.signature

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.defray.prepare.PreparePay
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/13 0013 16:59.
 * </br>
 *
 */
class SignaturePresenter @Inject constructor(private var view: SignatureView) : BasePresenter(view) {


    /**
     * 添加个体代理记账
     */
    fun addAgentBookkeeping(
        lifecycle: LifecycleOwner,
        money: String?,
        productId: String?,
        productPriceId: String?,
        legalPersonName: String?,
        phone: String?,
        idno: String?,
        shopName: String?,
        businessLicenseImgUrl: String?,
        signImgUrl: String
    ) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.addAgentBookkeeping(
                money,
                productId,
                productPriceId,
                legalPersonName,
                phone,
                idno,
                shopName,
                businessLicenseImgUrl,
                signImgUrl
            ),
            object : BaseHttpSubscriber<PreparePay>(view) {
                override fun onSuccess(data: PreparePay?) {

                    data?.let {
                        view.addAgentBookkeepingSuccess(it)
                    }

                }
            }
        )
    }


}