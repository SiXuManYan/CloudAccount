package com.account.app

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.account.common.CommonUtils
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.*
import kotlin.collections.HashMap

@GlideModule(glideName = "Glide")
class GlideModule : AppGlideModule() {

    val YH_PIC_GLIDE_CACHE = "/Cloud/PicCache/"

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val diskCacheSizeBytes = 1024 * 1024 * 500L // 500 MB
        builder.setDiskCache(DiskLruCacheFactory(CommonUtils.getOwnCacheDirectory(context, YH_PIC_GLIDE_CACHE), diskCacheSizeBytes))
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(ProgressManager.getOkHttpClient()))
    }
}

object ProgressManager {

    private val listenersMap = Collections.synchronizedMap(HashMap<String, OnProgressListener>())
    private var okHttpClient: OkHttpClient? = null

    private val LISTENER = ProgressResponseBody.InternalProgressListener { url, bytesRead, totalBytes ->
        val onProgressListener = getProgressListener(url)
        if (onProgressListener != null) {
            val percentage = (bytesRead * 1f / totalBytes * 100f).toInt()
            val isComplete = percentage >= 100
            onProgressListener.onProgress(isComplete, percentage, bytesRead, totalBytes)
            if (isComplete) {
                removeListener(url)
            }
        }
    }

    fun getOkHttpClient(): OkHttpClient {
        if (okHttpClient == null) {
            okHttpClient = OkHttpClient.Builder()
                    .addNetworkInterceptor { chain ->
                        val request = chain.request()
                        val response = chain.proceed(request)
                        response.newBuilder()
                                .body(ProgressResponseBody(request.url().toString(), LISTENER, response.body()))
                                .build()
                    }
                    .build()
        }
        return okHttpClient!!
    }

    fun addListener(url: String, listener: OnProgressListener?) {
        if (!TextUtils.isEmpty(url) && listener != null) {
            listenersMap[url] = listener
            listener.onProgress(false, 1, 0, 0)
        }
    }

    fun removeListener(url: String) {
        if (!TextUtils.isEmpty(url)) {
            listenersMap.remove(url)
        }
    }

    fun getProgressListener(url: String): OnProgressListener? {
        if (TextUtils.isEmpty(url) || listenersMap == null || listenersMap.isEmpty()) {
            return null
        }

        return listenersMap[url]
    }
}

interface OnProgressListener {
    fun onProgress(isComplete: Boolean, percentage: Int, bytesRead: Long, totalBytes: Long)
}

class ProgressImageViewTarget(imageView: ImageView, private val url: String, callback: OnProgressListener) : DrawableImageViewTarget(imageView) {

    init {
        ProgressManager.addListener(url, callback)
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        val onProgressListener = ProgressManager.getProgressListener(url)
        if (onProgressListener != null) {
            onProgressListener.onProgress(true, 100, 0, 0)
            ProgressManager.removeListener(url)
        }
        super.onLoadFailed(errorDrawable)
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        val onProgressListener = ProgressManager.getProgressListener(url)
        if (onProgressListener != null) {
            onProgressListener.onProgress(true, 100, 0, 0)
            ProgressManager.removeListener(url)
        }
        super.onResourceReady(resource, transition)
    }

}