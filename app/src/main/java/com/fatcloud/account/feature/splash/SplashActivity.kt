package com.fatcloud.account.feature.splash

import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.fatcloud.account.feature.MainActivity
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseActivity
import com.fatcloud.account.common.CommonUtils
import com.blankj.utilcode.util.BarUtils
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
                    afterAnimation()
                }

                override fun onAnimationStart(p0: Animation?) {
                }
            })
        }
        splash_container_ll.startAnimation(alphaAnimation)

        // todo 开启服务
//        DataService.startService(this, Constants.ACTION_SYNC)
//        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION)
//            == PackageManager.PERMISSION_GRANTED) {
//            DataService.startService(this, Constants.ACTION_START_LOCATION)
//        }


    }

    private fun afterAnimation() {

        startActivityClearTop(MainActivity::class.java, null)
        finish()
    }
}