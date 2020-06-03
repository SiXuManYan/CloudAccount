package com.account.feature.account.login

import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import butterknife.OnClick
import com.account.R
import com.account.base.ui.BaseMVPActivity
import com.account.common.CommonUtils
import com.account.common.Constants
import com.account.common.Html5Url
import com.account.feature.account.captcha.CaptchaActivity
import com.account.feature.account.captcha.CaptchaActivity.Companion.MODE_REGISTER
import com.account.feature.account.password.login.PasswordLoginActivity
import com.account.feature.webs.WebCommonActivity
import com.blankj.utilcode.constant.RegexConstants.REGEX_MOBILE_EXACT
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.VibrateUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by Wangsw on 2020/6/1 0001 17:10.
 * </br>
 *  登录 和 注册
 */
class LoginActivity : BaseMVPActivity<LoginPresenter>(), LoginView {


    /**
     * 是否为注册模式
     *
     */
    private var isRegisterMode = false


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()


    override fun getLayoutId() = R.layout.activity_login


    override fun onBackPressed() {
        if (isRegisterMode) {
            animateChangeView()
        } else {
            super.onBackPressed()
        }
    }


    override fun initViews() {

        phone_aet.requestFocus()
        phone_aet.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                next_tv.performClick()
                return@OnEditorActionListener true
            }
            return@OnEditorActionListener false
        })

        phone_aet.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                val phone = s.toString().trim()
                if (phone.length == 11) {
                    next_tv.setBackgroundResource(R.drawable.shape_bg_4_red)
                } else {
                    next_tv.setBackgroundResource(R.drawable.shape_bg_4_gray)
                }
            }
        })


        // 用户协议
        register_protocol.movementMethod = LinkMovementMethod.getInstance()
        val ruleTitle = getString(R.string.register_protocol_title)
        val ruleValue = getString(R.string.register_protocol_value)
        register_protocol.text = SpanUtils()
            .append(ruleTitle)
            .append(ruleValue)
            .setClickSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    startActivity(
                        Intent(this@LoginActivity, WebCommonActivity::class.java)
                            .putExtra(Constants.PARAM_URL, Html5Url.SERVICE_AGREEMENT_URL)
                            .putExtra(Constants.PARAM_TITLE, getString(R.string.privacy_statement))
                            .putExtra(Constants.PARAM_WEB_REFRESH, false)
                    )
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = Color.BLUE
                    ds.isUnderlineText = true
                }
            }).create()

        setSignViews()
        initEvent()
    }

    private fun initEvent() {
        presenter.subsribeEvent(Consumer {
            when (it.code) {
                Constants.EVENT_LOGIN -> {
                    finish()
                }
                else -> {
                }
            }
        })
    }


    /**
     * 动画切换界面
     */
    private fun animateChangeView() {
        val animation = if (isRegisterMode) {
            AnimationUtils.loadAnimation(context, R.anim.fade_out_to_bottom)
        } else {
            AnimationUtils.loadAnimation(context, R.anim.fade_in_from_bottom)
        }
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) = Unit

            override fun onAnimationEnd(p0: Animation?) {
                setSignViews()
            }

            override fun onAnimationStart(p0: Animation?) = Unit
        })
        isRegisterMode = !isRegisterMode
        scroll_nsv.startAnimation(animation)
        iv_back.startAnimation(animation)

    }


    private fun setSignViews() {
        if (isRegisterMode) {
            register_protocol.visibility = View.VISIBLE
            authorized_login_ll.visibility = View.GONE
            switch_model_tv.text = getString(R.string.login)
        } else {
            register_protocol.visibility = View.GONE
            authorized_login_ll.visibility = View.VISIBLE
            switch_model_tv.text = getString(R.string.register)

        }
    }


    @OnClick(
        R.id.next_tv,
        R.id.switch_model_tv,
        R.id.wechat_login_iv,
        R.id.alipay_login_iv

    )
    fun onClick(view: View) {

        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {

            R.id.next_tv -> {
                val account = phone_aet.text.toString().trim()
                if (account.isEmpty()) {
                    VibrateUtils.vibrate(10)
                    ToastUtils.showShort("请输入您的手机号码")
                    phone_aet.startAnimation(CommonUtils.getShakeAnimation(5))
                    return
                }

                if (!RegexUtils.isMatch(REGEX_MOBILE_EXACT, account)) {
                    VibrateUtils.vibrate(10)
                    ToastUtils.showShort("请输入正确格式的手机号码")
                    phone_aet.startAnimation(CommonUtils.getShakeAnimation(5))
                    return
                }
                if (isRegisterMode && !register_protocol.isChecked) {
                    VibrateUtils.vibrate(10)
                    register_protocol.startAnimation(CommonUtils.getShakeAnimation(3))
                    return
                }
                presenter.checkAccountIsExisted(this, account)
            }

            R.id.switch_model_tv -> {
                animateChangeView()
            }
            R.id.wechat_login_iv -> {
                VibrateUtils.vibrate(10)
                ToastUtils.showShort("开发中")
            }
            R.id.alipay_login_iv -> {
                VibrateUtils.vibrate(10)
                ToastUtils.showShort("开发中")
            }

        }

    }


    override fun accountExistedTag(existed: Boolean, account: String) {

        if (existed) {
            // 登录

            startActivity(
                Intent(this, PasswordLoginActivity::class.java)
                    .putExtra(Constants.PARAM_ACCOUNT, account)

            )
        } else {
            // 注册
            startActivity(
                Intent(this, CaptchaActivity::class.java)
                    .putExtra(Constants.PARAM_ACCOUNT, account)
                    .putExtra(Constants.PARAM_CAPTCHA_MODE, MODE_REGISTER)
            )

        }


    }


}