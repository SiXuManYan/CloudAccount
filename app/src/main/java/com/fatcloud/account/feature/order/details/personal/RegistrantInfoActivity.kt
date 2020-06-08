package com.fatcloud.account.feature.order.details.personal

import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.PersonalOrderDetail

/**
 * Created by Wangsw on 2020/6/4 0004 14:03.
 * </br>
 *  个人业务订单详情 注册信息
 */
class RegistrantInfoActivity : BaseMVPActivity<RegistrantInfoPresenter>(), RegistrantInfoView {


    var orderId: String? = ""

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId() = R.layout.activity_personal_order_detail

    override fun initViews() {
        initExtra()
        presenter.getRegistrantInfo(this, orderId)
    }


    private fun initExtra() {

        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_ID)) {
            finish()
            return
        }
        orderId = intent.extras!!.getString(Constants.PARAM_ORDER_ID)
    }


    override fun bindDetailInfo(data: PersonalOrderDetail) {

    }


}