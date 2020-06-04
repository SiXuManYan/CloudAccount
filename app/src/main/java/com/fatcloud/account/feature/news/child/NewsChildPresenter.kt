package com.fatcloud.account.feature.news.child

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseJsonArrayHttpSubscriber
import com.fatcloud.account.entity.news.News
import com.google.gson.JsonArray
import java.util.*
import javax.inject.Inject


class NewsChildPresenter @Inject constructor(private var newsChildView: NewsChildView) : BasePresenter(newsChildView) {


    fun loadNewsList(lifecycle: LifecycleOwner, pageSize: Int, lastItemId: String?, categoryValue: String?) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getNewsList(pageSize, lastItemId, categoryValue),

            object : BaseJsonArrayHttpSubscriber<News>(newsChildView, false) {

                override fun onSuccess(jsonArray: JsonArray?, list: ArrayList<News>, lastItemId: String?) {
                    newsChildView.bindList(list, lastItemId)
                }
            }

        )
    }


}
