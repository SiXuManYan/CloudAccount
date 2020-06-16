package com.fatcloud.account.feature.defray.result

import android.content.DialogInterface
import android.view.View
import butterknife.OnClick
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.feature.order.lists.OrderListActivity
import com.fatcloud.account.view.dialog.AlertDialog

/**
 * Created by Wangsw on 2020/6/16 0016 20:56.
 * </br>
 * 支付结果页
 */
class CloudPayResultActivity : BaseMVPActivity<CloudPayResultPresenter>(), CloudPayResultView {


    override fun getLayoutId(): Int = R.layout.activity_pay_result


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun initViews() {
        setMainTitle("支付成功")
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage("提示")
            .setPositiveButton("返回首页", AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, which ->
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
            }
            else -> {
            }
        }
    }


}