package com.fatcloud.account.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.view.Gravity
import androidx.annotation.IdRes
import androidx.multidex.MultiDex
import com.baidu.mobstat.StatService
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.fatcloud.account.BuildConfig
import com.fatcloud.account.backstage.DataServiceFaker
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.CrashHandler
import com.fatcloud.account.data.CloudDataBase
import com.fatcloud.account.entity.commons.Commons
import com.fatcloud.account.entity.users.User
import com.fatcloud.account.network.ApiService
import com.fatcloud.account.pushs.NotificationUtil
import com.fatcloud.account.view.dialog.LoadingDialog
import com.fatcloud.account.view.swipe.smart.CommonSmartAnimRefreshHeaderView
import com.fatcloud.account.view.swipe.smart.CommonSmartRefreshFooter
import com.mob.MobSDK
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.smtt.sdk.QbSdk
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.DaggerApplication
import io.reactivex.functions.Consumer
import io.reactivex.plugins.RxJavaPlugins
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/5/22 0022 15:41.
 * </br>
 *
 */
class CloudAccountApplication : DaggerApplication(), HasActivityInjector, Application.ActivityLifecycleCallbacks, CloudAccountView {



    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            CommonSmartAnimRefreshHeaderView(
                context
            )
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            CommonSmartRefreshFooter(
                context
            )
        }

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
        initEvent()
        DataServiceFaker.startService(this, Constants.ACTION_SYNC)
        registerActivityLifecycleCallbacks(this)
        loadCommonData()
        NotificationUtil.initCloudChannel(this)
    }


    private fun initEvent() {
        presenter.subsribeEvent(Consumer {
            when (it.code) {

                // 检查默认数据
                Constants.EVENT_CHECK_APPLICATION_DEFAULT_DATA -> {
                    val businessScope = commonData?.businessScope
                    val accountNatues = commonData?.accountNatues
                    val forms = commonData?.forms
                    val commitmentUrl = commonData?.commitmentUrl

                    if (businessScope.isNullOrEmpty() || accountNatues.isNullOrEmpty() || forms.isNullOrEmpty() || commitmentUrl.isNullOrBlank()) {
                        loadCommonData()
                    }

                }
                else -> {
                }
            }
        })
    }

    fun loadCommonData() {
        presenter.getCommonList()
    }

    private fun initHandle() {

        // 工具类初始化
        Utils.init(this)
        ToastUtils.setGravity(Gravity.CENTER, 0, 0)

        if (BuildConfig.DEBUG || BuildConfig.FLAVOR.equals("dev")) {
            LogUtils.getConfig().isLogSwitch = true
            LogUtils.getConfig().isSingleTagSwitch = true
        } else {
            LogUtils.getConfig().isLogSwitch = false
        }

        // rx crash
        RxJavaPlugins.setErrorHandler {
            it.printStackTrace()
        }

        // crash 日志收集
        CrashHandler.instance.init(this)

        // shareSdk
        MobSDK.init(this)

        // initX5WebView()

        initBaiduOcr()

        initBaiduMtj()

        initBugly()
    }


    /**
     * https://mtj.baidu.com/static/userguide/book/android/adconfig/circle/circle.html
     */
    private fun initBaiduMtj() {

        if (BuildConfig.DEBUG && BuildConfig.FLAVOR.equals("dev")) {

            // 打开调试开关，可以查看logcat日志。版本发布前，为避免影响性能，移除此代码
            // 查看方法：adb logcat -s sdkstat
            StatService.setDebugOn(true)


            // 获取测试设备ID
            val testDeviceId = StatService.getTestDeviceId(this)
            // 日志输出
            Log.d("BaiduMobStat", "Test DeviceId : $testDeviceId")
        }

        // 开启自动埋点统计，为保证所有页面都能准确统计，建议在Application中调用。
        // 第三个参数：autoTrackWebview：
        // 如果设置为true，则自动track所有webview；如果设置为false，则不自动track webview，
        // 如需对webview进行统计，需要对特定webview调用trackWebView() 即可。
        // 重要：如果有对webview设置过webchromeclient，则需要调用trackWebView() 接口将WebChromeClient对象传入，
        // 否则开发者自定义的回调无法收到。
        StatService.autoTrace(this, true, false)
    }


    private fun initBugly() {


        // UserStrategy类作为Bugly的初始化扩展，在这里修改本次初始化Bugly数据的版本、渠道及部分初始化行为
        val strategy = CrashReport.UserStrategy(applicationContext).apply {

            // 设置App版本、渠道、包名
            appChannel = BuildConfig.BUGLY_APP_CHANNNEL
            appVersion = BuildConfig.VERSION_NAME
            appPackageName = BuildConfig.APPLICATION_ID

            // 设置Bugly初始化延迟 (Bugly会在启动10s后联网同步数据。若您有特别需求，可以修改这个时间。)
            appReportDelay = 10000

            // 只在主进程下上报数据：判断是否是主进程（通过进程名是否为包名来判断），并在初始化Bugly时增加一个上报进程的策略配置。
            // 获取当前包名
            val packageName = applicationContext.packageName

            // 获取当前进程名
            val processName = CommonUtils.getProcessName(Process.myPid())

            // 设置是否为上报进程
            isUploadProcess = processName == null || processName == packageName


            // 设置Crash回调
            setCrashHandleCallback(object : CrashReport.CrashHandleCallback() {
                /**
                 * Crash处理.
                 *
                 * @param crashType 错误类型：CRASHTYPE_JAVA，CRASHTYPE_NATIVE，CRASHTYPE_U3D ,CRASHTYPE_ANR
                 * @param errorType 错误的类型名
                 * @param errorMessage 错误的消息
                 * @param errorStack 错误的堆栈
                 * @return 返回额外的自定义信息上报
                 */
                override fun onCrashHandleStart(crashType: Int, errorType: String, errorMessage: String, errorStack: String): MutableMap<String, String> {
                    val map = LinkedHashMap<String, String>()
                    map["crashType"] = crashType.toString()
                    map["errorType"] = errorType
                    map["errorMessage"] = errorMessage
                    map["errorStack"] = errorStack
                    return map
                }
            })

        }

        // 开发环境
        val isDevEnvironment = BuildConfig.DEBUG || BuildConfig.FLAVOR == "dev"

        // 将测试环境下运行的设备，设置成开发设备
        CrashReport.setIsDevelopmentDevice(applicationContext, isDevEnvironment);

        // 设置用户ID
        // 您可能会希望能精确定位到某个用户的异常，我们提供了用户ID记录接口。
        // 例：网游用户登录后，通过该接口记录用户ID，在页面上可以精确定位到每个用户发生Crash的情况
        if (User.isLogon()) {
            val username = User.get().username
            CrashReport.setUserId(username)
            CrashReport.putUserData(applicationContext, "userId", username)
        }


        //        第三个参数为SDK调试模式开关，调试模式的行为特性如下：
        //        输出详细的Bugly SDK的Log；
        //        每一条Crash都会被立即上报；
        //        自定义日志将会在Logcat中输出。
        //        建议在测试阶段建议设置成true，发布时设置为false。
        CrashReport.initCrashReport(applicationContext, BuildConfig.BUGLY_APP_ID, isDevEnvironment, strategy)
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

    /**
     * 百度ocr 证件识别
     */
    private fun initBaiduOcr() {

        OCR.getInstance(this).initAccessToken(object : OnResultListener<AccessToken> {
            override fun onResult(result: AccessToken?) {
                val token: String? = result?.accessToken
                LogUtils.d("ocr 初始化 onResult ", "token = " + token)
            }

            override fun onError(error: OCRError?) {
                LogUtils.d("ocr 初始化 onError ", "message = " + error?.message)
            }

        }, this)


    }


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerAppComponent.builder().application(this).build()


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
    fun onForeground() = Unit

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
     * @param isFaceUp 是否为证件照上传
     * @param localFilePatch 图片所在本地路径
     * @param fromViewId 发起请求的viewId 用于上传成功后，做后续操作
     * @param clx 请求发起位置
     *
     */
    fun getOssSecurityToken(isEncryptFile: Boolean, isFaceUp: Boolean, localFilePatch: String, @IdRes fromViewId: Int, clx: Class<*>) {
        presenter.getOssSecurityToken(
            this,
            isEncryptFile,
            isFaceUp,
            localFilePatch,
            fromViewId,
            clx
        )
    }

    var ossCallBack: OssSignCallBack? = null


    /**
     * 签名 阿里云 private image url
     */
    interface OssSignCallBack {
        fun ossUrlSignEnd(url: String)
    }

    /**
     * oss 图片下载
     */
    interface OssDownloadCallBack {

        /**
         * oss 图片下载成功
         */
        fun ossDownloadSuccess(path: String)
    }


    fun getOssSecurityTokenForSignUrl(objectKey: String, ossCallBack: OssSignCallBack) {

        presenter.getOssSecurityTokenForSignUrl(this, objectKey, ossCallBack)
    }

    fun downLoadOssImage(objectKey: String?) {
        presenter.getOssSecurityTokenForDownload(this, objectKey)
    }


}