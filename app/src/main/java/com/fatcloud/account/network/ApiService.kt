package com.fatcloud.account.network

import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.commons.Commons
import com.fatcloud.account.entity.defray.AliPayInfo
import com.fatcloud.account.entity.defray.WechatPayInfo
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.home.HomeMix
import com.fatcloud.account.entity.news.NewDetail
import com.fatcloud.account.entity.news.News
import com.fatcloud.account.entity.news.NewsCategory
import com.fatcloud.account.entity.order.enterprise.BankInfo
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo
import com.fatcloud.account.entity.order.persional.PersonalInfo
import com.fatcloud.account.entity.order.persional.bank.PersonalBankDetail
import com.fatcloud.account.entity.oss.SecurityTokenModel
import com.fatcloud.account.entity.product.ProductDetail
import com.fatcloud.account.entity.upgrade.Upgrade
import com.fatcloud.account.entity.users.User
import com.fatcloud.account.entity.wechat.WechatLogin
import com.fatcloud.account.entity.wechat.WechatUserInfo
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.reactivex.Flowable
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER
import retrofit2.http.*

/**
 * Created by Wangsw on 2020/5/22 0022 15:00.
 * </br>
 * 调用的api接口
 */
interface ApiService {
    companion object {

        /**
         * 微信相关接口
         */
        const val API_WECHAT_OFFICIAL = "wechat"

        private const val USE_CACHED = "cache:60"

        /**
         * 云账户API
         */
        private const val API_ACCOUNT_URI = "api/account"

        private const val API_URI = "api"

        /**
         * <首页>相关接口前缀
         */
        private const val HOME_API_PREFIX = "$API_ACCOUNT_URI/tHome/"

        /**
         * <产品>相关接口前缀
         */
        private const val PRODUCT_API_PREFIX = "$API_ACCOUNT_URI/tProduct/"


        /**
         * 账户相关接口前缀
         */
        private const val T_ACCOUNT_API = "$API_ACCOUNT_URI/tAccount"


        /**
         * 订单相关
         */
        private const val ORDER_API = "$API_ACCOUNT_URI/tOrder"

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
    @GET("$API_ACCOUNT_URI/tNews/mold")
    fun getNewsCategory(): Flowable<Response<JsonArray>>

    /**
     * 资讯页列表
     * @see NewsCategory
     * @see News
     * @see <a href="http://192.168.1.191:8881/api/account/tNews/?pageSize=10">资讯列表</a>
     * @param tailId 最后一项item id
     * @param mold 资讯列别，即：资讯tab列表中的 value 字段
     *
     */
    @GET("$API_ACCOUNT_URI/tNews/")
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
    @GET("$API_ACCOUNT_URI/tNews/detail")
    fun getNewsDetail(
        @Query("newsId") newsId: String?
    ): Flowable<Response<NewDetail>>

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
    @GET("$API_ACCOUNT_URI/tOrderWork/")
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
     * 个人业务详情
     * @param id 订单id
     */
    @GET("$ORDER_API/detail")
    fun getPersonalBankInfo(
        @Query("id") id: String? = null
    ): Flowable<Response<PersonalBankDetail>>




    /**
     * 企业订单详情
     * @param orderWorkId 订单流程id
     */
    @GET("$API_ACCOUNT_URI/tOrderWork/detail")
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
        @Body `in`: JsonObject? = null
    ): Flowable<Response<PreparePay>>


    /**
     * 添加个体户营业执照
     *
     */
    @POST("$ORDER_API/addSelfemployed")
    fun addLicensePersonal(
        @Body `in`: JsonObject? = null
    ): Flowable<Response<PreparePay>>


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
    ): Flowable<Response<PreparePay>>


    /**
     * 添加代理记账
     * @param signImgUrl 签字图片
     * @param businessLicenseImgUrl 营业执照图片
     */
    @POST("$ORDER_API/addBookkeeping")
    @FormUrlEncoded
    fun addAgentBookkeeping(
        @Field("money") money: String?,
        @Field("productId") productId: String?,
        @Field("productPriceId") productPriceId: String?,
        @Field("legalPersonName") legalPersonName: String?,
        @Field("phone") phone: String?,
        @Field("idno") idno: String?,
        @Field("shopName") shopName: String?,
        @Field("businessLicenseImgUrl") businessLicenseImgUrl: String?,
        @Field("signImgUrl") signImgUrl: String?
    ): Flowable<Response<PreparePay>>


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
     * @param orderWorkId               订单流程状态类型
     * @param reconciliatAddr           对账单收货地址详细
     * @param reconciliatArea           对账单收货地址区域
     * @param reconciliatContact        对账联系人
     * @param reconciliatPhone          对账联系人电话
     * @param postcode                  邮编
     * @see Constants.OW1
     * @see Constants.OW2
     * @see Constants.OW3
     * @see Constants.OW4
     */
    @POST("$API_ACCOUNT_URI/tOrderWork/add")
    @FormUrlEncoded
    fun addSpecificProcessContent(
        @Field("orderWorkId") orderWorkId: String?,
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
        @Field("reconciliatAddr") reconciliatAddr: String?,
        @Field("reconciliatArea") reconciliatArea: String?,
        @Field("reconciliatContact") reconciliatContact: String?,
        @Field("reconciliatPhone") reconciliatPhone: String?,
        @Field("postcode") postcode: String?
    ): Flowable<Response<JsonElement>>


    /**
     * 企业订单详情
     * @param orderWorkId 订单流程id
     */
    @GET("$API_ACCOUNT_URI/tOrderWork/detail2")
    fun getEnterpriseOrderDetail2(
        @Query("orderWorkId") orderWorkId: String? = null
    ): Flowable<Response<BankInfo>>


    /**
     * 微信统一下单
     * @param orderId 订单id
     */
    @GET("$API_URI/wxpay/unifiedOrder")
    fun wechatUnifiedOrder(
        @Query("orderId") orderId: String?
    ): Flowable<Response<WechatPayInfo>>

    /**
     * 支付宝统一下单
     * @param orderId 订单id
     */
    @GET("$API_URI/alipay/unifiedOrder")
    fun alipayUnifiedOrder(
        @Query("orderId") orderId: String?
    ): Flowable<Response<AliPayInfo>>


    /**
     * 验证订单是否已支付
     * @param id 订单id
     * @param orderNo 订单号
     */
    @GET("$API_ACCOUNT_URI/tOrder/checkpaied")
    fun checkOrderRealPaymentStatus(
        @Query("id") id: String?,
        @Query("orderNo") orderNo: String?
    ): Flowable<Response<AliPayInfo>>

    /**
     * 验证订单是否已支付
     * @param id 订单id
     * @param orderNo 订单号
     */
    @GET("$API_URI/common/version")
    fun checkAppVersion(@Query("appFlag") appFlag: String? = "android"): Flowable<Response<Upgrade>>


    /**
     * 获取 微信 AccessToken
     * @param code APP获取到的微信授权的code
     */
    @GET("$API_URI/wxpay/getAccessToken")
    fun getWechatAccessToken(
        @Query("code") code: String?
    ): Flowable<Response<JsonObject>>

    /**
     * 获取微信个人信息
     * https://developers.weixin.qq.com/doc/oplatform/Mobile_App/WeChat_Login/Authorized_API_call_UnionID.html
     * https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
     * @param access_token
     * @param openid
     */
    @Headers(DOMAIN_NAME_HEADER.plus(API_WECHAT_OFFICIAL))
    @GET("/sns/userinfo")
    fun getWechatUserInfo(
        @Query("access_token") access_token: String?,
        @Query("openid") openid: String?
    ): Flowable<Response<WechatUserInfo>>


    /**
     * 微信注册或登录
     */
    @POST("$T_ACCOUNT_API/registwx")
    @FormUrlEncoded
    fun doWechatLoginOrRegister(
        @Field("headUrl") headUrl: String?,
        @Field("nickName") nickName: String?,
        @Field("openid") openid: String?,
        @Field("phone") phone: String?,
        @Field("city") city: String?,
        @Field("lat") lat: String?,
        @Field("lng") lng: String?
    ): Flowable<Response<User>>


    /**
     * 获取资讯点赞状态
     */
    @GET("$API_ACCOUNT_URI/tNews/thumbsUpState")
    fun getNewsLikeStatus(
        @Query("newsId") newsId: String?,
        @Query("token") token: String?
    ): Flowable<Response<JsonElement>>


    /**
     * 资讯点赞操作
     */
    @GET("$API_ACCOUNT_URI/tNews/thumbsUpClick")
    fun newsLike(
        @Query("newsId") newsId: String?,
        @Query("token") token: String?
    ): Flowable<Response<JsonElement>>


    /**
     * 添加个体户营业执照变更
     *
     */
    @POST("$ORDER_API/addSelfemployedMotify")
    fun addLicensePersonalChange(
        @Body `in`: JsonObject? = null
    ): Flowable<Response<PreparePay>>

    /**
     * 添加个体户营业执照注销
     *
     */
    @POST("$ORDER_API/addSelfemployedCancel")
    fun addLicensePersonalLogout(
        @Body `in`: JsonObject? = null
    ): Flowable<Response<PreparePay>>

    /**
     * P7添加大师起名
     *
     */
    @POST("$ORDER_API/addMasterNamed")
    fun addMasterNamed(
        @Body `in`: JsonObject? = null
    ): Flowable<Response<PreparePay>>

    /**
     * P8添加个体户银行对公账户
     *
     */
    @POST("$ORDER_API/addSelfemployedBank")
    fun addSelfemployedBank(
        @Body `in`: JsonObject? = null
    ): Flowable<Response<PreparePay>>


}