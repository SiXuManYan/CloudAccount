package com.account.feature.news

import com.account.base.common.BasePresenter
import com.account.data.CloudDataBase
import com.account.entity.news.NewsCategory
import java.util.ArrayList
import javax.inject.Inject

class NewsPresenter @Inject constructor(private val view: NewsView):BasePresenter(view){

    lateinit var database: CloudDataBase @Inject set


    fun getNewCategoryList(): List<NewsCategory> {
        return database.newsCategoryDao().findTopServiceSubTypes()
    }


}
