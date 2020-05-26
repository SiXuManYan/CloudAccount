package com.account.feature.splash

import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.account.feature.MainActivity
import com.account.R
import com.account.base.ui.BaseActivity
import com.account.common.CommonUtils
import com.account.entity.users.User
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

        CommonUtils.setStatusBarTransparent(this)
        BarUtils.setNavBarVisibility(this, false)

        val alphaAnimation = AlphaAnimation(0f, 1f).apply {
            duration = 3000
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