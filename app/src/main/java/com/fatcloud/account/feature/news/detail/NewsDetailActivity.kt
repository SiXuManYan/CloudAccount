package com.fatcloud.account.feature.news.detail

import com.fatcloud.account.base.ui.BaseMVPWebActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.news.NewDetail

/**
 * Created by Wangsw on 2020/5/30 0030 16:30.
 * </br>
 * 资讯详情
 */
class NewsDetailActivity : BaseMVPWebActivity<NewsDetailPresenter>(), NewsDetailView{

    private var newsId: String? = ""



    override fun initViews() {
        webTitle =  "资讯详情"
        super.initViews()
        newsId = intent.getStringExtra(Constants.PARAM_ID)
        presenter.getNewsDetail(this,newsId)

    }

    override fun bindDetailData(it: NewDetail) {

        contentUrl = it.content
        initLoadUrl()
    }


}