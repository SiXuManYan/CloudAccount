package com.account.entity.home

/**
 * Created by Wangsw on 2020/5/26 11:39.
 * </br>
 * 首页混合信息
 * 轮播图，产品，资讯
 */
class HomeMix {

    companion object {

        /** 混合类型 轮播图 */
        var MIX_TYPE_BANNER = 1

        /** 混合类型 热门产品 */
        var MIX_TYPE_PRODUCT = 2

        /** 混合类型 资讯 */
        var MIX_TYPE_NEWS = 3

    }

    /**
     * 混合实体dto ， 根据 mixType 判断
     * @see Banners
     * @see Product
     * @see News
     */
    var mixDto: Any? = null

    /**
     * 实体类型 1.轮播图 2.热门产品 3.资讯
     */
    var mixType = 0

}