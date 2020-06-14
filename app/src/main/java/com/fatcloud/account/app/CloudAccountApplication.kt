package com.fatcloud.account.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.fatcloud.account.backstage.DataServiceFaker
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.CrashHandler
import com.fatcloud.account.data.CloudDataBase
import com.fatcloud.account.entity.commons.Commons
import com.fatcloud.account.network.ApiService
import com.fatcloud.account.view.dialog.LoadingDialog
import com.fatcloud.account.view.swipe.smart.CommonSmartAnimRefreshHeaderView
import com.fatcloud.account.view.swipe.smart.CommonSmartRefreshFooter
import com.mob.MobSDK
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.smtt.sdk.QbSdk
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/5/22 0022 15:41.
 * </br>
 *
 */
class CloudAccountApplication : DaggerApplication(), HasActivityInjector, Application.ActivityLifecycleCallbacks,
    CloudAccountView {



    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout -> CommonSmartAnimRefreshHeaderView(context) }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> CommonSmartRefreshFooter(context) }

    }

    lateinit var database: CloudDataBase @Inject set
    lateinit var apiService: ApiService @Inject set

    private var activityReferences = 0
    private var isActivityChangingConfigurations = false

    private var lastActivity: WeakReference<Activity>? = null
    private val presenter by lazy { CloudAccountPresenter(this) }


    override fun onCreate() {
        super.onCreate()
        initHandle()
        DataServiceFaker.startService(this, Constants.ACTION_SYNC)
    }

    private fun initHandle() {
        //工具类初始化
        Utils.init(this)
        ToastUtils.setBgColor(Color.argb(0xAE, 0, 0, 0))
        ToastUtils.setMsgColor(Color.WHITE)
        // crash
        RxJavaPlugins.setErrorHandler {
            it.printStackTrace()
        }
        // crash 日志收集
        CrashHandler.instance.init(this)

        // shareSdk
        MobSDK.init(this)

        // initX5WebView()
        registerActivityLifecycleCallbacks(this)
        presenter.getCommonList()
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

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
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


    override fun onActivityDestroyed(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

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

    private var loadingDialog: LoadingDialog? = null


    override fun showLoading() = showLoadingDialog()


    override fun hideLoading() = dismissLoadingDialog()


    private fun showLoadingDialog() {
        if (getLastActivity() == null && loadingDialog != null && loadingDialog!!.isShowing) {
            return
        } else {
            loadingDialog = LoadingDialog.Builder(getLastActivity()).setCancelable(false).create()
            loadingDialog!!.show()
        }

    }


    private fun dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog!!.dismiss()
        }
        loadingDialog = null
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
    fun onForeground() = LogUtils.d("ActivityLife--->", "onForeground")

    /**
     * 应用进入后台状态
     */
    private fun onBackgourd() = Unit

    var commonData: Commons? = null
    override fun receiveCommonData(data: Commons) {
        commonData = data
    }


    /**
     * 获取 token
     * @param objectName 文件路径
     * @param isEncryptFile 是否为加密文件
     */
    fun getOssSecurityToken(isEncryptFile: Boolean, isFaceUp: Boolean, localFilePatch: String,@IdRes fromViewId:Int) {
        presenter.getOssSecurityToken(this, isEncryptFile,isFaceUp, localFilePatch,fromViewId)
    }


}