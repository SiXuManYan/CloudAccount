package com.fatcloud.account.base.common

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.LogUtils
import com.fatcloud.account.BuildConfig
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.RxBus
import com.fatcloud.account.network.ApiService
import com.fatcloud.account.network.Response
import com.google.gson.JsonObject
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
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

    protected fun <T> requestWechatApi(
        lifecycle: LifecycleOwner,
        event: Lifecycle.Event,
        flowable: Flowable<Response<T>>,
        subscriber: BaseHttpSubscriber<T>
    ) {
        RetrofitUrlManager.getInstance().putDomain(ApiService.API_WECHAT_OFFICIAL, BuildConfig.WECHAT_OFFICIAL_API)
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


}