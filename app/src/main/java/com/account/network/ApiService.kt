package com.account.network

import com.account.entity.home.HomeMix
import com.account.entity.news.NewDetail
import com.account.entity.news.NewsCategory
import com.account.entity.news.News
import com.account.entity.product.ProductDetail
import com.account.entity.users.User
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.POST
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
         *
         */
        private const val NEWS_API = "$API_URI/tNews/"

        /**
         * 账户相关接口前缀
         */
        private const val ACCOUNT_API = "$API_URI/tAccount"


        /**
         * 订单相关
         */
        private const val ORDER_API = "$API_URI/tOrder/"

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
     * @param tailId 末尾项ID 用于分页
     */
    @GET(PRODUCT_API_PREFIX)
    fun getProductList(
        @Query("pageSize") pageSize: Int,
        @Query("tailId") tailId: String? = null
    ): Flowable<Response<JsonArray>>

    /**
     * 产品详情页
     *
     */
    @GET("$PRODUCT_API_PREFIX/detail")
    fun getProductDetail(@Query("productId") productId: String): Flowable<Response<ProductDetail>>

    /**
     * 获取资讯页tab
     * @see NewsCategory
     * @see <a href="http://192.168.1.139:8881/api/account/tNews/mold">点击查看接口返回</a>
     */
    @GET("$NEWS_API/mold")
    fun getNewsCategory(): Flowable<Response<JsonArray>>

    /**
     * 获取资讯页tab
     * @see NewsCategory
     * @see News
     * @see <a href="http://192.168.1.191:8881/api/account/tNews/?pageSize=10">资讯列表</a>
     * @param tailId 最后一项item id
     * @param mold 资讯列别，即：资讯tab列表中的 value 字段
     *
     */
    @GET(NEWS_API)
    fun getNewsList(
        @Query("pageSize") pageSize: Int,
        @Query("tailId") tailId: String? = null,
        @Query("mold") mold: String? = null
    ): Flowable<Response<JsonArray>>

    /**
     * 获取资讯页详情
     * http://192.168.1.191:8881/api/account/tNews/detail?newsId=1265846914897674240
     *
     */
    @GET("$NEWS_API/detail")
    fun getNewsDetail(@Query("newsId") newsId: String?): Flowable<Response<NewDetail>>

    /**
     * 校验账户是否存在
     * @Field
     */
    @POST("$ACCOUNT_API/existed")
    fun checkAccountIsExisted(@Query("username") username: String?): Flowable<Response<JsonObject>>

    /**
     * 发送验证码
     * @Field
     */
    @POST("$ACCOUNT_API/sendvc")
    fun sendCaptchaToTarget(@Query("username") username: String?): Flowable<Response<JsonElement>>

    /**
     * 校验验证码
     * @Field
     */
    @POST("$ACCOUNT_API/checkvc")
    fun checkCaptcha(
        @Query("username") username: String?,
        @Query("vc") vc: String?
    ): Flowable<Response<JsonElement>>

    /**
     * 注册
     * @Field
     */
    @POST("$ACCOUNT_API/regist")
    fun register(
        @Query("username") username: String?,
        @Query("passwd") passwd: String?,
        @Query("vc") vc: String?,
        @Query("from") from: String?,
        @Query("cityCode") cityCode: String?,
        @Query("lng") lng: String?,
        @Query("lat") lat: String?
    ): Flowable<Response<User>>

    /**
     * 密码登录
     * @Field
     */
    @POST("$ACCOUNT_API/login")
    fun passwordLogin(
        @Query("username") username: String?,
        @Query("passwd") passwd: String?
    ): Flowable<Response<User>>


    /**
     * 重设密码
     * @Field
     */
    @POST("$ACCOUNT_API/resetPasswd")
    fun resetPassword(
        @Query("username") username: String?,
        @Query("newPasswd") newPasswd: String?
    ): Flowable<Response<JsonElement>>

    /**
     * 重设密码
     * @Field
     */
    @POST("$ACCOUNT_API/logout")
    fun logout(): Flowable<Response<JsonElement>>


    /**
     * 获取订单列表
     * @param tailId 最后一项item id
     * @param pageSize 请求的分页数量
     *                 当返回的list.size()<pageSize 时，为最后一页
     */
    @GET(ORDER_API)
    fun getOrderList(
        @Query("pageSize") pageSize: Int,
        @Query("tailId") tailId: String? = null
    ): Flowable<Response<JsonArray>>


}