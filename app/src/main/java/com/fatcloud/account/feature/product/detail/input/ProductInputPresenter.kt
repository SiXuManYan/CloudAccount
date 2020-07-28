package com.fatcloud.account.feature.product.detail.input

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.google.gson.Gson
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/7/28 0028 9:37.
 * </br>
 *
 */
class ProductInputPresenter @Inject constructor(private var view: ProductInputView) : BasePresenter(view) {

    private val gson = Gson()

    fun addEmployedTaxAssessmentP11(lifecycle: LifecycleOwner, name: String, finalMoney: String, productId: String?, priceId: String?) {

        val model = EmployedTaxAssessmentP11().apply {
            businessLicenseName = name
            money = finalMoney
            this.productId = productId
            this.productPriceId = priceId
        }

        val bodyJsonStr = gson.toJson(model)
        val jsonObject: JsonObject = gson.fromJson(bodyJsonStr, JsonObject::class.java)

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY, apiService.addEmployedTaxAssessmentP11(jsonObject), object : BaseHttpSubscriber<PreparePay>(view) {
            override fun onSuccess(data: PreparePay?) {
                data?.let {
                    view.commitSuccess(it)
                }

            }
        })
    }

    class EmployedTaxAssessmentP11 {
        var businessLicenseName: String? = null
        var money: String? = null
        var productId: String? = null
        var productPriceId: String? = null
    }

}