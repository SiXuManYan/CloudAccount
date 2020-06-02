package com.account.feature.account.password.login

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import butterknife.OnClick
import com.account.R
import com.account.base.ui.BaseMVPActivity
import com.account.common.CommonUtils
import com.account.common.Constants
import com.account.feature.account.captcha.CaptchaActivity
import com.blankj.utilcode.util.ToastUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_login_password.next_tv
import kotlinx.android.synthetic.main.activity_login_password.password_et
import kotlinx.android.synthetic.main.activity_login_password.password_rule_iv
import kotlinx.android.synthetic.main.activity_login_password_set.*

/**
 * Created by Wangsw on 2020/6/2 0002 18:09.
 * </br>
 *  密码登录
 */
class PasswordLoginActivity : BaseMVPActivity<PasswordLoginPresenter>(), PasswordLoginView {


    /**
     * 当前操作账号
     */
    private var currentAccount = ""

    /**
     * 是否为密文
     */
    private var isCipherText = false


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()


    override fun getLayoutId() = R.layout.activity_login_password

    override fun initViews() {
        initView()
        initEvent()
        initExtra()
    }

    private fun initView() {
        password_et.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {


                val passwordFirst = s.toString().trim()
                if (passwordFirst.isNullOrBlank()) {
                    next_tv.setBackgroundResource(R.drawable.shape_bg_4_gray)
                } else {
                    next_tv.setBackgroundResource(R.drawable.shape_bg_4_red)
                }


            }
        })
        password_et.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                next_tv.performClick()
                return@OnEditorActionListener true
            }
            return@OnEditorActionListener false
        })
    }

    private fun initEvent() {
        presenter.subsribeEvent(Consumer {
            when (it.code) {
                Constants.EVENT_LOGIN -> {
                    finish()
                }
                Constants.EVENT_PASSWORD_RESET_SUCCESS->{
                    // 密码重设成功
                    password_et.setText("")
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


    }


    @OnClick(
        R.id.password_rule_iv,
        R.id.next_tv,
        R.id.forget_password_tv
    )
    fun onClick(view: View) {

        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.password_rule_iv -> changeDisplayMethod()
            R.id.next_tv -> {
                passwordLogin()
            }
            R.id.forget_password_tv -> {

                // 忘记密码
                startActivity(
                    Intent(this, CaptchaActivity::class.java)
                        .putExtra(Constants.PARAM_ACCOUNT, currentAccount)
                        .putExtra(Constants.PARAM_CAPTCHA_MODE, CaptchaActivity.MODE_FORGET_PASSWORD)
                )
            }
            else -> {

            }

        }

    }


    private fun changeDisplayMethod() {

        if (isCipherText) {
            // 切换至明文
            password_rule_iv.setImageResource(R.drawable.ic_login_password_visible)
            password_et.transformationMethod = HideReturnsTransformationMethod.getInstance();
            confirm_et.transformationMethod = HideReturnsTransformationMethod.getInstance();

        } else {
            // 切换至密文
            password_rule_iv.setImageResource(R.drawable.ic_login_password_gone)
            password_et.transformationMethod = PasswordTransformationMethod.getInstance();
            confirm_et.transformationMethod = PasswordTransformationMethod.getInstance();
        }
        isCipherText = !isCipherText
    }


    /**
     * 密码登录
     */
    private fun passwordLogin() {

        val password = password_et.text.toString().trim()
        if (password.isBlank()) {
            ToastUtils.showShort("请输入密码")
            return
        }


        presenter.passwordLogin(this, currentAccount, password)
    }

    override fun loginSuccess() {
        finish()
    }


}