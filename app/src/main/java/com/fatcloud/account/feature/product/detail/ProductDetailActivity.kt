package com.fatcloud.account.feature.product.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import butterknife.OnClick
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.AndroidUtil
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.product.ProductDetail
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.fatcloud.account.feature.product.detail.sheet.ProductSheetFragment
import com.fatcloud.account.feature.product.detail.spinners.ProductSpinnerFragment
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.activity_product_detail.*

/**
 * Created by Wangsw on 2020/5/28 0028 15:32.
 * </br>
 * 产品详情页
 */
class ProductDetailActivity : BaseMVPActivity<ProductDetailPresenter>(), ProductDetailView {

    private var mData: ProductDetail? = null
    private var productId = "0"


    override fun getLayoutId() = R.layout.activity_product_detail


    override fun initViews() {

        initView()
        initExtra()

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
                        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).dontAnimate())
                        .error(R.drawable.ic_error_image_load)
                        .into(imageView!!)
                }

                override fun createImageView(context: Context?): ImageView = ImageView(context).apply {
                    setBackgroundColor(ColorUtils.getColor(R.color.transparent))
                }
            })

        }

    }

    private fun initExtra() {

        productId = intent.getStringExtra(Constants.PARAM_PRODUCT_ID)
        presenter.getDetail(this, productId)
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
        image_container_ll.removeAllViews()


        data.publicImgUrls.forEachIndexed { index, url ->

            val imageView = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }

            Glide.with(this)
                .load(url)
                .error(R.drawable.ic_error_image_load)
                .into(imageView)

            image_container_ll.addView(imageView)


        }

        data.detailImgUrls.forEachIndexed { index, url ->

            val imageView = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }

            Glide.with(this)
                .load(url)
                .error(R.drawable.ic_error_image_load)
                .into(imageView)

            image_container_ll.addView(imageView)


        }

    }

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()


    @OnClick(
        R.id.back_iv,
        R.id.title_rl,
        R.id.share_iv,
        R.id.customer_service_tv,
        R.id.transact_tv

    )
    fun click(view: View) {
        when (view.id) {
            R.id.back_iv -> onBackPressed()
            R.id.title_rl -> {

                if (AndroidUtil.isDoubleClick(view)) {
                    scroll_nsv.smoothScrollTo(0, 0)
                }
            }
            R.id.share_iv -> {
                ToastUtils.showShort("分享功能开发中")
            }
            R.id.customer_service_tv -> {
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Constants.CONSUMER_HOT_LINE)))
            }
            R.id.transact_tv -> {
                if (!protocol.isChecked) {
                    ToastUtils.showShort("请同意用户服务协议")
                }
                mData?.let {
                    when (it.mold) {
                        Constants.P1 -> {
                            licenseHandleP1(it)
                        }
                        Constants.P2 -> {
                            enterpriseHandleP2(it)

                        }
                        Constants.P3 -> {

                        }
                        Constants.P4 -> {


                        }
                        else -> {
                        }
                    }

                }
            }
            else -> {
            }
        }

    }



    /**
     * 个体户营业执照办理
     */
    private fun licenseHandleP1(it: ProductDetail) {

        ProductSheetFragment.newInstance(it).apply {
            show(supportFragmentManager, this.tag)
        }

    }

    private fun enterpriseHandleP2(it: ProductDetail) {
        ProductSpinnerFragment.newInstance(it).apply {
            show(supportFragmentManager, this.tag)
        }
    }


}
