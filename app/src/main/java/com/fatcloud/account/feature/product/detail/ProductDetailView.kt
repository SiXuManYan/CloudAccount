package com.fatcloud.account.feature.product.detail

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.product.ProductDetail

/**
 * Created by Wangsw on 2020/5/28 0028 15:33.
 * </br>
 *
 */
interface ProductDetailView :BaseTaskView{
    fun bindDetailData(data: ProductDetail)
    fun updateMessageUnReadNumber(messageUnReadNumber: Long)

}