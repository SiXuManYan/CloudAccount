package com.account.backstage

import com.account.app.CloudAccountApplication
import com.account.base.common.BaseNoDaggerPresenter
import com.account.base.net.BaseJsonArrayHttpSubscriber
import com.account.data.CloudDataBase
import com.account.entity.news.NewsCategory
import com.blankj.utilcode.util.Utils
import com.google.gson.JsonArray
import java.util.*

class DataWorkPresenter constructor(private var serviceView: ServiceView) : BaseNoDaggerPresenter(serviceView) {


    val database: CloudDataBase by lazy { (Utils.getApp() as CloudAccountApplication).database }


    /**
     * 获取资讯分类并存储db
     */
    fun getNewsCategoryToDataBase() {
        addSubscribe(
            apiService.getNewsCategory()
                .compose(flowableUICompose())
                .subscribeWith(object : BaseJsonArrayHttpSubscriber<NewsCategory>(serviceView) {
                    override fun onSuccess(jsonArray: JsonArray?, list: ArrayList<NewsCategory>, lastItemId: String?) {
                        if (list.isNotEmpty()) database.newsCategoryDao().addAllServiceSubTypes(list)
                    }
                })
        )
    }


}