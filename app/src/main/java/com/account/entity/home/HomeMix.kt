package com.account.entity.home

import com.account.entity.news.News
import com.account.entity.product.Product

/**
 * Created by Wangsw on 2020/5/26 11:39.
 * </br>
 * 首页混合信息
 * 轮播图，产品，资讯
 */
class HomeMix {

    var banners: ArrayList<Banners> = ArrayList()
    var products: ArrayList<Product> = ArrayList()
    var news: ArrayList<News> = ArrayList()

}