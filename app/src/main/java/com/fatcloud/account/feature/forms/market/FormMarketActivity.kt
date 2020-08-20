package com.fatcloud.account.feature.forms.market

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import butterknife.OnClick
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fatcloud.account.R
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.form.MarketData
import kotlinx.android.synthetic.main.activity_form_market.*
import zhy.com.highlight.HighLight
import zhy.com.highlight.position.OnBottomPosCallback
import zhy.com.highlight.shape.RectLightShape


/**
 * Created by Wangsw on 2020/8/5 0005 13:48.
 * </br>
 * 辽宁市场主体登记全程电子化平台
 *
 */
class FormMarketActivity : BaseMVPActivity<FormMarketPresenter>(), FormMarketView {


    var orderId: String? = ""
    var mUrl: String = getString(R.string.liaoning_market_registration_platform_url)
    private var mHighLight: HighLight? = null


    override fun getLayoutId() = R.layout.activity_form_market

    override fun initViews() {

        setMainTitle(R.string.information)
        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_ID)) {
            finish()
            return
        }
        orderId = intent.extras!!.getString(Constants.PARAM_ORDER_ID)
        presenter.getMarketInfo(this, orderId)


        mHighLight = HighLight(this)
            .autoRemove(true)
            .intercept(true)
            .enableNext()
            .anchor(findViewById<View>(android.R.id.content))
            .setOnLayoutCallback {
                mHighLight?.addHighLight(R.id.register_url_rl, R.layout.view_market_tips_02, OnBottomPosCallback(10f), RectLightShape(0f, 0f, 0f, 0f, 0f))
                mHighLight?.show()
            }
            .setOnShowCallback {

            }

            .setOnRemoveCallback {

            }
            .setClickCallback {
                mHighLight?.let {
                    if (it.hightLightView != null) {
                        it.next()
                    }
                }
            }

    }

    override fun bindPageInfo(it: MarketData) {
        mUrl = it.url
        url_tv.text = it.url
        image_container_ll.removeAllViews()
        it.govcnImgUrls.forEach {
            if (it.isNotBlank()) {
                val image = ImageView(this).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                }
                Glide.with(this).load(it).diskCacheStrategy(DiskCacheStrategy.NONE).into(image)
                image_container_ll.addView(image)
            }
        }

    }

    override fun addMarketSuccess() {
        ToastUtils.showShort("数据提交成功")
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        mHighLight?.remove()
    }

    @OnClick(
        R.id.copy_tv,
        R.id.commit_tv,
        R.id.ic_tips
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.copy_tv -> {
                if (mUrl.isBlank()) {
                    return
                }
                val cmb: ClipboardManager = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cmb.primaryClip = ClipData.newPlainText("辽宁市场主体登记全程电子化平台注册地址", mUrl)
              CommonUtils.openUrlWithNativeWebApp(mUrl,this)
            }
            R.id.commit_tv -> {
                val account = account_et.text.toString().trim()

                if (account.isBlank()) {
                    ToastUtils.showShort("请输入账号")
                    return
                }
                val password = password_et.text.toString().trim()
                if (password.isBlank()) {
                    ToastUtils.showShort("请输入密码")
                    return
                }
                presenter.addMarket(this, orderId, account, password)
            }
            R.id.ic_tips -> {

                mHighLight?.let {
                    if (!it.isShowing) {
                        it.show()
                    }

                }
            }
            else -> {
            }
        }
    }


}