package com.fatcloud.account.network

import com.fatcloud.account.entity.commons.Commons
import com.fatcloud.account.entity.home.HomeMix
import com.fatcloud.account.entity.news.NewDetail
import com.fatcloud.account.entity.news.NewsCategory
import com.fatcloud.account.entity.news.News
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo
import com.fatcloud.account.entity.order.persional.PersonalInfo
import com.fatcloud.account.entity.oss.SecurityTokenModel
import com.fatcloud.account.entity.product.ProductDetail
import com.fatcloud.account.entity.users.User
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.reactivex.Flowable
import retrofit2.http.*
import java.math.BigDecimal

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
        private const val T_ACCOUNT_API = "$API_URI/tAccount"


        /**
         * 订单相关
         */
        private const val ORDER_API = "$API_URI/tOrder"

    }

    /**
     * 获取首页列表信息
     * @see HomeMix
     */
    @GET(HOME_API_PREFIX)
    fun getHomeList(
        @Query("pageSize") pageSize: Int,
        @Query("tailId") tailId: String? = null
    ): Flowable<Response<HomeMix>>

    /**
     * 获取产品信息
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
    @POST("$T_ACCOUNT_API/existed")
    fun checkAccountIsExisted(@Query("username") username: String?): Flowable<Response<JsonObject>>

    /**
     * 发送验证码
     * @Field
     */
    @POST("$T_ACCOUNT_API/sendvc")
    fun sendCaptchaToTarget(@Query("username") username: String?): Flowable<Response<JsonElement>>

    /**
     * 校验验证码
     * @Field
     */
    @POST("$T_ACCOUNT_API/checkvc")
    fun checkCaptcha(
        @Query("username") username: String?,
        @Query("vc") vc: String?
    ): Flowable<Response<JsonElement>>

    /**
     * 注册
     * @Field
     */
    @POST("$T_ACCOUNT_API/regist")
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
    @POST("$T_ACCOUNT_API/login")
    fun passwordLogin(
        @Query("username") username: String?,
        @Query("passwd") passwd: String?
    ): Flowable<Response<User>>


    /**
     * 重设密码
     * @Field
     */
    @POST("$T_ACCOUNT_API/resetPasswd")
    fun resetPassword(
        @Query("username") username: String?,
        @Query("newPasswd") newPasswd: String?
    ): Flowable<Response<JsonElement>>

    /**
     * 重设密码
     * @Field
     */
    @POST("$T_ACCOUNT_API/logout")
    fun logout(): Flowable<Response<JsonElement>>


    /**
     * 获取订单列表
     * @param tailId 最后一项item id
     * @param pageSize 请求的分页数量
     *                 当返回的list.size()<pageSize 时，为最后一页
     */
    @GET("$ORDER_API/")
    fun getOrderList(
        @Query("pageSize") pageSize: Int,
        @Query("tailId") tailId: String? = null
    ): Flowable<Response<JsonArray>>


    /**
     * 获取订单办理流程
     * http://192.168.1.191:8881/api/account/tOrderWork/?orderId=1268456920973312000
     *
     */
    @GET("$API_URI/tOrderWork/")
    fun getBusinessProgress(
        @Query("orderId") orderId: String? = null
    ): Flowable<Response<JsonArray>>

    /**
     * 个人业务详情
     * @param id 订单id
     */
    @GET("$ORDER_API/detail")
    fun getPersonalOrderDetail(
        @Query("id") id: String? = null
    ): Flowable<Response<PersonalInfo>>

    /**
     * 企业订单详情
     * @param orderWorkId 订单流程id
     */
    @GET("$API_URI/tOrderWork/detail")
    fun getEnterpriseOrderDetail(
        @Query("orderWorkId") orderWorkId: String? = null
    ): Flowable<Response<EnterpriseInfo>>


    @GET("api/common/list")
    fun getCommonList(): Flowable<Response<Commons>>


    /**
     * 添加企业套餐
     */
    @POST("$ORDER_API/addEnterprise")
    fun addEnterprise(
        @Body `in`: String? = null
    ): Flowable<Response<JsonObject>>


    /**
     * 添加个体户营业执照
     *
     */
    @POST("$ORDER_API/addSelfemployed")
    fun addLicensePersonal(
        @Body `in`: String? = null
    ): Flowable<Response<JsonObject>>


    /**
     * 添加税务登记
     */
    @POST("$ORDER_API/addTaxGrade")
    @FormUrlEncoded
    fun addTaxRegistration(
        @Field("money") money: String?,
        @Field("productId") productId: String?,
        @Field("productPriceId") productPriceId: String?,
        @Field("taxpayerNo") taxpayerNo: String?,
        @Field("legalPersonName") legalPersonName: String?,
        @Field("idno") idno: String?,
        @Field("bankNo") bankNo: String?,
        @Field("phoneOfBank") phoneOfBank: String?,
        @Field("businessLicenseImgUrl") businessLicenseImgUrl: String?,
        @Field("addr") addr: String?, // 税务登记+刻章时传递
        @Field("area") area: String?// 税务登记+刻章时传递
    ): Flowable<Response<JsonObject>>


    /**
     * 添加代理记账
     * @param signImgUrl 签字图片
     * @param businessLicenseImgUrl 营业执照图片
     */
    @POST("$ORDER_API/addBookkeeping")
    @FormUrlEncoded
    fun addAgentBookkeeping(
        @Field("money") money: String?,//
        @Field("productId") productId: String?,//
        @Field("productPriceId") productPriceId: String?,//
        @Field("legalPersonName") legalPersonName: String?,//
        @Field("phone") phone: String?,//
        @Field("idno") idno: String?,//
        @Field("shopName") shopName: String?,//
        @Field("businessLicenseImgUrl") businessLicenseImgUrl: String?,
        @Field("signImgUrl") signImgUrl: String?//
    ): Flowable<Response<JsonObject>>


    /**
     * 获取资源上传 oss token
     */
    @GET("alioss/sts/token")
    fun getOssSecurityToken(): Flowable<Response<SecurityTokenModel>>

    /**
     * 重视头像和昵称
     */
    @POST("$T_ACCOUNT_API/resetInfo")
    @FormUrlEncoded
    fun updateAvatarAndNickname(
        @Field("headUrl") headUrl: String?,
        @Field("nickName") nickName: String?
    ): Flowable<Response<JsonElement>>


    /**
     *
     * 添加具体流程的内容(如，开立银行对公账户)
     * @param businessLicenseImgUrl     公司营业执照
     * @param capital                   企业注册资金
     * @param electronicSealImgUrl      电子公章
     * @param enterpriseAddr            公司地址
     * @param enterpriseMold            企业性质
     * @param enterpriseName            公司名
     * @param financeIdno               财务负责人身份证号
     * @param financeIdnoImgUrlA        财务负责人身份证正面
     * @param financeIdnoImgUrlB        财务负责人身份证背面不能为空
     * @param financeName               财务负责人姓名
     * @param financePhone              财务负责人电话
     * @param financeShares             财务负责人股份比例
     * @param legalPersonWarrantImgUrl  法人签字授权书
     * @param orderWorkId
     * @param reconciliatAddr           对账单收货地址详细
     * @param reconciliatArea           对账单收货地址区域
     * @param reconciliatContact        对账联系人
     * @param reconciliatPhone          对账联系人电话
     */
    @POST("$API_URI/tOrderWork/add")
    @FormUrlEncoded
    fun updateAvatarAndNiSckname(
        @Field("businessLicenseImgUrl") businessLicenseImgUrl: String?,
        @Field("capital") capital: String?,
        @Field("electronicSealImgUrl") electronicSealImgUrl: String?,
        @Field("enterpriseAddr") enterpriseAddr: String?,
        @Field("enterpriseMold") enterpriseMold: String?,
        @Field("enterpriseName") enterpriseName: String?,
        @Field("financeIdno") financeIdno: String?,
        @Field("financeIdnoImgUrlA") financeIdnoImgUrlA: String?,
        @Field("financeIdnoImgUrlB") financeIdnoImgUrlB: String?,
        @Field("financeName") financeName: String?,
        @Field("financePhone") financePhone: String?,
        @Field("financeShares") financeShares: String?,
        @Field("legalPersonWarrantImgUrl") legalPersonWarrantImgUrl: String?,
        @Field("orderWorkId") orderWorkId: String?,
        @Field("reconciliatAddr") reconciliatAddr: String?,
        @Field("reconciliatContact") reconciliatContact: String?,
        @Field("reconciliatPhone") reconciliatPhone: String?
    ): Flowable<Response<JsonElement>>


}