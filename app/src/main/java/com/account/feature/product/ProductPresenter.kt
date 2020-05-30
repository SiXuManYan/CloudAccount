package com.account.feature.product

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.account.base.common.BasePresenter
import com.account.base.net.BaseJsonArrayHttpSubscriber
import com.account.entity.product.Product2
import com.google.gson.JsonArray
import java.util.ArrayList
import javax.inject.Inject


class ProductPresenter @Inject constructor(private var productView: ProductView) : BasePresenter(productView) {


    override fun loadList(lifecycle: LifecycleOwner, page: Int, pageSize: Int, lastItemId: String?) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getProductList(pageSize, lastItemId),

            object : BaseJsonArrayHttpSubscriber<Product2>(productView, false) {


                override fun onSuccess(jsonArray: JsonArray?, list: ArrayList<Product2>, lastItemId: String?) {
                    productView.bindList(list, lastItemId)
                }

            }


        )

    }

}
