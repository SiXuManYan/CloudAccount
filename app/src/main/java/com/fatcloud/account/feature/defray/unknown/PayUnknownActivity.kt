package com.fatcloud.account.feature.defray.unknown

import android.view.View
import butterknife.OnClick
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.feature.order.lists.OrderListActivity

/**
 * Created by Wangsw on 2020/6/20 0020 9:07.
 * </br>
 *  未知支付结果
 */
class PayUnknownActivity : BaseMVPActivity<PayUnknownPresenter>(), PayUnknownView {


    override fun getLayoutId(): Int {
        return R.layout.activity_pay_unknown
    }


    override fun initViews() = Unit

    override fun onBackPressed() = Unit


    @OnClick(
        R.id.view_order
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.view_order -> {
                startActivityClearTop(OrderListActivity::class.java,null)
                finish()
            }
            else -> {
            }
        }
    }

}