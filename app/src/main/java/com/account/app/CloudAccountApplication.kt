package com.account.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.LogUtils
import com.account.data.CloudDataBase
import com.account.network.ApiService
import com.tencent.smtt.sdk.QbSdk
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.DaggerApplication
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/5/22 0022 15:41.
 * </br>
 *
 */
class CloudAccountApplication : DaggerApplication(), HasActivityInjector, Application.ActivityLifecycleCallbacks, CloudAccountView {



    init {
//        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout -> CommonSmartAnimRefreshHeaderView(context) }
//        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> CommonSmartRefreshFooter(context) }

    }

    lateinit var database: CloudDataBase @Inject set
    lateinit var apiService: ApiService @Inject set

    private var activityReferences = 0
    private var isActivityChangingConfigurations = false

    private var lastActivity: WeakReference<Activity>? = null
    private val presenter by lazy { CloudAccountPresenter(this) }

    var isMute = true


    override fun onCreate() {
        super.onCreate()
        initX5WebView()

    }


    /**
     * 初始化X5内核
     */
    private fun initX5WebView() {
        QbSdk.initX5Environment(this, object : QbSdk.PreInitCallback {

            override fun onViewInitFinished(p0: Boolean) {
                LogUtils.e("X5_WebView", "加载内核是否成功:$p0")
            }

            override fun onCoreInitFinished() {

            }
        })
    }
    override fun applicationInjector(): AndroidInjector<out DaggerApplication>  =
        DaggerAppComponent.builder().application(this).build()


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStarted(activity: Activity) {
        if (++activityReferences == 1 && !isActivityChangingConfigurations) {
            // App enters foreground
            onForeground()
        }
    }


    override fun onActivityDestroyed(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        TODO("Not yet implemented")
    }

    override fun onActivityStopped(activity: Activity) {
        isActivityChangingConfigurations = activity?.isChangingConfigurations ?: false
        if (--activityReferences == 0 && !isActivityChangingConfigurations) {
            // App enters background
            onBackgourd()
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityResumed(activity: Activity) {
        lastActivity = WeakReference<Activity>(activity)
    }

    override fun showError(code: Int, message: String) {

    }

//    private var loadingDialog: LoadingDialog? = null

    private fun showLoadingDialog() {
//        if (getLastActivity() == null && loadingDialog != null && loadingDialog!!.isShowing) {
//            return
//        } else {
//            loadingDialog = LoadingDialog.Builder(getLastActivity()).setCancelable(false).create()
//            loadingDialog!!.show()
//        }

    }


    private fun dismissLoadingDialog() {
//        if (loadingDialog != null && loadingDialog!!.isShowing) {
//            loadingDialog!!.dismiss()
//        }
//        loadingDialog = null
    }

    override fun showLoading() {
        showLoadingDialog()
    }

    override fun hideLoading() {
        dismissLoadingDialog()
    }



    override fun getLastActivity(): Activity? {
        if (lastActivity != null && lastActivity!!.get() != null) {
            return lastActivity!!.get()
        }
        return null
    }


    /**
     * 应用进入前台状态
     */
    fun onForeground() {

        LogUtils.d("ActivityLife--->", "onForeground")

//        DataService.startService(this, Constants.ACTION_CHECK_ORALTOKEN)
    }

    /**
     * 应用进入后台状态
     */
    private fun onBackgourd() = Unit


}