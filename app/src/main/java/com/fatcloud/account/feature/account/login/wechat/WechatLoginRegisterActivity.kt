package com.fatcloud.account.feature.account.login.wechat

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import butterknife.OnClick
import com.blankj.utilcode.constant.RegexConstants
import com.blankj.utilcode.util.*
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.wechat.WechatAuthInfo
import com.fatcloud.account.feature.account.captcha.CaptchaActivity
import com.fatcloud.account.feature.webs.WebCommonActivity
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_login_wechat.*

/**
 * Created by Wangsw on 2020/6/18 0018 20:43.
 * </br>
 * 微信登录和注册
 */
class WechatLoginRegisterActivity : BaseMVPActivity<WechatLoginRegisterPresenter>(), WechatLoginRegisterView {

    var wechatAuthInfo: WechatAuthInfo? = null

    override fun getLayoutId() = R.layout.activity_login_wechat

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun initViews() {
        initExtra()
        initEvent()
        initPageView()
    }


    private fun initExtra() {
        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_DATA)) {
            finish()
            return
        }
        wechatAuthInfo = intent.extras!!.getSerializable(Constants.PARAM_DATA) as WechatAuthInfo?

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


    private fun initPageView() {
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
                        Intent(this@WechatLoginRegisterActivity, WebCommonActivity::class.java)
                            .putExtra(Constants.PARAM_URL, "fu_wu_xie_yi.html")
                            .putExtra(Constants.PARAM_TITLE, "服务协议")
                            .putExtra(Constants.PARAM_WEB_REFRESH, false)
                            .putExtra(Constants.PARAM_WEB_LOAD_LOCAL_HTML, true)

                    )
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = ColorUtils.getColor(R.color.color_118EEA)
                    ds.isUnderlineText = false
                }
            }).create()

    }


    @OnClick(
        R.id.next_tv

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

                if (!RegexUtils.isMatch(RegexConstants.REGEX_MOBILE_EXACT, account)) {
                    VibrateUtils.vibrate(10)
                    ToastUtils.showShort("请输入正确格式的手机号码")
                    phone_aet.startAnimation(CommonUtils.getShakeAnimation(5))
                    return
                }
                if (!register_protocol.isChecked) {
                    VibrateUtils.vibrate(10)
                    register_protocol.startAnimation(CommonUtils.getShakeAnimation(3))
                    return
                }


                // 检查用户是否存在
//                presenter.checkAccountIsExisted(this,account)


                wechatAuthInfo?.let {

                    // 微信登陆校验验证码
                    startActivity(CaptchaActivity::class.java, Bundle().apply {
                        putString(Constants.PARAM_ACCOUNT, account)
                        putInt(Constants.PARAM_CAPTCHA_MODE, CaptchaActivity.MODE_REGISTER_WECHAT)
                        putSerializable(Constants.PARAM_DATA, it)
                    })

                }


            }

        }

    }


    override fun accountExistedTag(existed: Boolean, account: String) {
        if (existed) {
            // 微信注册，输入的手机号码已经存在
            ToastUtils.showShort("手机号已存在，请重新输入")
        } else {

            // 手机号码不存在，进行验证码验证，后续进行微信注册
            wechatAuthInfo?.let {

                // 微信登陆校验验证码
                startActivity(CaptchaActivity::class.java, Bundle().apply {
                    putString(Constants.PARAM_ACCOUNT, account)
                    putInt(Constants.PARAM_CAPTCHA_MODE, CaptchaActivity.MODE_REGISTER_WECHAT)
                    putSerializable(Constants.PARAM_DATA, it)
                })

            }


        }
    }


}