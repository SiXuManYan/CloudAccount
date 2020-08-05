package com.fatcloud.account.feature.defray.result

import android.os.Bundle
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.RxBus
import com.fatcloud.account.feature.forms.market.FormMarketActivity
import kotlinx.android.synthetic.main.activity_pay_result.*

/**
 * Created by Wangsw on 2020/6/16 0016 20:56.
 * </br>
 * 支付结果页
 */
class CloudPayResultActivity : BaseMVPActivity<CloudPayResultPresenter>(), CloudPayResultView {

    var orderId: String? = ""

    override fun getLayoutId(): Int = R.layout.activity_pay_result

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun initViews() {
        setMainTitle("支付成功")

        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_ID)) {
            finish()
            return
        }
        orderId = intent.extras!!.getString(Constants.PARAM_ORDER_ID)

        presenter.countdown(countdown_tv)
    }

    override fun onBackPressed() {
//        AlertDialog.Builder(this)
//            .setMessage("提示")
//            .setCancelable(false)
//            .setPositiveButton("返回首页", AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, which ->
//                RxBus.post(Event(Constants.EVENT_SWITCH_HOME_TAB))
//                super.onBackPressed()
//                dialog.dismiss()
//            })
//            .setNegativeButton("查看订单", AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, which ->
//                startActivity(OrderListActivity::class.java)
//                super.onBackPressed()
//                dialog.dismiss()
//            })
//            .create()
//            .show()

    }


    override fun countdownComplete() {


        startActivity(FormMarketActivity::class.java, Bundle().apply {
            putString(Constants.PARAM_ORDER_ID, orderId)
        })
        RxBus.post(Event(Constants.EVENT_SWITCH_HOME_TAB))
        finish()

    }


}