package com.fatcloud.account.feature.defray.result

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import butterknife.OnClick
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.RxBus
import com.fatcloud.account.feature.forms.market.FormMarketActivity
import com.fatcloud.account.feature.order.lists.OrderListActivity
import com.fatcloud.account.view.dialog.AlertDialog
import kotlinx.android.synthetic.main.activity_pay_result.*

/**
 * Created by Wangsw on 2020/6/16 0016 20:56.
 * </br>
 * 支付结果页
 */
class CloudPayResultActivity : BaseMVPActivity<CloudPayResultPresenter>(), CloudPayResultView {

    var orderId: String? = ""
    var mMold: String? = ""

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
        mMold = intent.extras!!.getString(Constants.PARAM_MOLD)

        switchView()

        presenter.countdown(countdown_hint_tv)
    }

    private fun switchView() {
        when (mMold) {
            Constants.P1, Constants.P2, Constants.P5, Constants.P6, Constants.P9, Constants.P10 -> {
                countdown_hint_tv.visibility = View.VISIBLE
                common_hint_tv.visibility = View.GONE
                common_next_tv.visibility = View.GONE
            }
            else -> {
                countdown_hint_tv.visibility = View.GONE
                common_hint_tv.visibility = View.VISIBLE
                common_next_tv.visibility = View.VISIBLE
            }
        }
    }

    override fun onBackPressed() {
        if (mMold != Constants.P1 || mMold != Constants.P2 || mMold != Constants.P5 || mMold != Constants.P6 || mMold != Constants.P9 || mMold != Constants.P10) {
            AlertDialog.Builder(this)
                .setMessage("提示")
                .setCancelable(false)
                .setPositiveButton("返回首页", AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, which ->
                    RxBus.post(Event(Constants.EVENT_SWITCH_HOME_TAB))
                    super.onBackPressed()
                    dialog.dismiss()
                })
                .setNegativeButton("查看订单", AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, which ->
                    startActivity(OrderListActivity::class.java)
                    super.onBackPressed()
                    dialog.dismiss()
                })
                .create()
                .show()
        }

    }


    override fun countdownComplete() {

        startActivity(FormMarketActivity::class.java, Bundle().apply {
            putString(Constants.PARAM_ORDER_ID, orderId)
        })
        finish()
    }

    @OnClick(
        R.id.next_tv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.next_tv -> {
                startActivity(OrderListActivity::class.java)
                finish()
            }
            else -> {
            }
        }
    }


}