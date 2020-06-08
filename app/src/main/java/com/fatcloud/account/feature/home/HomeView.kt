package com.fatcloud.account.feature.home

import com.fatcloud.account.base.common.BaseView
import com.fatcloud.account.entity.home.Banners
import com.fatcloud.account.entity.news.News
import com.fatcloud.account.entity.product.Product
import java.util.ArrayList

interface HomeView : BaseView {

    /**
     * 设置“首页Banner” 以及 “热门产品”数据
     */
    fun setHeaderDada(banners: ArrayList<Banners>, products: ArrayList<Product>)




    /**
     * 绑定列表数据，并且记录最后一项 itemId
     * @param list 列表数据
     * @param lastItemId 最后一项
     */
    fun bindList(list: ArrayList<News>, lastItemId: String? = null)

}
