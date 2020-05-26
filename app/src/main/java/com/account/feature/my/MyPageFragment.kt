package com.account.feature.my

import android.view.View
import com.account.R
import com.account.base.ui.BaseFragment
import com.account.feature.home.HomePresenter
import com.account.feature.home.HomeView

/**
 * Created by Wangsw on 2020/5/25 0025 16:42.
 * </br>
 *
 */
class MyPageFragment: BaseFragment<MyPagePresenter>(), MyPageView {

    init {
        needMenuControl = true
    }

    override fun getLayoutId() = R.layout.fragment_my

    override fun initViews(parent: View) {

    }

    override fun loadOnVisible() {

    }
}