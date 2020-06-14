package com.fatcloud.account.base.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.RxBus
import com.fatcloud.account.network.ApiService
import com.fatcloud.account.network.Response
import com.blankj.utilcode.util.LogUtils
import com.fatcloud.account.R
import com.fatcloud.account.common.Constants
import com.fatcloud.account.feature.matisse.Glide4Engine
import com.fatcloud.account.feature.matisse.Matisse
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import com.zhihu.matisse.MimeType
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber
import javax.inject.Inject

/**
 * 数据提供器基类
 */
open class BasePresenter constructor(private var view: BaseView?) {

    //Reactive收集
    private var compositeDisposable: CompositeDisposable? = null

    protected lateinit var apiService: ApiService @Inject set

    protected lateinit var appContext: Context @Inject set

    /**
     * 添加Rxbus的订阅
     * @param eventType 传递类型
     * @param consumer  消费
     */
    fun <T> addRxBusSubscribe(eventType: Class<T>, consumer: Consumer<T>) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(
            RxBus.toFlowable(eventType).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer, Consumer {
                LogUtils.d(it)
            })
        )
    }

    /**
     * 转换为线程处理
     */
    protected fun <T> flowableCompose(): FlowableTransformer<T, T> {
        return FlowableTransformer {
            it.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
        }
    }

    /**
     * 转换为线程处理后，最后回到UI线程中
     */
    protected fun <T> flowableUICompose(): FlowableTransformer<T, T> {
        return FlowableTransformer {
            it.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * 网络请求及处理
     * @param lifecycle 绑定对象
     * @param event 取消周期事件
     * @param flowable 请求Service
     * @param subscriber 订阅处理
     */
    protected fun <T> requestApi(
        lifecycle: LifecycleOwner,
        event: Lifecycle.Event,
        flowable: Flowable<Response<T>>,
        subscriber: BaseHttpSubscriber<T>
    ) {
        addSubscribe(
            flowable.bindUntilEvent(lifecycle, event)
                .compose(flowableUICompose())
                .subscribeWith(subscriber)
        )
    }

    /**
     * 网络请求及处理
     * @param lifecycle 绑定对象
     * @param event 取消周期事件
     * @param flowable 请求Service
     * @param subscriber 订阅处理
     */
    protected fun <T> requestApi(
        lifecycle: LifecycleOwner,
        event: Lifecycle.Event,
        flowable: Flowable<Response<JsonObject>>,
        mapper: Function<Response<JsonObject>, T>,
        subscriber: ResourceSubscriber<T>
    ) {
        addSubscribe(
            flowable.bindUntilEvent(lifecycle, event)
                .compose(flowableCompose())
                .map(mapper)
                .compose(flowableUICompose())
                .subscribeWith(subscriber)
        )
    }


    /**
     * 订阅事件
     * @param consumer 处理
     */
    fun subsribeEvent(consumer: Consumer<Event>) {
        addRxBusSubscribe(Event::class.java, consumer)
    }

    /**
     * 订阅事件
     * @param consumer 处理
     */
    inline fun <reified T> subsribeEventEntity(consumer: Consumer<T>) {
        addRxBusSubscribe(T::class.java, consumer)
    }

    /**
     * 获取列表数据
     * @param lifecycle 绑定对象
     * @param page 页码
     */
    open fun loadList(lifecycle: LifecycleOwner, page: Int) {}

    /**
     * 使用 last Item id 的方式获取列表数据
     * 注：当返回数据size < 分页数量时pageSize时 ，为最后一页
     * @param lifecycle 绑定对象
     * @param page 页码
     * @param pageSize 请求的分页数量
     * @param lastItemId 最后一项的item 的id
     *
     */
    open fun loadList(lifecycle: LifecycleOwner, page: Int, pageSize: Int, lastItemId: String?) {}

    /**
     * 添加订阅
     * @param subscription 订阅
     */
    protected fun addSubscribe(subscription: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(subscription)
    }

    /**
     * 取消订阅
     * @param subscription 订阅
     */
    protected fun removeSubscribe(subscription: Disposable) {
        compositeDisposable?.remove(subscription)
    }

    /**
     * 取消所有的订阅
     */
    fun unSubscribe() {
        if (compositeDisposable != null) {
            compositeDisposable?.clear()
        }
    }

    /**
     * 释放绑定的view
     */
    open fun detachView() {
        view = null
        unSubscribe()
    }


    lateinit var dialog: BottomSheetDialog


    /**
     * @param mediaType
     * @see Matisse.IMG
     * @see Matisse.GIF
     * @see Matisse.VIDEO
     */
    fun selectImage(activity: Activity, mediaType: Int) {
        if (!this::dialog.isInitialized) {
            dialog = BottomSheetDialog(activity)
            val view = LayoutInflater.from(activity).inflate(R.layout.post_feed_bottom_sheet, null)
            dialog.setContentView(view)
            try {
                // hack bg color of the BottomSheetDialog
                val parent = view.parent as ViewGroup
                parent.setBackgroundResource(android.R.color.transparent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            view.findViewById<TextView>(R.id.shooting).setOnClickListener {
                // 拍摄
//                presenter.requestShootingPermissions(this@PostFeedActivity, filePath, 0)
                dialog.dismiss()
            }


            view.findViewById<TextView>(R.id.select_album).setOnClickListener {
                // 相册选择
                if (requestAlbumPermissions(activity)) {
                    Matisse.from(activity)
                        .choose(
                            if (mediaType == 0)
                                MimeType.ofAll()
                            else
                                MimeType.ofImage().apply {
                                    remove(MimeType.GIF)
                                }, true
                        )
                        .countable(true)
                        .originalEnable(false)
                        .maxSelectable(1)
                        .theme(R.style.Matisse_Dracula)
                        .thumbnailScale(0.87f)
                        .imageEngine(Glide4Engine())
                        .forResult(Constants.REQUEST_MEDIA, mediaType)
                } else {

                }
                dialog.dismiss()
            }
            view.findViewById<TextView>(R.id.cancel_tv).setOnClickListener {
                dialog.dismiss()
            }


        }
        dialog.show()

    }


    /**
     * 相册权限申请
     */
    fun requestAlbumPermissions(activity: Activity): Boolean {
        var isGranted = false
        addSubscribe(RxPermissions(activity)
            .request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .subscribe {
                isGranted = true
            })
        return isGranted
    }


}