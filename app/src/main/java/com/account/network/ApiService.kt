package com.account.network

import com.google.gson.JsonElement
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
         * 首页相关接口前缀
         */
        private const val HOME_API = "$API_URI/tHome"

        /**
         * 资讯相关接口前缀
         */
        private const val NEWS_API = "$API_URI/tNews"

    }

    /**
     * 获取首页混合列表信息
     */
    @GET("$HOME_API/v1/area/conditionList")
    fun getHomeList(): Flowable<Response<JsonObject>>


}