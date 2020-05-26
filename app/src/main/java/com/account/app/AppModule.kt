package com.account.app

import android.content.Context
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.account.BuildConfig
import com.account.common.CommonUtils
import com.account.common.Constants
import com.account.common.Constants.DEVICE_ANDROID
import com.account.data.CloudDataBase
import com.account.network.ApiService
import com.account.network.UrlUtil
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.jz.yihua.activity.network.GsonConvertFactory

import dagger.Module
import dagger.Provides
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * 应用数据模块
 */
@Module
class AppModule {

    /** 超时时间 */
    private val defaultTimeout: Long = 60
    private val gson = Gson()
    @Provides
    internal fun contextProvider(application: CloudAccountApplication) = application.applicationContext

    @Provides
    @Singleton
    internal fun okHttpClientProvider(context: Context): OkHttpClient {
        val builder = RetrofitUrlManager.getInstance().with(OkHttpClient.Builder())
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
            }

            @Throws(java.security.cert.CertificateException::class)
            override fun checkClientTrusted(
                    x509Certificates: Array<java.security.cert.X509Certificate>,
                    s: String) {
            }

            @Throws(java.security.cert.CertificateException::class)
            override fun checkServerTrusted(
                    x509Certificates: Array<java.security.cert.X509Certificate>,
                    s: String) {
            }
        })

        try {
            //构造自己的SSLContext
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, java.security.SecureRandom())
            builder.sslSocketFactory(sc.socketFactory, trustAllCerts[0] as X509TrustManager)
                    .hostnameVerifier { _, _ -> true }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (BuildConfig.DEBUG) {
            //日志拦截器
            val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                LogUtils.v(it)
            }).apply { this.level = HttpLoggingInterceptor.Level.BASIC }
            val bodyInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Log.i("bodyInterceptor", it)
            }).apply { this.level = HttpLoggingInterceptor.Level.BODY }
            builder.addInterceptor(loggingInterceptor)
            builder.addInterceptor(bodyInterceptor)
        }
        builder.protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .addInterceptor(cacheInterceptor)
                .retryOnConnectionFailure(true)
                .cache(Cache(File(context.cacheDir, "http_response"), 10485760L))//10M
                .connectTimeout(defaultTimeout, TimeUnit.SECONDS)
                .readTimeout(defaultTimeout, TimeUnit.SECONDS)

        return builder.build()
    }

    @Provides
    @Singleton
    internal fun apiServiceProvider(okHttpClient: OkHttpClient): ApiService {
        RetrofitUrlManager.getInstance().putDomain(ApiService.NEW_SERVICE, UrlUtil.SERVER_HOST_V3)
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(UrlUtil.SERVER_HOST_V2)
                .addConverterFactory(GsonConvertFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    internal fun databaseProvider(context: Context) = CloudDataBase.get(context)

    /** 缓存拦截器 */
    private val cacheInterceptor: Interceptor
        get() = Interceptor { chain ->
            val url = chain.request().url().newBuilder().addQueryParameter(FROM, DEVICE_ANDROID).addQueryParameter(VERSION, BuildConfig.VERSION_CODE.toString()).build()
            //加入通用的参数
            val request = chain.request()
                    .newBuilder()
                    .apply {
                        // 判断用户是否登录
//                        if (User.isLogon()) {
//                            addHeader("yihuaId", User.get().id?.toString() ?: "")
//                            addHeader("phone", User.get().phone ?: "")
//                        }
                    }
                    .url(url.run {
                        if (chain.request().url().toString().contains("/cloud-api/")) {
                            this
                        } else {
                            var sign: String? = ""
                            when {
                                chain.request().method().toUpperCase() == "GET" -> {

                                }
                                chain.request().body() is FormBody && (chain.request().body() as FormBody).size() > 0 -> {
                                    sign = (chain.request().body() as FormBody).let {
                                        val requestParameter = StringBuilder()
                                        for (i in 0 until it.size()) {
                                            requestParameter.append(it.name(i)).append("=").append(it.value(i)).append("&")
                                        }
                                        requestParameter.toString()
                                    }
                                }
                                else -> {
                                    chain.request().body()?.let {
                                        if (it.contentLength() > 0) {
                                            val buffer = Buffer()
                                            it.writeTo(buffer)
                                            sign = buffer.readUtf8().plus("&")
                                            LogUtils.d(sign)
                                        }
                                    }

                                }
                            }
                            newBuilder().addQueryParameter("sign", RsaUtils.signMd5WithRsa(sign.plus(url.query()))).build()
                        }
                    })
                    .build()

            val response = chain.proceed(request)

            //  修改拦截器 当返回 code为9时 修改key信息重新请求 可以在 CommonUtils 下增加 key map 获取key的原始字符串 修改key后加密重新请求
            request.url().queryParameter("key")?.let { key ->
                isCode9(response.body(), request, chain, key)?.let {
                    return@Interceptor it
                }
            }

            return@Interceptor response
        }

    private fun isCode9(body: ResponseBody?, request: Request, chain: Interceptor.Chain, key: String): Response? {
        val source = body?.source()
        if (source != null) {
            source.request(Long.MAX_VALUE)
            val buffer = source.buffer().clone()
            gson.fromJson(buffer.readUtf8(), com.account.network.Response::class.java)?.apply {
                if (resultCode == "9" || code == 9) {
                    data?.let {
                        if (it is LinkedTreeMap<*, *>) {
                            if (it.contains("currMillSecs")) {
                                val shareDefault = CommonUtils.getShareDefault()
                                val serverTime = it["currMillSecs"] as String
                                shareDefault.put(Constants.SP_AES_LOGIN_SERVICE_TIME, serverTime.toLong())
                                shareDefault.put(Constants.SP_AES_LOGIN_TIME, System.currentTimeMillis())
                                CommonUtils.removeKeyParameter(key)?.let { pair ->
                                    return chain.proceed(request.newBuilder().url(
                                            request.url().newBuilder().removeAllQueryParameters("key")
                                                    .addQueryParameter("key", CommonUtils.encrypt("", pair.first, pair.second).apply {
                                                        CommonUtils.removeKeyParameter(this)
                                                    })
                                                    .build()
                                    ).build())
                                }
                            }
                        }
                    }

                }
            }
        }
        CommonUtils.removeKeyParameter(key)
        return null
    }

    companion object {
        internal const val FROM = "from"
        internal const val VERSION = "versionCode"
    }
}