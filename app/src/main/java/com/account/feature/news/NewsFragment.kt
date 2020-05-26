package com.account.feature.news

import android.view.View
import com.account.R
import com.account.base.ui.BaseFragment

/**
 * Created by Wangsw on 2020/5/25 0025 16:42.
 * </br>
 *
 */
class NewsFragment : BaseFragment<NewsPresenter>(), NewsView {
    init {
        needMenuControl = true
    }

    override fun getLayoutId() = R.layout.fragment_news

    override fun initViews(parent: View) {

    }

    override fun loadOnVisible() {

    }
}