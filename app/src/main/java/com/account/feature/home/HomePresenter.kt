package com.account.feature.home

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.account.base.common.BasePresenter
import com.account.base.net.BaseHttpSubscriber
import com.account.entity.home.HomeMix
import javax.inject.Inject

class HomePresenter @Inject constructor(private var homeView: HomeView) : BasePresenter(homeView) {


    fun loadHomeList(lifecycle: LifecycleOwner, page: Int) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getHomeList(),
            object : BaseHttpSubscriber<HomeMix>(homeView) {
                override fun onSuccess(data: HomeMix?) {
                    data?.let {
                        homeView.setHeaderDada(it.banners, it.products)
                        homeView.bindNewsList(data.news, page == 0, true)

                    }

                }
            })
    }
}
