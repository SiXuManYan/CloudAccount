package com.account.feature.home

import android.view.View
import com.account.R
import com.account.base.ui.BaseFragment

/**
 * Created by Wangsw on 2020/5/25 0025 16:41.
 * </br>
 * 首页
 */
class HomeFragment : BaseFragment<HomePresenter>(), HomeView {

    init {
        needMenuControl = true
    }

    override fun getLayoutId() = R.layout.fragment_home

    override fun initViews(parent: View) {

    }

    override fun loadOnVisible() {

    }
}