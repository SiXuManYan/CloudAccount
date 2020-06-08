package com.fatcloud.account.feature.home

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.home.HomeMix
import com.google.gson.JsonObject
import javax.inject.Inject

class HomePresenter @Inject constructor(private var homeView: HomeView) : BasePresenter(homeView) {


    fun loadHomeList(lifecycle: LifecycleOwner, pageSize: Int, lastItemId: String?) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getHomeList(pageSize, lastItemId),
            object : BaseHttpSubscriber<HomeMix>(homeView) {
                override fun onSuccess(data: HomeMix?) {


                    data?.let {

                        // 第一页
                        if (lastItemId.isNullOrBlank()) {
                            homeView.setHeaderDada(it.banners, it.products)
                        }

                        var lastId: String? = ""

                        val list = data.news
                        list.forEachIndexed { index, news ->

                            // 获取最后一项item id ,用于请求下一页
                            if (index == list.size - 1) {
                                lastId =  news.id
                            }
                        }
                        homeView.bindList(list, lastId)
                    }

                }
            })
    }


}
