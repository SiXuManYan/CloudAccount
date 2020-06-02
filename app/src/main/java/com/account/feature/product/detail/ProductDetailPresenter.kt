package com.account.feature.product.detail

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.account.base.common.BasePresenter
import com.account.base.net.BaseHttpSubscriber
import com.account.entity.product.ProductDetail
import java.util.ArrayList
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