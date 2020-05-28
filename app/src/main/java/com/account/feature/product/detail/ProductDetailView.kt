package com.account.feature.product.detail

import com.account.base.common.BaseTaskView
import com.account.base.common.BaseView
import com.account.entity.product.ProductDetail

/**
 * Created by Wangsw on 2020/5/28 0028 15:33.
 * </br>
 *
 */
interface ProductDetailView :BaseTaskView{
    fun bindDetailData(data: ProductDetail)


}