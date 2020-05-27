package com.account.feature.home

import com.account.base.common.BaseView
import com.account.entity.home.Banners
import com.account.entity.home.News
import com.account.entity.home.Product
import java.util.ArrayList

interface HomeView : BaseView {

    /**
     * 设置Banner & 热门产品数据
     */
    fun setHeaderDada(banners: ArrayList<Banners>, products: ArrayList<Product>)


    /**
     * 设置 资讯列表数据
     * @param news list
     * @param isFirstPage 是否是首页
     * @param isLastPage 是否为最后一页
     */
    fun bindNewsList(news: ArrayList<News>, isFirstPage: Boolean, isLastPage: Boolean)

}
