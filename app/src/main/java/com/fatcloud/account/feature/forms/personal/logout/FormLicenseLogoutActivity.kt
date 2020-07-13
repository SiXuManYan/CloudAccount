package com.fatcloud.account.feature.forms.personal.logout

import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity

/**
 * Created by Wangsw on 2020/7/13 0013 16:38.
 * </br>
 * 个体户营业执照注销
 */
class FormLicenseLogoutActivity : BaseMVPActivity<FormLicenseLogoutPresenter>(), FormLicenseLogoutView {


    override fun getLayoutId() = R.layout.activity_form_license_logout_personal
    override fun initViews() {

    }

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()
}