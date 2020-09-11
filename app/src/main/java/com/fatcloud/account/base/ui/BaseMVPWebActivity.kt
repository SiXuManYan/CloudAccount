package com.fatcloud.account.base.ui

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.blankj.utilcode.util.RegexUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.view.error.AccidentView
import com.fatcloud.account.view.web.JsWebViewX5
import com.google.gson.JsonParser
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.scwang.smart.refresh.layout.listener.ScrollBoundaryDecider
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.export.external.interfaces.WebResourceError
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.activity_web_common.*
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Created by Wangsw on 2019/9/3 16:55.
 * </br>
 * web 页
 */
abstract class BaseMVPWebActivity<P : BasePresenter> : BaseMVPActivity<P>(), OnRefreshListener {


    @BindView(R.id.tv_title)
    lateinit var tv_title: TextView

    @BindView(R.id.iv_back)
    lateinit var iv_back: ImageView

    @BindView(R.id.x5_web)
    lateinit var x5_web: JsWebViewX5

    @BindView(R.id.swipe)
    lateinit var swipeLayout: SmartRefreshLayout

    @BindView(R.id.accident)
    lateinit var accidentView: AccidentView

    protected val jsonParser by lazy { JsonParser() }


    var contentUrl = ""

    var loadLocalHtml: Boolean = false

    /** 标题 */
    var webTitle = ""

    /** 处理返回键 */
    private var backCallJs = false


    private var isError = false

    private var changeTitle = false

    private var refresh = true

    override fun getLayoutId(): Int {
        return R.layout.activity_web_common
    }

    override fun onResume() {
        super.onResume()

    }


    override fun initViews() {

        initExtra()
        initView()
    }


    private fun initExtra() {

        intent.getStringExtra(Constants.PARAM_URL)?.let {
            contentUrl = it
        }

        intent.getStringExtra(Constants.PARAM_TITLE)?.let {
            if (!TextUtils.isEmpty(it)) {
                webTitle = it
            }
        }

        intent.getBooleanExtra(Constants.PARAM_HANDLE_BACK, false).let {
            backCallJs = it
        }

        intent.getBooleanExtra(Constants.PARAM_WEB_CHANGETITLE, false).let {
            changeTitle = it
        }

        intent.getBooleanExtra(Constants.PARAM_WEB_REFRESH, true).let {
            refresh = it
        }
        intent.getBooleanExtra(Constants.PARAM_WEB_LOAD_LOCAL_HTML, false).let {
            loadLocalHtml = it
        }

    }

    private fun initView() {

        tv_title.text = webTitle
        iv_back.setOnClickListener {
            onBackPressed()
        }
        title_rl.setOnClickListener {
            if (CommonUtils.isDoubleClick(it)) {
                x5_web.scrollTo(0, 0)
            }

        }

        if (refresh) {
            swipeLayout.setEnableLoadMore(false)
            swipeLayout.setOnRefreshListener(this)
            swipeLayout.setScrollBoundaryDecider(object : ScrollBoundaryDecider {
                override fun canRefresh(content: View): Boolean {
                    return x5_web.webScrollY <= 0
                }

                override fun canLoadMore(content: View): Boolean {
                    return false
                }
            })
        } else {
            swipeLayout.setEnableLoadMore(false)
            swipeLayout.setEnableRefresh(false)
        }



        accidentView.onRetryClickListener = object : AccidentView.OnRetryClickListener {
            override fun onRetryClick() {
                showLoadingDialog()
                onRefresh()
            }
        }
        x5_web.addJavascriptInterface(this@BaseMVPWebActivity, JsWebViewX5.BRIDGE_NAME)
        val webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                if (!title.isNullOrEmpty() && changeTitle) {
                    tv_title.text = title
                }
            }
        }
        x5_web.webChromeClient = webChromeClient

        x5_web.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(webView: WebView?, url: String?): Boolean {
                webView?.loadUrl(url)
                return true
            }

            override fun onReceivedSslError(webView: WebView?, sslErrorHandler: SslErrorHandler, sslError: SslError?) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError)
                //  忽略SSL证书错误，继续加载页面
                sslErrorHandler.proceed()
            }

            override fun onReceivedError(p0: WebView?, p1: WebResourceRequest?, p2: WebResourceError?) {
                super.onReceivedError(p0, p1, p2)
                isError = true
            }

            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
                if (refresh) {
                    swipeLayout.finishRefresh()
                }
                dismissLoadingDialog()
                if (isError) {
                    accidentView.showRetry()
                    backCallJs = false
                    isError = false
                }
            }
        }

        // 加载Html
        x5_web.settings.defaultTextEncodingName = "utf-8";//文本编码
        x5_web.settings.domStorageEnabled = true //设置DOM存储已启用（注释后文本显示变成js代码）
        x5_web.settings.blockNetworkImage = false //设置DOM存储已启用（注释后文本显示变成js代码）


        //本地HTML里面有跨域的请求 原生webview需要设置之后才能实现跨域请求
        try {
            val clazz: Class<*> = x5_web.getSettings().javaClass
            val method: Method? = clazz.getMethod(
                "setAllowUniversalAccessFromFileURLs", Boolean::class.javaPrimitiveType
            )
            method?.invoke(x5_web.getSettings(), true)
        } catch (e: IllegalArgumentException) {

        } catch (e: NoSuchMethodException) {

        } catch (e: IllegalAccessException) {

        } catch (e: InvocationTargetException) {

        }

        x5_web.clearCache(true)
        showLoadingDialog()

        initLoadUrl()
    }

    fun initLoadUrl() {
        if (!TextUtils.isEmpty(contentUrl)) {
            if (loadLocalHtml) {
                x5_web.loadUrl("file:///android_asset/$contentUrl");
            } else {
                if (RegexUtils.isURL(contentUrl)) {
                    x5_web.loadUrl(contentUrl)

                } else {

                    x5_web.loadDataWithBaseURL(null, contentUrl, "text/html", "utf-8", null)
                }
            }

        }
    }

    override fun onBackPressed() {
        when {
            backCallJs -> x5_web.evaluateJavascript("clickLeft()")
            else -> super.onBackPressed()
        }
    }

    override fun onDestroy() {
        x5_web.destroy()
        super.onDestroy()
    }

    protected fun onRefresh() {
        if (accidentView.visibility == View.VISIBLE) {
            accidentView.hide()
        }
        if (!TextUtils.isEmpty(contentUrl)) {
            x5_web.reload()
        }
    }


    override fun onRefresh(refreshLayout: RefreshLayout) = onRefresh()


}