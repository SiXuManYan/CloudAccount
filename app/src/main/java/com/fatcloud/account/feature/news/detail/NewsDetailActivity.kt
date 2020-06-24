package com.fatcloud.account.feature.news.detail

import android.text.TextUtils
import android.view.View
import butterknife.OnClick
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.RegexUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.TimeUtil
import com.fatcloud.account.entity.news.NewDetail
import com.fatcloud.account.entity.users.User
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.RxBus
import com.fatcloud.account.feature.account.login.LoginActivity
import com.fatcloud.account.view.web.JsWebViewX5
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.export.external.interfaces.WebResourceError
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_detail_news.*
import java.lang.reflect.Method


/**
 * Created by Wangsw on 2020/5/30 0030 16:30.
 * </br>
 * 资讯详情
 */
class NewsDetailActivity : BaseMVPActivity<NewsDetailPresenter>(), NewsDetailView {

    private var newsId: String? = ""
    private var liked = false
    private var likeCount = 0
    private var isError = false
    private var contentUrl = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_detail_news
    }


    override fun initViews() {
        initView()
        newsId = intent.getStringExtra(Constants.PARAM_ID)
        presenter.getNewsDetail(this, newsId)
    }

    private fun initView() {

        x5_web.addJavascriptInterface(this, JsWebViewX5.BRIDGE_NAME)

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

                dismissLoadingDialog()
                if (isError) {
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
        } catch (e: Exception) {

        }
        x5_web.clearCache(true)
        showLoadingDialog()

    }


    fun initLoadUrl() {
        if (!TextUtils.isEmpty(contentUrl)) {
            if (RegexUtils.isURL(contentUrl)) {
                x5_web.loadUrl(contentUrl)
            } else {

                x5_web.loadDataWithBaseURL(null, contentUrl, "text/html", "utf-8", null)
            }

        }
    }

    override fun bindDetailData(it: NewDetail) {
        contentUrl = it.content
        initLoadUrl()
        title_tv.text = it.title
        author_name_tv.text = it.editor
        date_tv.text = TimeUtil.getFormatTimeYMD(it.createDt)
        page_views_tv.text = it.readCount.toString()
        likeCount = it.likeCount
        like_tv.text = likeCount.toString()
        collect_tv.text = it.moldText

        // 更新页面浏览量
        RxBus.post(Event(Constants.EVENT_ADD_NEWS_PAGE_VIEWS, newsId))
    }

    override fun newsLikeStatus(status: Int) {

        like_iv.setImageResource(
            if (status == 1) {
                liked = true
                R.drawable.ic_like_red
            } else {
                liked = false
                R.drawable.ic_like_gray
            }
        )

    }

    override fun handleLikeSuccess() {
        if (liked) {
            like_iv.setImageResource(R.drawable.ic_like_gray)
            likeCount--
            if (likeCount < 0) {
                likeCount = 0
            }
            like_tv.text = likeCount.toString()
            like_tv.setTextColor(ColorUtils.getColor(R.color.color_third_level))
            liked = false
        } else {
            like_iv.setImageResource(R.drawable.ic_like_red)
            likeCount++
            like_tv.text = likeCount.toString()
            like_tv.setTextColor(ColorUtils.getColor(R.color.color_app_red))
            liked = true
        }
    }

    @OnClick(
        R.id.like_ll,
        R.id.iv_back
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.like_ll -> {
                if (!User.isLogon()) {
                    startActivity(LoginActivity::class.java)
                    return
                }
                presenter.doLikeAction(this, newsId, liked)
            }
            R.id.iv_back -> onBackPressed()
            else -> {
            }
        }
    }

}