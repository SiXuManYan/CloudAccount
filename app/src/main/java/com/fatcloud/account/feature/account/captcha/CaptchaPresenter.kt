package com.fatcloud.account.feature.account.captcha

import android.os.Looper
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.R
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.common.Constants
import com.blankj.utilcode.util.StringUtils
import com.fatcloud.account.BuildConfig
import com.fatcloud.account.backstage.DataServiceFaker
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.data.CloudDataBase
import com.fatcloud.account.entity.users.User
import com.fatcloud.account.entity.wechat.WechatAuthInfo
import com.fatcloud.account.entity.wechat.WechatUserInfo
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.RxBus
import com.google.gson.Gson
import com.google.gson.JsonElement
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/2 0002 14:30.
 * </br>
 *
 */
class CaptchaPresenter @Inject constructor(private var captchaView: CaptchaView) : BasePresenter(captchaView) {

    lateinit var database: CloudDataBase @Inject set

    /**
     * 发送验证码
     */
    fun sendCaptcha(life: LifecycleOwner, targetAccount: String, actionTv: TextView) {
        requestApi(life, Lifecycle.Event.ON_DESTROY,
            apiService.sendCaptchaToTarget(targetAccount), object : BaseHttpSubscriber<JsonElement>(captchaView) {
                override fun onSuccess(data: JsonElement?) {
                    captchaView.captchaSendResult()
                    actionTv.isEnabled = false
                    countdown(actionTv, StringUtils.getString(R.string.get_again))
                }

                override fun onError(e: Throwable) {
                    actionTv.isEnabled = true
                    super.onError(e)
                    captchaView.captchaSendResult()
                }

            }
        )
    }


    /**
     * 倒计时
     * @param countdownView 倒计时显示的view
     * @param default       默认显示的文字
     */
    fun countdown(countdownView: TextView, default: String) {
        addSubscribe(
            Flowable.intervalRange(0, Constants.WAIT_DELAYS + 1L, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    countdownView.text = appContext.getString(R.string.count_down_format, Constants.WAIT_DELAYS - it)
                }
                .doOnComplete {
                    countdownView.text = default
                    countdownView.isEnabled = true
                }
                .subscribe())
    }


    /**
     * 校验验证码
     */
    fun checkCaptcha(life: LifecycleOwner, account: String, captcha: String) {
        requestApi(life, Lifecycle.Event.ON_DESTROY,
            apiService.checkCaptcha(account, captcha), object : BaseHttpSubscriber<JsonElement>(captchaView) {
                override fun onSuccess(data: JsonElement?) {
                    captchaView.captchaVerified(captcha, account)
                }
            }
        )

    }

    /**
     * 使用access_token ，和 openid ，通过微信api 获取 用户信息
     */
    fun getWechatUserInfoFromWechatApi(lifecycleOwner: LifecycleOwner, wechatAuthInfo: WechatAuthInfo, account: String, captcha: String) {

        val hashMap = HashMap<String, String>().apply {
            put("access_token", wechatAuthInfo.access_token)
            put("openid", wechatAuthInfo.openid)
        }
        val runnable = Runnable {
            val jsonStr = HttpRequest.httpGet(BuildConfig.WECHAT_OFFICIAL_API + "/sns/userinfo?", hashMap)
            if (jsonStr.isNotEmpty()) {

                val gson = Gson()
                val userInfo = gson.fromJson(jsonStr, WechatUserInfo::class.java)
                Looper.prepare()
                doWechatRegister(lifecycleOwner, userInfo, account, captcha)
                Looper.loop()

            }
        }
        Thread(runnable).start()
    }


    private fun doWechatRegister(lifecycleOwner: LifecycleOwner, wechatUserInfo: WechatUserInfo, account: String, captcha: String) {

        requestApi(lifecycleOwner,
            Lifecycle.Event.ON_DESTROY,
            apiService.doWechatLoginOrRegister(
                wechatUserInfo.headimgurl,
                wechatUserInfo.nickname,
                wechatUserInfo.openid,
                account,
                CommonUtils.getLocationInfo()[2],
                CommonUtils.getLocationInfo()[0],
                CommonUtils.getLocationInfo()[1],
                captcha,
                CommonUtils.getShareDefault().getString(Constants.SP_PUSH_DEVICE_ID)
            ),

            object : BaseHttpSubscriber<User>(captchaView) {

                override fun onSuccess(data: User?) {

                    data?.let {
                        // 登录成功
                        wechatRegisterSuccess(data, account)

                    }

                }
            })

    }


    /**
     * 微信注册成功
     */
    private fun wechatRegisterSuccess(it: User, account: String) {

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
        captchaView.wechatRegisterSuccess()
    }


}