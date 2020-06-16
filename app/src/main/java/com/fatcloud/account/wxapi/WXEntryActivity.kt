package com.fatcloud.account.wxapi

import com.fatcloud.account.BuildConfig
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.event.RxBus
import com.fatcloud.account.event.entity.WechatPayResultEvent
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * Created by Wangsw on 2020/6/16 0016 15:42.
 * </br>
 * 处理微信SDK回调
 */
class WXEntryActivity : BaseMVPActivity<WXEntryPresenter>(), WXEntryView {

    private var api: IWXAPI? = null

    override fun getLayoutId(): Int = R.layout.activity_wx_entry

    override fun initViews() {
        api = WXAPIFactory.createWXAPI(this, BuildConfig.WECHAT_APPID)
        api?.handleIntent(intent, this)

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun onResp(resp: BaseResp?) {
        when (resp?.type) {
            ConstantsAPI.COMMAND_PAY_BY_WX -> {
                handlePayResult(resp)
            }
            else -> {
            }
        }

    }

    private fun handlePayResult(resp: BaseResp) {
        when (resp.errCode) {
            0 -> {
                // 支付成功
                RxBus.post(WechatPayResultEvent(resp.errCode))
            }
            -2 -> {
                // 用户取消支付
                RxBus.post(WechatPayResultEvent(resp.errCode))
            }
            else -> {
                // 支付错误 (-1)
                RxBus.post(WechatPayResultEvent(resp.errCode))
            }
        }


    }

    override fun onReq(p0: BaseReq?) {

    }


}