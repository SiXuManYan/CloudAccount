package com.account.network

import com.account.entity.home.HomeMix
import com.account.entity.product.ProductDetail
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Wangsw on 2020/5/22 0022 15:00.
 * </br>
 * 调用的api接口
 */
interface ApiService {
    companion object {

        private const val USE_CACHED = "cache:60"

        /**
         * 云账户API
         */
        private const val API_URI = "api/account"

        /**
         * <首页>相关接口前缀
         */
        private const val HOME_API_PREFIX = "$API_URI/tHome/"

        /**
         * <产品>相关接口前缀
         */
        private const val PRODUCT_API_PREFIX = "$API_URI/tProduct/"

        /**
         * 资讯相关接口前缀
         */
        private const val NEWS_API = "$API_URI/tNews"

    }

    /**
     * 获取首页列表信息
     * @see HomeMix
     */
    @GET(HOME_API_PREFIX)
    fun getHomeList(): Flowable<Response<HomeMix>>

    /**
     * 获取首页混合列表信息
     * @param pageSize 请求的分页数量
     *                 当返回的list.size()<pageSize 时，为最后一页
     */
    @GET(PRODUCT_API_PREFIX)
    fun getProductList(@Query("pageSize") pageSize: Int): Flowable<Response<JsonArray>>

    /**
     * 产品详情页
     */
    @GET("$PRODUCT_API_PREFIX/detail")
    fun getProductDetail(@Query("productId") productId: String): Flowable<Response<ProductDetail>>


}