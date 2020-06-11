package com.fatcloud.account.app

import com.blankj.utilcode.util.Utils
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.commons.Commons
import com.fatcloud.account.network.ApiService
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Wangsw on 2020/5/22 0022 15:52.
 * </br>
 *
 */
class CloudAccountPresenter(val view: CloudAccountView) {

    private val apiService: ApiService = (Utils.getApp() as CloudAccountApplication).apiService


    private var compositeDisposable: CompositeDisposable? = null

    fun addSubscribe(subscription: Disposable) {
        if (compositeDisposable == null) {
//            RetrofitUrlManager.getInstance().putDomain(ApiService.NEW_SERVICE, UrlUtil.SERVER_HOST_V3)
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(subscription)
    }

    private fun <T> flowableUICompose(): FlowableTransformer<T, T> {
        return FlowableTransformer {
            it.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun <T> flowableCompose(): FlowableTransformer<T, T> {
        return FlowableTransformer {
            it.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
        }
    }


    fun getCommonList() {
        addSubscribe(
            apiService.getCommonList()
                .compose(flowableUICompose())
                .subscribeWith(object : BaseHttpSubscriber<Commons>(view) {
                    override fun onSuccess(data: Commons?) {
                        data?.let {

                            data.businessScope.apply {


                                forEachIndexed { index, first ->
                                    if (index == 0) {
                                        first.nativeIsSelect = true
                                    }


                                    first.childs.forEach { second ->
                                        second.nativeIsSelect = true
                                    }

                                }


                            }

                            view.receiveCommonData(data)

                        }
                    }

                })
        )
    }

}