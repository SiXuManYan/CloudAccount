package com.fatcloud.account.feature.order.details

import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity

/**
 * Created by Wangsw on 2020/6/4 0004 10:14.
 * </br>
 *  订单详情
 */
class OrderDetailActivity : BaseMVPActivity<OrderDetailPresenter>(), OrderDetailView {

    override fun getLayoutId() = R.layout.activity_order_detail

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun initViews() {
        TODO("Not yet implemented")
    }


}