package com.account.network

/**
 * Created by Wangsw on 2020/5/22 0022 15:00.
 * </br>
 * 调用的api接口
 */
interface ApiService {
    companion object {

        /**
         * v3 接口地址
         */
        const val NEW_SERVICE = "v3"

        /**
         * v2  接口地址
         */
        private const val API_URI = "cloud-api"

        private const val USE_CACHED = "cache:60"

    }



   /* @Headers(RetrofitUrlManager.DOMAIN_NAME_HEADER.plus(NEW_SERVICE))
    @GET("$USER_API_PREFIX/v1/area/conditionList")
    fun getData(@Query("exVersion") exVer: Int,
                @Query("yihuaId") yihuaId: Long? = User.get().id): Flowable<Response<JsonElement>>
*/
}