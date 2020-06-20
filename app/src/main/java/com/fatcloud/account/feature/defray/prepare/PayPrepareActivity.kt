package com.fatcloud.account.feature.defray.prepare

import android.content.DialogInterface
import android.content.Intent
import android.view.View
import butterknife.OnClick
import com.fatcloud.account.R
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.RxBus
import com.fatcloud.account.event.entity.OrderPaySuccessEvent
import com.fatcloud.account.feature.defray.PayActivity
import com.fatcloud.account.feature.order.lists.OrderListActivity
import com.fatcloud.account.view.dialog.AlertDialog
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_pay_prepare.*
import java.math.BigDecimal

/**
 * Created by Wangsw on 2020/6/17 0017 18:49.
 * </br>
 * 支付过度页
 */
class PayPrepareActivity : BaseMVPActivity<PayPreparePresenter>(), PayPrepareView {

    private var orderId = ""
    private var orderNo = ""
    private var money = ""
    private var productLogoImgUrl = ""
    private var productName = ""
    private var createDt = ""
    private var closePayListenerRequest = 1


    override fun getLayoutId(): Int = R.layout.activity_pay_prepare

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()


    override fun initViews() {

        initExtra()
        initEvent()
    }


    private fun initEvent() {
        presenter.subsribeEventEntity<OrderPaySuccessEvent>(Consumer {
            finish()
        })

        presenter.subsribeEvent(Consumer {
            when (it.code) {
                Constants.EVENT_CLOSE_PAY_UNKNOWN -> {
                    finish()
                }
                else -> {
                }
            }
        })

    }

    private fun initExtra() {
        if (intent.extras == null
            || !intent.extras!!.containsKey(Constants.PARAM_ORDER_ID)
            || !intent.extras!!.containsKey(Constants.PARAM_ORDER_NUMBER)
            || !intent.extras!!.containsKey(Constants.PARAM_MONEY)
            || !intent.extras!!.containsKey(Constants.PARAM_IMAGE_URL)
            || !intent.extras!!.containsKey(Constants.PARAM_PRODUCT_NAME)
            || !intent.extras!!.containsKey(Constants.PARAM_DATE)
        ) {
            finish()
            return
        }
        orderId = intent.extras!!.getString(Constants.PARAM_ORDER_ID, "")
        orderNo = intent.extras!!.getString(Constants.PARAM_ORDER_NUMBER, "")
        money = intent.extras!!.getString(Constants.PARAM_MONEY, "")
        productLogoImgUrl = intent.extras!!.getString(Constants.PARAM_IMAGE_URL, "")
        productName = intent.extras!!.getString(Constants.PARAM_PRODUCT_NAME, "")
        createDt = intent.extras!!.getString(Constants.PARAM_DATE, "")
        setData()
    }

    private fun setData() {
        setMainTitle(productName)
        Glide.with(this).load(productLogoImgUrl).into(image_iv)
        content_tv.text = productName
        time_tv.text = createDt
        if (!money.isBlank()) {
            val money = BigDecimal(money).toPlainString().apply {
                CommonUtils.setFormatText(amount_tv, getString(R.string.money_symbol_with_blank), this, 8, 14)
                CommonUtils.setFormatText(univalent_tv, getString(R.string.money_symbol_with_blank), this, 12, 18)
            }
        }
    }


    @OnClick(
        R.id.defray
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.defray -> {
                startActivityForResult(
                    Intent(this, PayActivity::class.java)
                        .putExtra(Constants.PARAM_ORDER_ID, orderId)
                        .putExtra(Constants.PARAM_ORDER_NUMBER, orderNo)
                        .putExtra(Constants.PARAM_MONEY, money), closePayListenerRequest
                )
            }
            else -> {
            }
        }
    }

    override fun onBackPressed() {
        RxBus.post(Event(Constants.EVENT_FORM_CLOSE))
        AlertDialog.Builder(this)
            .setTitle("提示")
            .setMessage("订单已提交")
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