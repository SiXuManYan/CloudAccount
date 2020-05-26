package com.account.feature.home

import com.account.base.common.BasePresenter
import javax.inject.Inject

class HomePresenter @Inject constructor(private var homeView: HomeView) : BasePresenter(homeView) {

}
