package com.fatcloud.account.app

import android.content.Context
import android.util.Log
import com.fatcloud.account.BuildConfig
import com.fatcloud.account.R2.id.original
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.Constants.DEVICE_ANDROID
import com.fatcloud.account.data.CloudDataBase
import com.fatcloud.account.network.ApiService
import com.fatcloud.account.network.GsonConvertFactory
import com.fatcloud.account.network.UrlUtil
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
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
    internal fun contextProvider(application: CloudAccountApplication) =
        application.applicationContext

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
                s: String
            ) {
            }

            @Throws(java.security.cert.CertificateException::class)
            override fun checkServerTrusted(
                x509Certificates: Array<java.security.cert.X509Certificate>,
                s: String
            ) {
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

//        if (BuildConfig.DEBUG) {

            // 日志拦截器
            val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                LogUtils.v(it)
            }).apply {
                this.level = HttpLoggingInterceptor.Level.BASIC
            }

            val bodyInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Log.i("bodyInterceptor", it)
            }).apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

            builder.addInterceptor(loggingInterceptor)
            builder.addInterceptor(bodyInterceptor)
//        }



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
//        RetrofitUrlManager.getInstance().putDomain(ApiService.API_WECHAT_OFFICIAL, BuildConfig.WECHAT_OFFICIAL_API)
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(UrlUtil.SERVER_HOST)
            .addConverterFactory(GsonConvertFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    internal fun databaseProvider(context: Context) = CloudDataBase.get(context)

    /**
     * 拦截器
     */
    private val cacheInterceptor: Interceptor
        get() = Interceptor { chain ->


            val request = chain.request()

            // url上增加通用参数
            val url = request.url()
                .newBuilder()
//                .addQueryParameter(FROM, DEVICE_ANDROID)
//                .addQueryParameter(VERSION, BuildConfig.VERSION_CODE.toString())
                .build()

            val requestBuilder = request.newBuilder().url(url)

            // 增加  header token
            val shareDefault = CommonUtils.getShareDefault()
            val isLogin = shareDefault.getBoolean(Constants.SP_LOGIN)
            val token = shareDefault.getString(Constants.SP_TOKEN, "")

            if (isLogin && token.isNotEmpty()) {
                requestBuilder.addHeader("token", token)
            }
            return@Interceptor chain.proceed(requestBuilder.build())
        }


    companion object {
        internal const val FROM = "from"
        internal const val VERSION = "versionCode"
    }
}