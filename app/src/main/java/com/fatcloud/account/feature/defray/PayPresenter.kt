package com.fatcloud.account.feature.defray

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.defray.WechatPayInfo
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/16 0016 16:02.
 * </br>
 *
 */
class PayPresenter @Inject constructor(private var view: PayView) : BasePresenter(view) {


    /**
     * 微信统一下单
     */
    fun wechatUnifiedOrder(lifecycleOwner: LifecycleOwner, orderId: String) {


        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.wechatUnifiedOrder(orderId), object : BaseHttpSubscriber<WechatPayInfo>(view) {
                override fun onSuccess(data: WechatPayInfo?) {
                    data?.let {
                        view.doWechatPay(it)

                    }

                }
            })

    }

    /**
     * 支付宝统一下单
     */
    fun alipayUnifiedOrder(lifecycleOwner: LifecycleOwner, orderId: String) {

    }


}