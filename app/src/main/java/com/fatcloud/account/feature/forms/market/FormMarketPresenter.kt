package com.fatcloud.account.feature.forms.market

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.form.MarketData
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/8/5 0005 13:33.
 * </br>
 *
 */
class FormMarketPresenter @Inject constructor(private var view: FormMarketView) : BasePresenter(view) {


    fun getMarketInfo(lifecycle: LifecycleOwner, orderId: String?) {

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getMarketInfo(orderId),
            object : BaseHttpSubscriber<MarketData>(view) {
                override fun onSuccess(data: MarketData?) {

                    data?.let {
                        view.bindPageInfo(it)
                    }

                }

            }
        )
    }


    private val gson = Gson()

    fun addMarket(lifecycle: LifecycleOwner, currentOrderId: String?, account: String?, password: String?) {


        val model = MasterNaming().apply {
            govCnAccount = account
            govCnPassword = password
            orderId = currentOrderId
        }

        val bodyJsonStr = gson.toJson(model)
        val jsonObject: JsonObject = gson.fromJson(bodyJsonStr, JsonObject::class.java)

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY, apiService.addMarketInfo(jsonObject), object : BaseHttpSubscriber<JsonElement>(view) {
            override fun onSuccess(data: JsonElement?) {

            }
        })
    }


    class MasterNaming {
        var govCnAccount: String? = ""
        var govCnPassword: String? = ""
        var orderId: String? = ""

    }

}