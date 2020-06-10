package com.fatcloud.account.feature.home.header

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.fatcloud.account.R
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.Html5Url
import com.fatcloud.account.entity.home.Banners
import com.fatcloud.account.entity.product.Product
import com.fatcloud.account.event.RxBus
import com.fatcloud.account.event.entity.SetMainSelectNewsTabEvent
import com.fatcloud.account.extend.RoundTransFormation
import com.fatcloud.account.feature.product.detail.ProductDetailActivity
import com.fatcloud.account.feature.webs.WebCommonActivity
import com.fatcloud.account.view.banner.BannerSupport
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.FlexboxLayout
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader

/**
 * Created by Wangsw on 2020/5/26 16:46.
 * </br>
 *
 */
open class HomeHeader constructor(private var context: Context) : RecyclerArrayAdapter.ItemView {


    /**
     *     轮播图data
     *     setData 后调用
     *    getAdapter()!!.notifyItemChanged(0)
     */
    var mBannersData: ArrayList<Banners> = ArrayList()

    /**
     * 资讯 data
     */
    var mProductData: ArrayList<Product> = ArrayList()

    lateinit var banner: BannerSupport
    lateinit var product_fbl: FlexboxLayout
    lateinit var product_0: ImageView
    lateinit var product_1: ImageView
    lateinit var product_2: ImageView
    lateinit var product_3: ImageView
    lateinit var product_container: LinearLayout


    fun setNewBannerData(newDataList: List<Banners>) {
        if (newDataList.isNullOrEmpty()) {
            return
        }
        mBannersData.clear()
        mBannersData.addAll(newDataList)
    }

    fun setNewProductData(newDataList: List<Product>) {
        if (newDataList.isNullOrEmpty()) {
            return
        }
        mProductData.clear()
        mProductData.addAll(newDataList)
    }


    override fun onBindView(headerView: View?) {

        if (headerView == null) {
            return
        }

        if (this::banner.isInitialized) {
            updateBannerData(mBannersData)
        }

        if (this::product_fbl.isInitialized) {
            updateHotProductData(mProductData)
        }

    }


    override fun onCreateView(parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.view_home_header, parent, false)
        banner = view.findViewById<BannerSupport>(R.id.banner)
        product_fbl = view.findViewById<FlexboxLayout>(R.id.product_fbl)
        product_0 = view.findViewById<ImageView>(R.id.product_0)
        product_1 = view.findViewById<ImageView>(R.id.product_1)
        product_2 = view.findViewById<ImageView>(R.id.product_2)
        product_3 = view.findViewById<ImageView>(R.id.product_3)
        product_container = view.findViewById<LinearLayout>(R.id.product_container)
        view.findViewById<TextView>(R.id.more_product_tv).setOnClickListener {
          RxBus.post(SetMainSelectNewsTabEvent())
        }

        // 宽高比 1.78
        val screenWidth = ScreenUtils.getScreenWidth()

        banner.layoutParams.apply {
            height = (screenWidth / 1.78).toInt()
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

                override fun createImageView(context: Context?): ImageView {

                    return ImageView(context).apply {
                        // banner item 增加间隔
                        //  setPadding(SizeUtils.dp2px(15f), 0, SizeUtils.dp2px(15f), 0)
                        setBackgroundColor(ColorUtils.getColor(R.color.transparent))
                    }
                }
            })

            setOnBannerListener {
                if (CommonUtils.isDoubleClick(banner)) {
                    return@setOnBannerListener
                }


                if (mBannersData.isNotEmpty()) {
                    val banners = mBannersData[it]

                    context.startActivity(
                        Intent(context, WebCommonActivity::class.java)
                            .putExtra(Constants.PARAM_URL, Html5Url.SERVICE_AGREEMENT_URL)
                            .putExtra(Constants.PARAM_TITLE, StringUtils.getString(R.string.privacy_statement))
                            .putExtra(Constants.PARAM_WEB_REFRESH, false)
                    )


                }
            }

        }

        return view
    }


    private fun updateBannerData(bannersData: List<Banners>) {

        if (bannersData.isNullOrEmpty()) {
            return
        }
        banner.apply {
            update(getBannerUrls(bannersData))
            updateBannerStyle(BannerConfig.CIRCLE_INDICATOR)
            visibility = View.VISIBLE
        }

    }

    private fun updateHotProductData(productData: List<Product>) {
        if (productData.isNullOrEmpty()) {
            product_container.visibility = View.GONE
            return
        }
        product_container.visibility = View.VISIBLE
        product_fbl.visibility = View.VISIBLE

        productData.forEachIndexed { index, product ->

            if (index > 3) {
                return@forEachIndexed
            }
            val indexImage = getIndexImage(index)
            indexImage.setOnClickListener {

                context.startActivity(
                    Intent(context, ProductDetailActivity::class.java).putExtra(
                        Constants.PARAM_PRODUCT_ID,
                        product.id
                    )
                )

            }

            Glide.with(context)
                .load(product.imgUrl)
                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 8))))
                .error(R.drawable.ic_error_image_load)
                .into(indexImage)


        }


    }


    private fun getBannerUrls(listBean: List<Banners>): List<String> {
        val urls = java.util.ArrayList<String>()
        for (i in listBean.indices) {
            urls.add(listBean[i].imgUrl)
        }
        return urls
    }


    private fun getIndexImage(index: Int): ImageView {

        return when (index) {
            0 -> product_0
            1 -> product_1
            2 -> product_2
            else -> product_3
        }

    }


}