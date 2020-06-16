package com.fatcloud.account.feature.defray

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.defray.AliPayInfo
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

    val orderInfo: String =
        "alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2018010201524640&biz_content=%7B%22out_trade_no%22%3A%22232901te20180110550511221%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22wjy%E9%80%80%E6%AC%BE%E6%B5%8B%E8%AF%95%E4%BB%A3%E9%87%91%E5%88%B81%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F118.89.161.171%3A8080%2Fyihua-api%2Falipay%2Fcallback&sign=IeHaWz3c8ZZ082bl1Lt4haly4SvnKt6rdDzaIi6xgqMfn1Z1LJ3UaLfMk7SMOtiGsImLIaPIX3SeMNSMYytJDfb4PxD274T%2FkVfqqCGHIEaWXiJDnVAzM%2BaHSFSC5L5tNqAca8PtCELsyWJPDo9thx8QFihWOfWpWrkHC%2FDTVoybbSd2qLK2eaY9rbtpHiU%2FcEr0%2Bf9dPiliyv6LTnc85rDAJKHNAOtk%2FLBgA0QTx5Wu1ccsuq6ZoV58PWLJLc2%2FuHKtkR%2FNdXYoVRVHDMUPjlxmfTEFBj2NoWafNhha60yOH8ZnfGZ2QJgjS5j%2FUr5za83vR8EjMcoQhydrERWumg%3D%3D&sign_type=RSA2&timestamp=2018-01-10+10%3A15%3A28&version=1.0"

    /**
     * 支付宝统一下单
     */
    fun alipayUnifiedOrder(lifecycleOwner: LifecycleOwner, orderId: String) {
        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.alipayUnifiedOrder(orderId), object : BaseHttpSubscriber<AliPayInfo>(view) {
                override fun onSuccess(data: AliPayInfo?) {
                    data?.let {
                        view.doAliPay(data.orderInfo)
                    }
                }
            })
    }

    /**
     * 检查订单是否已经支付过
     */
    fun checkOrderRealPaymentStatus(lifecycleOwner: LifecycleOwner, orderId: String, orderNumber: String) {
        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.checkOrderRealPaymentStatus(id = orderId, orderNo = orderNumber), object : BaseHttpSubscriber<AliPayInfo>(view) {
                override fun onSuccess(data: AliPayInfo?) {
                    view.orderPaySuccess()
                }
            })
    }


}