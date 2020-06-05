package com.fatcloud.account.feature.order.details.personal

import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity

/**
 * Created by Wangsw on 2020/6/4 0004 14:03.
 * </br>
 *  个人业务订单详情
 */
class PersonalOrderDetailActivity : BaseMVPActivity<PersonalOrderDetailPresenter>(), PersonalOrderDetailView {


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId() = R.layout.activity_personal_order_detail

    override fun initViews() {
        TODO("Not yet implemented")
    }

}