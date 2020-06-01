package com.account.base.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.account.R
import com.account.base.common.BasePresenter
import com.account.common.*
import com.account.view.error.AccidentView
import com.google.gson.JsonParser
import com.account.view.web.JsWebViewX5
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
        x5_web.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                if (!title.isNullOrEmpty() && changeTitle) {
                    tv_title.text = title
                }
            }
        }
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
        x5_web.clearCache(true)
        showLoadingDialog()

        initLoadUrl()
    }

    fun initLoadUrl() {
        if (!TextUtils.isEmpty(contentUrl)) {
            if (RegexUtils.isURL(contentUrl)) {
                x5_web.loadUrl(contentUrl)
            } else {
//                var body = Common.WEB_STYLE + contentUrl
//                body += "<div style='margin-bottom: 80px;'/>"
                x5_web.loadDataWithBaseURL(null, contentUrl, "text/html", "utf-8", null)
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


    /**
     * 打开新网页
     * pageUrl
     * pageTitle
     */
    @JavascriptInterface
    open fun openNewWebPage(json: String?) {
        if (AndroidUtil.isDoubleClick(iv_back)) {
            return
        }
        if (TextUtils.isEmpty(json)) {
            return
        }
        val jsonStr = jsonParser.parse(json).asJsonObject
        val pageUrl = if (jsonStr.has("pageUrl")) jsonStr.get("pageUrl").asString else ""
        val pageTitle = if (jsonStr.has("pageTitle")) jsonStr.get("pageTitle").asString else ""
        if (jsonStr.has("closeCurrentPage") && jsonStr.get("closeCurrentPage").asBoolean) {
            finish()
        }
        if (!TextUtils.isEmpty(pageUrl)) {


        }


    }


    /**
     * 分享微信小程序至好友
     */
    @JavascriptInterface
    fun shareToWeChatFriend(json: String?) {

        LogUtils.d(json)
        if (TextUtils.isEmpty(json)) {
            return
        }
        val jsonStr = jsonParser.parse(json).asJsonObject
        val sharePath = if (jsonStr.has("sharePath")) jsonStr.get("sharePath").asString else ""
        val shareTitle = if (jsonStr.has("shareTitle")) jsonStr.get("shareTitle").asString else ""
        val shareImageUrl = if (jsonStr.has("shareImageUrl")) jsonStr.get("shareImageUrl").asString else ""
        val shareUrl = if (jsonStr.has("shareUrl")) jsonStr.get("shareUrl").asString else ""

        ShareUtil.shareMiniWechat(shareTitle, shareUrl, shareImageUrl, "", null, sharePath, object :
            PlatformActionListener {

            override fun onCancel(p0: Platform?, p1: Int) {
                ToastUtils.showShort(R.string.share_cancle)
            }

            override fun onError(p0: Platform?, p1: Int, p2: Throwable?) {
                ToastUtils.showShort(R.string.share_fail)
            }

            override fun onComplete(p0: Platform?, p1: Int, p2: HashMap<String, Any>?) {
                ToastUtils.showShort(R.string.share_success)
            }

        })

    }

    /**
     * 关闭原生页面
     */
    @JavascriptInterface
    fun closePage(json: String?) {
        finish()
    }

    /**
     * 发送event
     */
    @JavascriptInterface
    fun addEventLog(json: String) {
    }


    /**
     * 分享内容至朋友圈
     */
    @JavascriptInterface
    open fun shareToWeChatCircle(json: String?) {
        LogUtils.d(json)
        if (json.isNullOrEmpty()) return
        showLoadingDialog()
        val jsonStr = jsonParser.parse(json).asJsonObject
        val photoUrl = if (jsonStr.has("photoUrl")) jsonStr.get("photoUrl") else ""
        Glide.with(this).asBitmap().load(photoUrl).apply(RequestOptions.overrideOf(300, 300))
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    shareMoment("店铺提名", resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    shareMoment("店铺提名", null)
                }
            })
    }

    private fun shareMoment(title: String, resource: Bitmap?) {
        dismissLoadingDialog()
        ShareUtil.shareImageMoment(title, if (resource == null) Common.LOGO_URL else null, null, resource,
            object : PlatformActionListener {
                override fun onComplete(p0: Platform?, p1: Int, p2: java.util.HashMap<String, Any>?) {
                    ToastUtils.showShort(R.string.share_success)
                }

                override fun onCancel(p0: Platform?, p1: Int) {
                    ToastUtils.showShort(R.string.share_cancle)
                }

                override fun onError(p0: Platform?, p1: Int, p2: Throwable?) {
                    ToastUtils.showShort(R.string.share_fail)
                }
            })
    }

    override fun onRefresh(refreshLayout: RefreshLayout) = onRefresh()

    /**
     * 分享文本至微信好友
     * shareTitle
     * shareText
     *
     */
    @JavascriptInterface
    open fun shareTextWechat(json: String) {
        if (TextUtils.isEmpty(json)) {
            return
        }

        val jsonStr = jsonParser.parse(json).asJsonObject
        val shareTitle = if (jsonStr.has("shareTitle")) jsonStr.get("shareTitle").asString else ""
        val shareText = if (jsonStr.has("shareText")) jsonStr.get("shareText").asString else ""
        ShareUtil.shareTextWechat(shareTitle, shareText, object : PlatformActionListener {

            override fun onCancel(p0: Platform?, p1: Int) = Unit

            override fun onError(p0: Platform?, p1: Int, p2: Throwable?) = Unit

            override fun onComplete(p0: Platform?, p1: Int, p2: HashMap<String, Any>?) = Unit
        })
    }


}