package com.fatcloud.account.feature.forms.enterprise.license

import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity

/**
 * Created by Wangsw on 2020/6/10 0010 18:30.
 * </br>
 *  企业营业执照表单
 */
class FormLicenseEnterpriseActivity : BaseMVPActivity<FormLicenseEnterprisePresenter>(), FormLicenseEnterpriseView {


    override fun getLayoutId() = R.layout.activity_form_license_enterprise

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun initViews() {

    }


}