package com.account.feature.account.captcha

import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.account.R
import com.account.base.common.BasePresenter
import com.account.base.net.BaseHttpSubscriber
import com.account.common.Constants
import com.blankj.utilcode.util.StringUtils
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
            apiService.checkCaptcha(account,captcha), object : BaseHttpSubscriber<JsonElement>(captchaView) {
                override fun onSuccess(data: JsonElement?) {
                    captchaView.captchaVerified(captcha)
                }
            }
        )

    }

}