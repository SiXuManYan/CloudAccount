package com.fatcloud.account.feature.product.detail

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.widget.*
import butterknife.OnClick
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.AndroidUtil
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.product.ProductDetail
import com.fatcloud.account.entity.users.User
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.RxBus
import com.fatcloud.account.event.entity.OrderPaySuccessEvent
import com.fatcloud.account.feature.account.login.LoginActivity
import com.fatcloud.account.feature.forms.master.MasterNamingActivity
import com.fatcloud.account.feature.forms.personal.bank.basic.FormPersonalBankBasicActivity
import com.fatcloud.account.feature.message.MessageActivity
import com.fatcloud.account.feature.product.detail.check.ProductCheckFragment
import com.fatcloud.account.feature.product.detail.input.ProductInputFragment
import com.fatcloud.account.feature.product.detail.share.ShareFragment
import com.fatcloud.account.feature.product.detail.sheet.ProductSheetFragment
import com.fatcloud.account.feature.product.detail.spinners.ProductSpinnerFragment
import com.fatcloud.account.feature.webs.WebCommonActivity
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.layout_bottom_action.*

/**
 * Created by Wangsw on 2020/5/28 0028 15:32.
 * </br>
 * 产品详情页
 */
class ProductDetailActivity : BaseMVPActivity<ProductDetailPresenter>(), ProductDetailView {

    private var mData: ProductDetail? = null
    private var mProductId = ""
    private var unReadNumber: Long = 0

    override fun getLayoutId() = R.layout.activity_product_detail


    override fun initViews() {

        initView()
        initExtra()
        initEvent()
    }


    private fun initView() {
        banner.layoutParams.apply {
            height = (ScreenUtils.getScreenWidth() / 1.78).toInt()
        }

        banner.apply {

            setViewPagerIsScroll(true)
            isAutoPlay(true)
            setDelayTime(3000)
            setImageLoader(object : ImageLoader() {
                override fun displayImage(context: Context?, obj: Any?, imageView: ImageView?) {

                    Glide.with(context!!)
                        .load(obj)
                        .apply(
                            RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .dontAnimate()
                        )
                        .error(R.drawable.ic_error_image_load)
                        .into(imageView!!)
                }

                override fun createImageView(context: Context?): ImageView =
                    ImageView(context).apply {
                        setBackgroundColor(ColorUtils.getColor(R.color.transparent))
                    }
            })

        }
        bottom_left_tv.text = "联系客服"
        bottom_right_tv.text = "立即办理"

        good_tab.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                good_image_container_ll.visibility = View.VISIBLE
                good_tab.setTextColor(ColorUtils.getColor(R.color.color_zero_level))
            } else {
                good_image_container_ll.visibility = View.GONE
                good_tab.setTextColor(ColorUtils.getColor(R.color.color_third_level))
            }
        }

        service_tab.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                service_image_container_ll.visibility = View.VISIBLE
                service_tab.setTextColor(ColorUtils.getColor(R.color.color_zero_level))
            } else {
                service_image_container_ll.visibility = View.GONE
                service_tab.setTextColor(ColorUtils.getColor(R.color.color_third_level))
            }

        }

    }

    private fun initExtra() {
        intent.getStringExtra(Constants.PARAM_PRODUCT_ID)?.let {
            mProductId = it
            getData()
        }
    }


    private fun initEvent() {
        presenter.subsribeEventEntity<OrderPaySuccessEvent>(Consumer {
            finish()
        })

        presenter.subsribeEvent(Consumer {
            when (it.code) {
                Constants.EVENT_CLOSE_PAY,
                Constants.EVENT_CLOSE_PAY_UNKNOWN,
                Constants.EVENT_FORM_COMMIT_SUCCESS,
                Constants.EVENT_FORM_CLOSE -> {
                    finish()
                }
                else -> {
                }
            }
        })


    }

    private fun getData() {

        // 检测默认数据

        RxBus.post(Event(Constants.EVENT_CHECK_APPLICATION_DEFAULT_DATA))


        presenter.getDetail(this, mProductId)
        presenter.getNewsUnreadCount(this)
    }


    override fun bindDetailData(data: ProductDetail) {

        mData = data
        title_tv.text = data.name
        content_tv.text = data.introduce
        CommonUtils.setFormatText(
            amount_tv,
            getString(R.string.money_symbol),
            data.money.stripTrailingZeros().toPlainString(),
            12,
            18
        )

        // banner
        val bannerImgUrls = data.bannerImgUrls
        if (bannerImgUrls.isNotEmpty()) {
            banner.apply {
                update(bannerImgUrls)
                updateBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                visibility = View.VISIBLE
            }
        } else {
            banner.visibility = View.GONE
        }

        // image
        good_image_container_ll.removeAllViews()
        service_image_container_ll.removeAllViews()
        public_image_container_ll.removeAllViews()


        // 小人物提示
        data.publicImgUrls.forEachIndexed { index, url ->
            val imageView = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }

            Glide.with(this)
                .load(url)
                .error(R.drawable.ic_error_image_load)
                .into(imageView)

            public_image_container_ll.addView(imageView)

        }

        // 商品详情
        data.detailImgUrls.forEachIndexed { index, url ->

            val imageView = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }

            Glide.with(this)
                .load(url)
                .error(R.drawable.ic_error_image_load)
                .into(imageView)

            good_image_container_ll.addView(imageView)
        }

        // 服务内容
        if (data.serviceImgUrls.isNullOrEmpty()) {
            service_tab.visibility = View.GONE
            service_image_container_ll.visibility = View.GONE
        } else {
            service_tab.visibility = View.VISIBLE

            data.serviceImgUrls.forEachIndexed { index, url ->

                val imageView = ImageView(this).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                }

                Glide.with(this)
                    .load(url)
                    .error(R.drawable.ic_error_image_load)
                    .into(imageView)

                service_image_container_ll.addView(imageView)
            }

        }


        // 服务协议
        protocol.movementMethod = LinkMovementMethod.getInstance()
        val ruleTitle = getString(R.string.register_protocol_title)
        val ruleValue = getString(R.string.register_protocol_product_detail_value)
        protocol.text = SpanUtils()
            .append(ruleTitle)
            .append(ruleValue)
            .setClickSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {

                    var url = ""
                    when (data.mold) {
                        Constants.P1 -> {
                            url = "ying_ye_zhi_zhao_ge_ti.html"
                        }
                        Constants.P2 -> {
                            url = "ying_ye_zhi_zhao_qi_ye.html"
                        }
                        Constants.P3, Constants.P4 -> {
                            url = "shui_wu_deng_ji_dai_li_ji_zhang.html"
                        }
                        else -> {
                            url = "fu_wu_xie_yi.html"
                        }
                    }

                    startActivity(
                        Intent(this@ProductDetailActivity, WebCommonActivity::class.java)
                            .putExtra(Constants.PARAM_URL, url)
                            .putExtra(Constants.PARAM_TITLE, "服务协议")
                            .putExtra(Constants.PARAM_WEB_REFRESH, false)
                            .putExtra(Constants.PARAM_WEB_LOAD_LOCAL_HTML, true)
                    )
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = ColorUtils.getColor(R.color.color_red_foreground)
                    ds.isUnderlineText = false
                }
            }).create()

    }

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()


    @OnClick(
        R.id.back_iv,
        R.id.title_rl,
        R.id.bottom_left_tv,
        R.id.bottom_right_tv,
        R.id.share_iv
    )
    fun click(view: View) {
        when (view.id) {
            R.id.back_iv -> onBackPressed()
            R.id.title_rl -> {
                if (AndroidUtil.isDoubleClick(view)) {
                    scroll_nsv.smoothScrollTo(0, 0)
                }
            }

            R.id.bottom_left_tv -> {
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Constants.CONSUMER_HOT_LINE)))
            }
            R.id.bottom_right_tv -> {

                mData?.let {
                    handleNext(it)
                }
            }
            R.id.share_iv -> {
                val showTipPopupWindow = showTipPopupWindow()
            }

            else -> {
            }
        }

    }

    private fun handleNext(it: ProductDetail) {
        if (!User.isLogon()) {
            startActivity(LoginActivity::class.java)
            return
        }
        if (!protocol.isChecked) {
            ToastUtils.showShort("请先同意服务协议")
            return
        }
        when (it.mold) {
            Constants.P1,
            Constants.P4,
            Constants.P6 -> {
                // P1个体户营业执照 ，P4 个体户税务登记
                ProductSheetFragment.newInstance(it).apply {
                    show(supportFragmentManager, this.tag)
                }

            }
            Constants.P2,
            Constants.P3,
            Constants.P9,
            Constants.P10 -> {
                ProductSpinnerFragment.newInstance(it).apply {
                    show(supportFragmentManager, this.tag)
                }
            }

            Constants.P5 -> {
                ProductCheckFragment.newInstance(it).apply {
                    show(supportFragmentManager, this.tag)
                }
            }
            Constants.P7 -> {
                val prices = it.prices
                if (prices.isNotEmpty()) {
                    startActivity(
                        Intent(this, MasterNamingActivity::class.java)
                            .putExtra(Constants.PARAM_PRODUCT_ID, mProductId)
                            .putExtra(Constants.PARAM_FINAL_MONEY, mData!!.money.stripTrailingZeros()?.toPlainString())
                            .putExtra(Constants.PARAM_PRODUCT_PRICE_ID, prices[0].id)
                    )
                }
            }
            Constants.P8 -> {
                val prices = it.prices
                if (prices.isNotEmpty()) {
                    startActivity(
                        Intent(this, FormPersonalBankBasicActivity::class.java)
                            .putExtra(Constants.PARAM_PRODUCT_ID, mProductId)
                            .putExtra(Constants.PARAM_FINAL_MONEY, mData!!.money.stripTrailingZeros()?.toPlainString())
                            .putExtra(Constants.PARAM_PRODUCT_PRICE_ID, prices[0].id)
                            .putExtra(Constants.PARAM_MOLD, Constants.P8)
                    )
                }
            }
            Constants.P11 -> {
                ProductInputFragment.newInstance(it).apply {
                    show(supportFragmentManager, this.tag)
                }
            }
            else -> {
            }
        }

    }


    fun showTipPopupWindow(): PopupWindow? {

        val contentView = LayoutInflater.from(context).inflate(R.layout.layout_view_pop, null)
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        val message_unread_fl = contentView.findViewById<FrameLayout>(R.id.message_unread_fl)
        val message_unread_number_tv = contentView.findViewById<TextView>(R.id.message_unread_number_tv)

        val message_ll = contentView.findViewById<LinearLayout>(R.id.message_l)
        val share_ll = contentView.findViewById<LinearLayout>(R.id.shar_l)


        if (unReadNumber > 0L) {
            message_unread_fl.visibility = View.VISIBLE
            message_unread_number_tv.setText(unReadNumber.toString())
        } else {
            message_unread_fl.visibility = View.GONE
        }

        message_ll.setOnClickListener {
            startActivity(MessageActivity::class.java)
        }

        share_ll.setOnClickListener {
            ShareFragment().apply {
                show(supportFragmentManager, this.tag)
            }
        }

        val popupWindow = PopupWindow(contentView, contentView.measuredWidth, contentView.measuredHeight, false).apply {
            isOutsideTouchable = true
            inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
            softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
            setBackgroundDrawable(ColorDrawable())
            showAsDropDown(share_iv)
        }
        return popupWindow
    }


    override fun updateMessageUnReadNumber(messageUnReadNumber: Long) {
        unReadNumber = messageUnReadNumber
    }

}
