package com.account.feature.account.login

import com.account.R
import com.account.base.ui.BaseMVPActivity

/**
 * Created by Wangsw on 2020/6/1 0001 17:10.
 * </br>
 *
 */
class LoginActivity :BaseMVPActivity<LoginPresenter>(),LoginView{


    override fun getLayoutId() = R.layout.activity_login

    override fun initViews() {

    }

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()
}