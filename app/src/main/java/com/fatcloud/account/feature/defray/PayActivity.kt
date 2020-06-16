package com.fatcloud.account.feature.defray

import android.view.View
import butterknife.OnClick
import com.blankj.utilcode.util.ToastUtils
import com.fatcloud.account.BuildConfig
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.defray.WechatPayInfo
import com.fatcloud.account.event.entity.WechatPayResultEvent
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_pay.*
import java.math.BigDecimal

/**
 * Created by Wangsw on 2020/6/16 0016 16:02.
 * </br>
 *
 */
class PayActivity : BaseMVPActivity<PayPresenter>(), PayView {

    private var orderId = ""
    private var finalMoney = ""
    private var api: IWXAPI? = null

    override fun getLayoutId(): Int = R.layout.activity_pay


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun initViews() {
        initExtra()
        initEvent()
        initView()
    }

    private fun initEvent() {
        presenter.subsribeEventEntity<WechatPayResultEvent>(Consumer {

            when (it.resultCode) {
                0 -> {
                    // 支付成功
                    ToastUtils.showShort("支付成功")
                }
                -2 -> {
                    // 用户取消支付
                    ToastUtils.showShort("您取消了支付")
                }
                else -> {
                    // 支付错误 (-1)
                    ToastUtils.showShort("支付错误")
                }
            }
            finish()

        })
    }


    private fun initExtra() {
        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_ID) || !intent.extras!!.containsKey(Constants.PARAM_MONEY)) {
            finish()
            return
        }
        orderId = intent.extras!!.getString(Constants.PARAM_ORDER_ID, "")
        finalMoney = intent.extras!!.getString(Constants.PARAM_MONEY, "")
    }

    private fun initView() {
        setMainTitle("支付详情 ")
        if (finalMoney.isNotEmpty()) {
            card_money_tv.text = BigDecimal(finalMoney).toPlainString()
            bottom_tv.text = getString(R.string.pay_bottom_format, BigDecimal(finalMoney).toPlainString())
        }

        wechat_rb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                alipay_rb.isChecked = false
            }
        }
        alipay_rb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                wechat_rb.isChecked = false
            }
        }
        wechat_rb.isChecked = true
    }


    @OnClick(
        R.id.pay_tv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.pay_tv -> handlePayment()
            else -> {
            }
        }
    }

    private fun handlePayment() {
        if (wechat_rb.isChecked) {
            presenter.wechatUnifiedOrder(this, orderId)
        } else {
            presenter.alipayUnifiedOrder(this, orderId)
        }
    }


    override fun doWechatPay(it: WechatPayInfo) {
        val wechatRunnable: Runnable = Runnable {
            api = WXAPIFactory.createWXAPI(this@PayActivity, BuildConfig.WECHAT_APPID, false).apply {
                registerApp(BuildConfig.WECHAT_APPID)
            }
            val req = PayReq().apply {
                appId = BuildConfig.WECHAT_APPID
                partnerId = it.partnerid
                prepayId = it.prepayid
                nonceStr = it.noncestr
                packageValue = it.`package`
                sign = it.sign
                timeStamp = it.timestamp
                // extData = "" // 可传递extra，在回调页中获取
            }
            api?.sendReq(req)
        }
        Thread(wechatRunnable).run()


    }


}