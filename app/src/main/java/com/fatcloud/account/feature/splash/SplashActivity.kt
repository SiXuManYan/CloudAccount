package com.fatcloud.account.feature.splash

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SpanUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.feature.MainActivity
import com.fatcloud.account.feature.webs.WebCommonActivity
import com.fatcloud.account.view.dialog.ActionAlertDialog
import com.mob.MobSDK
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Created by Wangsw on 2020/5/25 0025 15:44.
 * </br>
 *
 */
class SplashActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_splash

    override fun initViews() {
        if (!isTaskRoot) {
            finish();
            return;
        }
        CommonUtils.setStatusBarTransparent(this)
        BarUtils.setNavBarVisibility(this, false)

        val alphaAnimation = AlphaAnimation(0f, 1f).apply {
            duration = 1000
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    initUserAgreement()
                }

                override fun onAnimationStart(p0: Animation?) {
                }
            })
        }
        splash_container_ll.startAnimation(alphaAnimation)


    }

    private fun initUserAgreement() {

        val message = SpanUtils()
            .append(getString(R.string.use_agreement_content_0))
            .append(getString(R.string.use_agreement_content_1))
            .setClickSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    startActivity(
                        Intent(this@SplashActivity, WebCommonActivity::class.java)
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
            })
            .append(getString(R.string.use_agreement_content_2))
            .append(getString(R.string.use_agreement_content_3))
            .setClickSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    startActivity(
                        Intent(this@SplashActivity, WebCommonActivity::class.java)
                            .putExtra(Constants.PARAM_URL, "yin_si_sheng_ming.html")
                            .putExtra(Constants.PARAM_TITLE, "隐私政策")
                            .putExtra(Constants.PARAM_WEB_REFRESH, false)
                            .putExtra(Constants.PARAM_WEB_LOAD_LOCAL_HTML, true)
                    )
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = ColorUtils.getColor(R.color.color_118EEA)
                    ds.isUnderlineText = false
                }
            })
            .append(getString(R.string.use_agreement_content_4))
            .create()

        val ishowUserAgreement = CommonUtils.getShareDefault().getBoolean(Constants.SP_IS_SHOW_USER_AGREEMENT, false)
        if (ishowUserAgreement) {
            afterAnimation()
        } else {

            ActionAlertDialog.Builder(this)
                .setTitle(R.string.tips_0)
                .setCancelable(false)
                .setMessage(message)
                .setMessageMargins(11f, 15f, 11f, 25f)
                .setMessageScrollViewHeight(350f)
                .setMovementMethod()
                .setPositiveButton(R.string.agree_and_continue, ActionAlertDialog.SPECIAL, DialogInterface.OnClickListener { dialog, which ->
                    CommonUtils.getShareDefault().put(Constants.SP_IS_SHOW_USER_AGREEMENT, true)
                    afterAnimation()
                    dialog.dismiss()
                })
                .setNegativeButton(R.string.disagree, ActionAlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, which ->
                    CommonUtils.getShareDefault().put(Constants.SP_IS_SHOW_USER_AGREEMENT, true)
                    afterAnimation()
                    dialog.dismiss()
                })
                .create()
                .show()

            // mob sdk 用户隐私
            MobSDK.submitPolicyGrantResult(true, null)
        }
    }

    private fun afterAnimation() {
        startActivityClearTop(MainActivity::class.java, null)
        finish()
    }
}