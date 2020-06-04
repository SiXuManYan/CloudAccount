package com.fatcloud.account.feature.product.header

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.fatcloud.account.R
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.entity.home.Banners
import com.fatcloud.account.extend.RoundTransFormation
import com.fatcloud.account.view.banner.BannerSupport
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader

/**
 * Created by Wangsw on 2020/5/27 18:30.
 * </br>
 *  产品Header
 */
open class ProductHeader constructor(private var context: Context) : RecyclerArrayAdapter.ItemView {


    /**
     *     轮播图data
     *     setData 后调用
     *    getAdapter()!!.notifyItemChanged(0)
     */
    var mBannersData: ArrayList<Banners> = ArrayList()


    lateinit var banner: BannerSupport


    fun setNewBannerData(newDataList: List<Banners>) {
        if (newDataList.isNullOrEmpty()) {
            return
        }
        mBannersData.clear()
        mBannersData.addAll(newDataList)
    }


    override fun onBindView(headerView: View?) {

        if (headerView == null) {
            return
        }

        if (this::banner.isInitialized) {
            updateBannerData(mBannersData)
        }


    }


    override fun onCreateView(parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.view_product_header, parent, false)
        banner = view.findViewById<BannerSupport>(R.id.banner)

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
                        .apply(
                            RequestOptions().transform(
                                MultiTransformation(
                                    CenterCrop(),
                                    RoundTransFormation(context,4)
                                )
                            )
                        )
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

                // todo 轮播图跳转事件
                if (mBannersData.isNotEmpty()) {
                    ToastUtils.showShort("轮播图点击 mold == " + mBannersData[it].mold)
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


    private fun getBannerUrls(listBean: List<Banners>): List<String> {
        val urls = java.util.ArrayList<String>()
        for (i in listBean.indices) {
            urls.add(listBean[i].imgUrl)
        }
        return urls
    }


}