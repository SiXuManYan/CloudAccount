package com.fatcloud.account.feature.news.detail

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.news.NewDetail
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/5/30 0030 16:33.
 * </br>
 *
 */
class NewsDetailPresenter @Inject constructor(private var newsDetailView: NewsDetailView) : BasePresenter(newsDetailView) {


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


    /**
     * 获取点赞状态
     */
    fun getNewsLikeStatus(lifecycle: LifecycleOwner, newsId: String?) {

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getNewsLikeStatus(newsId),
            object : BaseHttpSubscriber<JsonElement>(newsDetailView, false) {
                override fun onSuccess(data: JsonElement?) {

                    /*
                    {
                        "code": "200",
                        "msg": "成功",
                        "data": 1
                    }
                    */

                    if (data == null) {
                        return
                    }

                    if (data.isJsonPrimitive) {

                        /**
                         * 1 已经点赞
                         * 0 没点赞
                         */
                        val status = data.asInt
                        newsDetailView.newsLikeStatus(status)
                    }

                }


            })
    }


    /**
     * 进行点赞和取消赞
     */
    fun doLikeAction(lifecycle: LifecycleOwner, newsId: String?) {

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.newsLike(newsId),
            object : BaseHttpSubscriber<JsonElement>(newsDetailView, false) {
                override fun onSuccess(data: JsonElement?) {
                    newsDetailView.handleLikeSuccess()
                }
            })
    }


}