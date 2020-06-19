package com.fatcloud.account.feature.account.login

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.backstage.DataServiceFaker
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.data.CloudDataBase
import com.fatcloud.account.entity.users.User
import com.fatcloud.account.entity.wechat.WechatAuthInfo
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.RxBus
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/1 0001 17:10.
 * </br>
 *
 */
class LoginPresenter @Inject constructor(private var loginView: LoginView) : BasePresenter(loginView) {

    lateinit var database: CloudDataBase @Inject set

    /**
     * 检查用户是否存在
     */
    fun checkAccountIsExisted(lifecycleOwner: LifecycleOwner, account: String) {

        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.checkAccountIsExisted(account), object : BaseHttpSubscriber<JsonObject>(loginView) {
                override fun onSuccess(data: JsonObject?) {


                    data?.let {
                        if (it.has("existed")) {
                            loginView.accountExistedTag(it.get("existed").asBoolean, account)
                        }
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    loginView.accountExistedTag(false, account)
                }

            }
        )

    }

    /**
     * 微信authCode登录
     */
    fun getWechatAccessToken(lifecycleOwner: LifecycleOwner, authCode: String?) {

        requestApi(lifecycleOwner,
            Lifecycle.Event.ON_DESTROY,
            apiService.getWechatAccessToken(code = authCode),

            object : BaseHttpSubscriber<JsonObject>(loginView) {

                override fun onSuccess(data: JsonObject?) {

/*
                {
                    "code" : "200",
                    "msg" : "成功",
                    "data" : {
                    "mold" : "L0",
                    "info" : "{\"access_token\":\"34_0JlJfDcj3PWkQgnfr55UG1pBdigVtuBPgOaeaEtajR84nRgmd8uLbcqVcTvV2VofiR9-CwBPQ9DtpfRR81T07Py8Fvz4aaTLam-CRhB-zNI\",\"expires_in\":7200,\"refresh_token\":\"34_WBRyRUs0goXEDqLexV3GXPQWLoNKJPiVx-wz-kVpbiAV5hOGduaChBEOPwY91vSq2wyj2WPZupXMjlzL4vR9akcCHB1rTcVmqecoe8xwH88\",\"openid\":\"oHGlZ6MA14PNBBbrBr8x0yR6j21E\",\"scope\":\"snsapi_userinfo\",\"unionid\":\"oZ3Fg1f6A-0gZB5OOaXX3dD_e0o0\"}"
                             }
                }
            */

                    /*
                        "code" : "200",
                        "msg" : "成功",
                        "data" : {
                            "mold" : "L1",
                            "info" : {
                                "token" : "01f8297c6aca41cea78bf047ef56d61f",
                                "username" : "17600001112",
                                "nickName" : "明天也很困",
                                "headUrl" : "http://thirdwx.qlogo.cn/mmopen/vi_32/8rgx7V7WuRKPMGeTtrQEDEpdYITcLzdKRt2FxQ3OURMqHLTXlibbVP2d58o220qK5nCkG6bAvib1gsaicAiaUoDsYg/132"
                            }
                        }
                    }

        */


                    data?.let {

                        if (data.has("mold")) {

                            val moldStr = data.get("mold").asString
                            when (moldStr) {

                                Constants.L0 -> {
                                    if (data.has("info")) {
                                        val infoStr = data.get("info").asString
                                        val wechatInfo = gson.fromJson(infoStr, WechatAuthInfo::class.java)
                                        loginView.getWechatAccessTokenSuccess(wechatInfo)
                                    }
                                }

                                Constants.L1 -> {
                                    // 老用户，直接登录成功
                                    if (data.has("info")) {
                                        val infoObject = data.get("info").asJsonObject
                                        val userInfo = gson.fromJson(infoObject, User::class.java)

                                        wechatLoginSuccess(userInfo, userInfo.username)
                                        loginView.wechatLoginSuccess()

                                    }


                                }
                                else -> {


                                }
                            }


                        }


                    }

                }
            })

    }


    /**
     * 微信登陆成功
     */
    private fun wechatLoginSuccess(it: User, account: String) {

        // 更新用户
        database.userDao().addUser(it)
        User.update()

        // 更新登录状态
        CommonUtils.getShareDefault().put(Constants.SP_LOGIN, true)
        CommonUtils.getShareDefault().put(Constants.SP_TOKEN, it.token)
        CommonUtils.getShareDefault().put(Constants.SP_LAST_LOGIN_USER, account)

        // 刷新页面登录状态
        RxBus.post(Event(Constants.EVENT_LOGIN))
        RxBus.post(Event(Constants.EVENT_NEED_REFRESH))

        // 更新应用数据
        DataServiceFaker.startService(appContext, Constants.ACTION_SYNC_OTHER)
    }


}