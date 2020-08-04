package com.fatcloud.account.feature.account.captcha

import android.content.Intent
import android.view.View
import butterknife.OnClick
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.wechat.WechatAuthInfo
import com.fatcloud.account.feature.account.password.PasswordSetActivity
import com.fatcloud.account.view.extend.VerificationCodeView
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_login_captcha.*

/**
 * Created by Wangsw on 2020/6/2 0002 14:30.
 * </br>
 *  验证码校验
 *  注册验证身份 + 忘记密码进行密码重置，验证身份
 *  todo ：弹出软键盘获取焦点
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

        /**
         * 微信注册
         */
        var MODE_REGISTER_WECHAT = 3
    }

    /**
     * 当前操作账号
     */
    private var currentAccount = ""

    /**
     * 验证码用途
     */
    private var captchaMode = 0

    var wechatAuthInfo: WechatAuthInfo? = null

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
                Constants.EVENT_PASSWORD_RESET_SUCCESS -> {
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

        intent.getSerializableExtra(Constants.PARAM_DATA)?.let {
            wechatAuthInfo = it as WechatAuthInfo
        }

    }

    override fun captchaSendResult() {
        captcha_target_tv.text = getString(R.string.captcha_target_format, currentAccount)
    }

    override fun captchaVerified(captcha: String, account: String) {
        when (captchaMode) {
            MODE_REGISTER -> {
                // 注册
                startActivity(
                    Intent(this, PasswordSetActivity::class.java)
                        .putExtra(Constants.PARAM_ACCOUNT, currentAccount)
                        .putExtra(Constants.PARAM_CAPTCHA, captcha)
                        .putExtra(Constants.PARAM_IS_PASSWORD_REGISTER_SET_MODE, true)
                )
                finish()
            }

            MODE_FORGET_PASSWORD -> {
                // 忘记密码，重置密码
                startActivity(
                    Intent(this, PasswordSetActivity::class.java)
                        .putExtra(Constants.PARAM_ACCOUNT, currentAccount)
                        .putExtra(Constants.PARAM_IS_PASSWORD_REGISTER_SET_MODE, false)
                )
                finish()
            }

            MODE_REGISTER_WECHAT -> {
                // 进行微信注册
                wechatAuthInfo?.let {
                    presenter.getWechatUserInfoFromWechatApi(this, it, account, captcha)

                }

            }
            else -> {
            }
        }
    }


    override fun wechatRegisterSuccess() {
        finish()
    }


    @OnClick(
        R.id.action_tv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.action_tv -> {
                presenter.sendCaptcha(this, currentAccount, action_tv)
            }
            else -> {
            }
        }
    }

}