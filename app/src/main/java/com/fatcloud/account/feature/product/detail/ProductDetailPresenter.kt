package com.fatcloud.account.feature.product.detail

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.product.Price
import com.fatcloud.account.entity.product.ProductDetail
import com.fatcloud.account.entity.users.User
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/5/28 0028 15:33.
 * </br>
 *
 */
class ProductDetailPresenter @Inject constructor(private var productView: ProductDetailView) : BasePresenter(productView) {

    fun getDetail(lifecycle: LifecycleOwner, productId: String) {

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getProductDetail(productId),
            object : BaseHttpSubscriber<ProductDetail>(productView, false) {
                override fun onSuccess(data: ProductDetail?) {

                    data?.let {
                        productView.bindDetailData(it)
                    }
                }

            })

    }


    fun getNewsUnreadCount(lifecycleOwner: LifecycleOwner) {
        if (!User.isLogon()) {
            return
        }
        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY, apiService.getNewsUnreadCount(), object : BaseHttpSubscriber<JsonElement>(productView) {
            override fun onSuccess(data: JsonElement?) {
                if (data == null) {
                    return
                }

                if (data.isJsonPrimitive) {
                    val messageUnReadNumber = data.asLong
                    productView.updateMessageUnReadNumber(messageUnReadNumber)

                }
            }
        })
    }


}