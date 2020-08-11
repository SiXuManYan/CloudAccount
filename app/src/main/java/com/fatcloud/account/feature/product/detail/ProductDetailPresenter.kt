package com.fatcloud.account.feature.product.detail

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
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

                        // 代理记账人姓名
                        val authorizePersonName = it.authorizePersonName
                        if (authorizePersonName.isNotBlank()) {
                            CommonUtils.getShareDefault().put(Constants.SP_AUTH_PERSON_NAME, authorizePersonName)
                        }

                        val authorizePersonIdno = it.authorizePersonIdno
                        if (authorizePersonIdno.isNotBlank()) {
                            CommonUtils.getShareDefault().put(Constants.SP_AUTH_PERSON_ID_NUMBER, authorizePersonIdno)
                        }
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