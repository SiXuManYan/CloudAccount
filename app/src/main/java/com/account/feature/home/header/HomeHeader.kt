package com.account.feature.home.header

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.account.R
import com.account.R2.id.recyclerview
import com.account.common.CommonUtils
import com.account.entity.home.Banners
import com.account.entity.home.Product
import com.account.extend.SpaceDecoration
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.youth.banner.Banner
import com.youth.banner.loader.ImageLoader
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Created by Wangsw on 2020/5/26 16:46.
 * </br>
 *
 */
class HomeHeader constructor(private var context: Context) : RecyclerArrayAdapter.ItemView {


    /**
     *     轮播图data
     *     setData 后调用
     *    getAdapter()!!.notifyItemChanged(0)
     */
    var bannersData: ArrayList<Banners> = ArrayList()

    /**
     * 资讯 data
     */
    var productData: ArrayList<Product> = ArrayList()

    lateinit var banner: Banner
    lateinit var recyclerView: RecyclerView
    private var staggeredGridDecoration: SpaceDecoration? = null

    private val handler = Handler()
    private var clickAble = true



    override fun onBindView(headerView: View?) {

        if (headerView == null) {
            return
        }

        if (this::banner.isInitialized) {
            setBannerData(bannersData)
        }

        if (this::recyclerView.isInitialized) {
            setHotProductData(productData)
            productData.clear()
        }

    }


    override fun onCreateView(parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.view_home_header, parent, false)
        banner = view.findViewById<Banner>(R.id.banner)
        recyclerView = view.findViewById<RecyclerView>(R.id.product_rv)

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
                                    RoundedCornersTransformation(SizeUtils.dp2px(4f), 0)
                                )
                            )
                        )
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
                if (bannersData.isNotEmpty()) {
                    ToastUtils.showShort("mold" + bannersData[it].mold)
                }
            }

        }

        recyclerView.apply {
            setBackgroundColor(ColorUtils.getColor(R.color.font_white))
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = getRecyclerAdapter()
        }

        staggeredGridDecoration = SpaceDecoration(SizeUtils.dp2px(8f)).apply {
            setPaddingStart(true)// 是否在给第一行的item添加上padding(不包含header).默认true.
            setPaddingEdgeSide(true)// 是否为左右2边添加padding.默认true.
            setSpace(SizeUtils.dp2px(8f))
            recyclerView.addItemDecoration(this)
        }

        return view
    }


    private fun getRecyclerAdapter(): RecyclerArrayAdapter<Product> {

        val adapter = object : RecyclerArrayAdapter<Product>(context) {

            override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<Product> {
                return HotProductHolder(parent)
            }
        }

        adapter.setOnItemClickListener {
            if (!clickAble) {
                return@setOnItemClickListener
            }
            clickAble = false
            handler.postDelayed({
                clickAble = true
            }, 1000)

            // TODO 热门产品点击事件
            val name = adapter.allData.get(it).name
            ToastUtils.showShort("热门产品点击name = :$name")

        }
        return adapter
    }



    private fun setBannerData(bannersData: List<Banners>) {

        if (bannersData.isNullOrEmpty()) {
            banner.visibility = View.GONE
            return
        }
        banner.visibility = View.VISIBLE

    }

    private fun setHotProductData(productData: List<Product>) {
        if (productData.isNullOrEmpty()) {
            recyclerView.visibility = View.GONE
            return
        }
        recyclerView.visibility = View.VISIBLE
        getRecyclerAdapter().apply {
            clear()
            addAll(productData)
        }

    }


}