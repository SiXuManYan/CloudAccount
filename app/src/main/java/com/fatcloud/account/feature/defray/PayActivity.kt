package com.fatcloud.account.feature.defray

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import butterknife.OnClick
import com.alipay.sdk.app.PayTask
import com.blankj.utilcode.util.ToastUtils
import com.fatcloud.account.BuildConfig
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.AndroidUtil
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ShareUtil
import com.fatcloud.account.entity.defray.AlipayResultStatus
import com.fatcloud.account.entity.defray.PayResult
import com.fatcloud.account.entity.defray.WechatPayInfo
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.RxBus
import com.fatcloud.account.event.entity.OrderPaySuccessEvent
import com.fatcloud.account.event.entity.WechatPayResultEvent
import com.fatcloud.account.feature.defray.result.CloudPayResultActivity
import com.fatcloud.account.feature.defray.unknown.PayUnknownActivity
import com.fatcloud.account.view.dialog.AlertDialog
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_pay.*
import java.math.BigDecimal


/**
 * Created by Wangsw on 2020/6/16 0016 16:02.
 * </br>
 * 支付状态页
 */
class PayActivity : BaseMVPActivity<PayPresenter>(), PayView {

    private var orderId = ""
    private var orderNumber = ""
    private var finalMoney = ""
    private var mMold = ""
    private var api: IWXAPI? = null

    /**
     * 支付宝sdk正常运行
     */
    private val SDK_PAY_FLAG = 1

    /**
     * 支付宝支付同步结果返回，仅作为支付结束状态判断
     */
    @SuppressLint("HandlerLeak")
    private val handler = object : Handler() {

        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                SDK_PAY_FLAG -> {
                    handleAlipaySynchronizeResult(msg)
                }
                else -> {
                }
            }
        }


    }


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
            val resultCode = it.resultCode
            if (resultCode == -2) {
                ToastUtils.showShort("您已取消支付")
            } else {
                presenter.checkOrderRealPaymentStatus(this, orderId, orderNumber)

//                orderPaySuccess()
            }
        })
    }


    private fun initExtra() {
        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_ID)
            || !intent.extras!!.containsKey(Constants.PARAM_ORDER_ID) || !intent.extras!!.containsKey(Constants.PARAM_MONEY)
        ) {
            finish()
            return
        }
        orderId = intent.extras!!.getString(Constants.PARAM_ORDER_ID, "")
        orderNumber = intent.extras!!.getString(Constants.PARAM_ORDER_NUMBER, "")
        finalMoney = intent.extras!!.getString(Constants.PARAM_MONEY, "")
        mMold = intent.extras!!.getString(Constants.PARAM_MOLD, "")
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
                AndroidUtil.isWeixinAvilible(this)
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
        R.id.wechat_rl,
        R.id.alipay_rl,
        R.id.card_cv,

        R.id.pay_tv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.pay_tv -> handlePayment()
            R.id.wechat_rl -> if (!wechat_rb.isChecked) wechat_rb.isChecked = true
            R.id.alipay_rl -> if (!alipay_rb.isChecked) alipay_rb.isChecked = true
            R.id.card_cv -> {

            }
            else -> {
            }
        }
    }

    private fun handlePayment() {
        if (wechat_rb.isChecked) {
            if (!AndroidUtil.isWeixinAvilible(this)) {
                return
            }
            presenter.wechatUnifiedOrder(this, orderId)
        } else {
            presenter.alipayUnifiedOrder(this, orderId)
        }
    }


    /**
     * 进行微信支付
     * 异步调用
     */
    override fun doWechatPay(it: WechatPayInfo) {
//        val wechatRunnable: Runnable = Runnable {
//
//        }
//        Thread(wechatRunnable).start()

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
        }
        api?.sendReq(req)

    }


    /**
     * 支付宝支付，异步调用
     * @param orderInfo        app支付请求参数字符串，主要包含商户的订单信息，key=value形式，以&连接。
     * @param isShowPayLoading 用户在商户app内部点击付款，是否需要一个loading做为在钱包唤起之前的过渡，
     *                         这个值设置为true，将会在调用pay接口的时候直接唤起一个loading，
     *                         直到唤起H5支付页面或者唤起外部的钱包付款页面loading才消失。
     *                         （建议将该值设置为true，优化点击付款到支付唤起支付页面的过渡过程。）
     */
    override fun doAliPay(it: String) {
        val alipayRunnable: Runnable = Runnable {
            val alipay = PayTask(this)
            val result = alipay.payV2(it, true)
            Log.i("支付宝支付 result = ", result.toString())

            val msg = Message().apply {
                what = SDK_PAY_FLAG
                obj = result
            }
            handler.sendMessage(msg)
        }
        Thread(alipayRunnable).start()

    }


    /**
     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
     */
    @Suppress("UNCHECKED_CAST")
    private fun handleAlipaySynchronizeResult(msg: Message) {
        val payResult = PayResult(msg.obj as Map<String?, String?>)
        val resultInfo = payResult.result
        val resultStatus = payResult.resultStatus
        if (resultStatus == AlipayResultStatus.Status_6001) {
            ToastUtils.showShort("您已取消支付")
        } else {
            presenter.checkOrderRealPaymentStatus(this, orderId, orderNumber)
        }

    }

    override fun orderPaySuccess() {
        startActivity(CloudPayResultActivity::class.java, Bundle().apply {
            putString(Constants.PARAM_ORDER_ID, orderId)
            putString(Constants.PARAM_MOLD, mMold)
        })
        RxBus.post(OrderPaySuccessEvent())
        finish()
    }


    override fun finish() {
        RxBus.post(Event(Constants.EVENT_CLOSE_PAY))
        setResult(Activity.RESULT_OK)
        super.finish()
    }

    override fun checkOrderRealPaymentStatusFailure() {
        RxBus.post(Event(Constants.EVENT_CLOSE_PAY_UNKNOWN))
        startActivityClearTop(PayUnknownActivity::class.java, null)
        finish()

    }


}