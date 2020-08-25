package com.fatcloud.account.wxapi

import android.content.Intent
import android.os.Bundle
import cn.sharesdk.wechat.utils.WechatHandlerActivity
import com.fatcloud.account.BuildConfig
import com.fatcloud.account.common.Constants
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.RxBus
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * Created by Wangsw on 2020/6/16 0016 15:42.
 * </br>
 * 处理微信SDK回调
 */
class WXEntryActivity : WechatHandlerActivity(), IWXAPIEventHandler {

    private var api: IWXAPI? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, BuildConfig.WECHAT_APPID)
        api?.handleIntent(intent, this)

    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        api!!.handleIntent(intent, this)
    }

    override fun onReq(p0: BaseReq?) = Unit


    override fun onResp(resp: BaseResp?) {
        when (resp?.type) {
            ConstantsAPI.COMMAND_SENDAUTH -> {
                handlePayResult(resp)
                finish()
            }
            else -> {
            }
        }

    }

    private fun handlePayResult(resp: BaseResp) {
        when (resp.errCode) {

            BaseResp.ErrCode.ERR_OK -> {
                if (resp is SendMessageToWX.Resp) {
                    // 分享成功
                    RxBus.post(Event(Constants.EVENT_SHARE_SUCCESS))
                } else if (resp is SendAuth.Resp) {
                    // 微信授权成功
                    // 拿到了微信返回的code,立马再去请求access_token
                    RxBus.post(Event(Constants.EVENT_AUTH_SUCCESS, resp.code))
                }
            }
            else ->{
                RxBus.post(Event(Constants.EVENT_AUTH_CANCEL))
            }
        }


    }


}