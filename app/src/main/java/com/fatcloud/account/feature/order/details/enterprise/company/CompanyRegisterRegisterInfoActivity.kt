package com.fatcloud.account.feature.order.details.enterprise.company

import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.Constants

/**
 * Created by Wangsw on 2020/6/4 0004 14:03.
 * </br>
 *  公司信息
 */
class CompanyRegisterRegisterInfoActivity : BaseMVPActivity<CompanyRegisterInfoPresenter>(),
    CompanyRegisterInfoView {

    var orderWorkId: String? = ""

    override fun getLayoutId() = R.layout.activity_order_detail_company

    override fun initViews() {
        initExtra()
    }


    private fun initExtra() {

        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_WORK_ID)) {
            finish()
            return
        }
        orderWorkId = intent.extras!!.getString(Constants.PARAM_ORDER_WORK_ID)
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }
}