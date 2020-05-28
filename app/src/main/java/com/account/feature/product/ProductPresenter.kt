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


    override fun loadListWithPageSize(lifecycle: LifecycleOwner, page: Int, pageSize: Int) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getProductList(pageSize),

            object : BaseJsonArrayHttpSubscriber<Product2>(productView, false) {
                override fun onSuccess(jsonObject: JsonArray?, list: ArrayList<Product2>) {
                    productView.bindList(list, page == 0, (list.size < pageSize))
                }

            }


        )

    }

}
