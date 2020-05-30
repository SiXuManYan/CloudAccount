package com.account.feature.news.child

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.account.base.common.BasePresenter
import com.account.base.net.BaseJsonArrayHttpSubscriber
import com.account.entity.news.News
import com.google.gson.JsonArray
import java.util.*
import javax.inject.Inject


class NewsChildPresenter @Inject constructor(private var newsChildView: NewsChildView) : BasePresenter(newsChildView) {




    override fun loadList(lifecycle: LifecycleOwner, page: Int, pageSize: Int, lastItemId: String?) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getNewsList(pageSize,lastItemId),

            object : BaseJsonArrayHttpSubscriber<News>(newsChildView, false) {

                override fun onSuccess(jsonArray: JsonArray?, list: ArrayList<News>, lastItemId: String?) {
                    newsChildView.bindList(list, lastItemId)
                }
            }

        )
    }


}
