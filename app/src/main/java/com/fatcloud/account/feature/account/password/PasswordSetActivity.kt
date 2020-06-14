package com.fatcloud.account.feature.account.password

import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import butterknife.OnClick
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.VibrateUtils
import kotlinx.android.synthetic.main.activity_login_password_set.*

/**
 * Created by Wangsw on 2020/6/2 0002 15:54.
 * </br>
 * 注册设置密码 和 登录后重置密码
 */
class PasswordSetActivity : BaseMVPActivity<PasswordSetPresenter>(), PasswordSetView {

    /**
     * 页面类型
     * true  注册设置密码
     * false 登录后修改密码
     *
     */
    private var isRegisterMode = false

    /**
     * 是否为密文
     */
    private var isCipherText = true

    /**
     * 账号
     */
    private var account = ""

    /**
     * 验证码
     */
    private var captcha = ""


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId() = R.layout.activity_login_password_set

    override fun initViews() {

        initExtra()
        initView()
    }

    private fun initView() {
        password_et.requestFocus()
//        KeyboardUtils.showSoftInput(password_et)
        if (isRegisterMode) {
            confirm_rl.visibility = View.VISIBLE
            rule_same_cb.visibility = View.VISIBLE
            password_et.imeOptions = EditorInfo.IME_ACTION_NEXT
        } else {
            confirm_rl.visibility = View.GONE
            rule_same_cb.visibility = View.GONE
            password_et.imeOptions = EditorInfo.IME_ACTION_DONE
        }


        password_et.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {


                val passwordFirst = s.toString().trim()
                if (passwordFirst.isNullOrBlank()) {
                    rule_format_cb.isChecked = false
                    return
                }

                rule_length_cb.isChecked = passwordFirst.length in 6..18
                rule_format_cb.isChecked = RegexUtils.isMatch("^[A-Za-z0-9]+$", passwordFirst)

                changeActionButtonBackground(!isRegisterMode && rule_length_cb.isChecked && rule_format_cb.isChecked)
            }
        })

        confirm_et.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString().trim()
                if (password.isNullOrBlank()) {
                    rule_same_cb.isChecked = false
                    return
                }

                val isSame = (password == password_et.text.toString().trim())
                rule_same_cb.isChecked = isSame

                changeActionButtonBackground(isSame)

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


    private fun initExtra() {
        intent.getStringExtra(Constants.PARAM_ACCOUNT)?.let {
            account = it
        }

        intent.getStringExtra(Constants.PARAM_CAPTCHA)?.let {
            captcha = it
        }

        intent.getBooleanExtra(Constants.PARAM_IS_PASSWORD_REGISTER_SET_MODE, false)?.let {
            isRegisterMode = it
        }

    }


    @OnClick(
        R.id.password_rule_iv,
        R.id.next_tv
    )
    fun onClick(view: View) {

        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.password_rule_iv -> changeDisplayMethod()
            R.id.next_tv -> {
                handlePasswordSet()
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
        password_et.setSelection(password_et.text.toString().length)
        confirm_et.setSelection(confirm_et.text.toString().length)

        isCipherText = !isCipherText

    }

    private fun handlePasswordSet() {
        if (!rule_length_cb.isChecked) {
            rule_length_cb.startAnimation(CommonUtils.getShakeAnimation(5))
            VibrateUtils.vibrate(10)
            return
        }
        if (!rule_format_cb.isChecked) {
            rule_format_cb.startAnimation(CommonUtils.getShakeAnimation(5))
            VibrateUtils.vibrate(10)
            return
        }

        if (isRegisterMode && !rule_same_cb.isChecked) {
            VibrateUtils.vibrate(10)
            rule_same_cb.startAnimation(CommonUtils.getShakeAnimation(5))
            return
        }

        val passWord = password_et.text.toString().trim()
        if (isRegisterMode) {
            // 注册
            presenter.register(this, passWord, account, captcha)
        } else {
            // 重置密码
            presenter.resetPassword(this, passWord, account)
        }
    }

    override fun registerSuccess() = finish()

    override fun passwordResetSuccess() = finish()

    private fun changeActionButtonBackground(positive: Boolean) {
        if (positive) {
            next_tv.setBackgroundResource(R.drawable.shape_bg_4_red)
        } else {
            next_tv.setBackgroundResource(R.drawable.shape_bg_4_gray)
        }
    }


}