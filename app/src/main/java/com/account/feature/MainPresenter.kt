package com.account.feature

import com.account.base.common.BasePresenter
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/5/25 0025 16:22.
 * </br>
 *
 */
class MainPresenter @Inject constructor(private var mainView: MainView) : BasePresenter(mainView)   {
}