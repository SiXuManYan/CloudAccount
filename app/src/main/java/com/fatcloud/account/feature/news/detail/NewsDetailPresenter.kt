package com.fatcloud.account.feature.news.detail

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.news.NewDetail
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/5/30 0030 16:33.
 * </br>
 *
 */
class NewsDetailPresenter  @Inject constructor(private var newsDetailView: NewsDetailView) : BasePresenter(newsDetailView){




    fun getNewsDetail(lifecycle: LifecycleOwner, newsId: String?) {

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getNewsDetail(newsId),
            object : BaseHttpSubscriber<NewDetail>(newsDetailView, false) {
                override fun onSuccess(data: NewDetail?) {

                    data?.let {
                        newsDetailView.bindDetailData(it)

                    }
                }

            })

    }

}