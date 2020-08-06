package com.fatcloud.account.feature.forms.market

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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


/**
 * Created by Wangsw on 2020/8/5 0005 13:48.
 * </br>
 * 辽宁市场主体登记全程电子化平台
 */
class FormMarketActivity : BaseMVPActivity<FormMarketPresenter>(), FormMarketView {


    var orderId: String? = ""
    var mUrl: String = ""

    override fun getLayoutId() = R.layout.activity_form_market

    override fun initViews() {
        setMainTitle(R.string.information)
        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_ID)) {
            finish()
            return
        }
        orderId = intent.extras!!.getString(Constants.PARAM_ORDER_ID)
        presenter.getMarketInfo(this, orderId)

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

    @OnClick(
        R.id.copy_tv,
        R.id.commit_tv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.copy_tv -> {
                if (mUrl.isNotBlank()) {
                    val cmb: ClipboardManager = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cmb.primaryClip = ClipData.newPlainText(null, "")
                    ToastUtils.showShort("复制成功")
                }
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
            else -> {
            }
        }
    }


}