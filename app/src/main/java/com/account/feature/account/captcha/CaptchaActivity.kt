package com.account.feature.account.captcha

import android.content.Intent
import android.view.View
import com.account.R
import com.account.base.ui.BaseMVPActivity
import com.account.common.Constants
import com.account.feature.account.password.PasswordSetActivity
import com.account.view.extend.VerificationCodeView
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_login_captcha.*

/**
 * Created by Wangsw on 2020/6/2 0002 14:30.
 * </br>
 *  验证码校验
 *  注册验证身份 + 忘记密码进行密码重置，验证身份
 */
class CaptchaActivity : BaseMVPActivity<CaptchaPresenter>(), CaptchaView {

    companion object {

        /**
         * 验证码用途 ：注册验证身份
         */
        var MODE_REGISTER = 0

        /**
         *  验证码用途 ：忘记密码进行密码重置，验证身份
         */
        var MODE_FORGET_PASSWORD = 1
    }

    /**
     * 当前操作账号
     */
    private var currentAccount = ""

    /**
     * 验证码用途
     */
    private var captchaMode = 0

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()


    override fun getLayoutId() = R.layout.activity_login_captcha

    override fun initViews() {
        initExtra()
        captcha_view.onCodeFinishListener = object : VerificationCodeView.OnCodeFinishListener {

            override fun onTextChange(view: View?, content: String?) = Unit

            override fun onComplete(view: View?, content: String?) {

                content?.let {
                    presenter.checkCaptcha(this@CaptchaActivity, currentAccount, it)
                }

            }


        }

        initEvent()

        presenter.sendCaptcha(this, currentAccount, action_tv)


    }

    private fun initEvent() {
        presenter.subsribeEvent(Consumer {
            when (it.code) {
                Constants.EVENT_LOGIN -> {
                    finish()
                }
                Constants.EVENT_PASSWORD_RESET_SUCCESS->{
                    finish()
                }
                else -> {
                }
            }
        })
    }

    private fun initExtra() {
        intent.getStringExtra(Constants.PARAM_ACCOUNT)?.let {
            currentAccount = it
        }

        intent.getIntExtra(Constants.PARAM_CAPTCHA_MODE, 0).apply {
            captchaMode = this
        }

    }

    override fun captchaSendResult() {
        captcha_target_tv.text = getString(R.string.captcha_target_format, currentAccount)
    }

    override fun captchaVerified(captcha: String) {
        when (captchaMode) {
            MODE_REGISTER -> {
                // 注册
                startActivity(
                    Intent(this, PasswordSetActivity::class.java)
                        .putExtra(Constants.PARAM_ACCOUNT, currentAccount)
                        .putExtra(Constants.PARAM_CAPTCHA, captcha)
                        .putExtra(Constants.PARAM_IS_PASSWORD_REGISTER_SET_MODE, true)
                )
            }

            MODE_FORGET_PASSWORD -> {
                // 忘记密码，重置密码
                startActivity(
                    Intent(this, PasswordSetActivity::class.java)
                        .putExtra(Constants.PARAM_ACCOUNT, currentAccount)
                        .putExtra(Constants.PARAM_IS_PASSWORD_REGISTER_SET_MODE, false)
                )
            }
            else -> {
            }
        }
    }


}