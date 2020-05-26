package com.account.app

import com.blankj.utilcode.util.Utils
import com.account.network.ApiService
import com.account.network.UrlUtil
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.jessyan.retrofiturlmanager.RetrofitUrlManager

/**
 * Created by Wangsw on 2020/5/22 0022 15:52.
 * </br>
 *
 */
class CloudAccountPresenter (val view: CloudAccountView) {

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

}