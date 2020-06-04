package com.fatcloud.account.feature.product.detail

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.product.ProductDetail
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


}