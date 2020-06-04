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
     * 设置 “资讯列表”数据
     * @param news list
     * @param isFirstPage 是否是首页
     * @param isLastPage 是否为最后一页
     */
    fun bindNewsList(news: ArrayList<News>, isFirstPage: Boolean, isLastPage: Boolean)

}
