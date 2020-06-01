package com.account.feature.home

import android.content.Intent
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.OnClick
import com.account.R
import com.account.base.ui.BaseFragment
import com.account.common.AndroidUtil
import com.account.common.Constants
import com.account.entity.home.Banners
import com.account.entity.news.News
import com.account.entity.product.Product
import com.account.event.entity.TabRefreshEvent
import com.account.feature.home.adapters.NewsChildAdapter
import com.account.feature.home.header.HomeHeader
import com.account.feature.news.detail.NewsDetailActivity
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jude.easyrecyclerview.EasyRecyclerView
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.jude.easyrecyclerview.decoration.DividerDecoration
import com.account.view.swipe.footer.EmptyImageFooter
import com.account.view.swipe.footer.EmptyLoadingFooter
import com.account.view.swipe.footer.EmptyRetryFooter
import com.account.view.swipe.NoMoreItemView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.functions.Consumer
import java.util.*

/**
 * Created by Wangsw on 2020/5/25 0025 16:41.
 * </br>
 * 首页
 */
open class HomeFragment : BaseFragment<HomePresenter>(), HomeView, OnRefreshLoadMoreListener {

    init {
        needMenuControl = true
    }

    @BindView(R.id.swipe)
    lateinit var swipeLayout: SmartRefreshLayout

    @BindView(R.id.recycler)
    lateinit var easyRecyclerView: EasyRecyclerView
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.share_iv)
    lateinit var share_iv: ImageView

    private val handler = Handler()
    private var clickAble = true
    private var homeHeader: HomeHeader? = null

    private lateinit var noMoreItemView: NoMoreItemView
    private var emptyImageFooter: EmptyImageFooter? = null
    private var emptyRetryFooter: EmptyRetryFooter? = null
    private var emptyLoadingFooter: EmptyLoadingFooter? = null
    private var mAdapter: RecyclerArrayAdapter<News> = getRecyclerAdapter()
    var page = 0


    override fun getLayoutId() = R.layout.fragment_home

    override fun loadOnVisible() {
        if (mAdapter.footerCount > 0) {
            mAdapter.removeAllFooter()
        }
        mAdapter.addFooter(emptyLoadingFooter)
        onRefresh()
    }

    override fun initViews(parent: View) {
        initEvent()
        initRecyclerView()
    }

    private fun initEvent() {
        presenter.subsribeEventEntity<TabRefreshEvent>(Consumer {
            if (it.clx == HomeFragment::class.java) {
                if (isViewVisible) {
                    recyclerView.smoothScrollToPosition(0)
                    swipeLayout.postDelayed({
                        swipeLayout.autoRefresh()
                    }, 200)
                }
            }

        })
    }

    private fun initRecyclerView() {

        // 刷新和加载
        swipeLayout.setOnRefreshLoadMoreListener(this)
        swipeLayout.setEnableLoadMore(false)
        swipeLayout.setEnableAutoLoadMore(true)

        // header 、 footer 空view (footer 代替 emptyView)
        noMoreItemView = NoMoreItemView()
        emptyImageFooter = EmptyImageFooter(context!!).apply {
            emptyImageResId = R.drawable.ic_empty
            emptyText = getString(R.string.empty_text)
        }

        emptyLoadingFooter = EmptyLoadingFooter(context!!)
        emptyRetryFooter = EmptyRetryFooter(context!!).apply {
            accentListener = object : EmptyRetryFooter.AccentRetryClickListener {
                override fun onRetryClick() {
                    onRefresh(swipeLayout)
                }
            }
        }

        // 列表
        mAdapter = getRecyclerAdapter()
        recyclerView = easyRecyclerView.recyclerView
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        share_iv.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fab_alpha_more_transparent))
                    }
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        share_iv.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fab_alpha_more_saturation))
                    }
                    else -> {
                    }
                }


            }
        })
        easyRecyclerView.apply {
            setLayoutManager(androidx.recyclerview.widget.LinearLayoutManager(context))
            setAdapterWithProgress(mAdapter)
            showRecycler()
        }
        getItemDecoration()?.let {
            easyRecyclerView.addItemDecoration(it)
        }

        //  首页Header
        homeHeader = HomeHeader(context!!)
        mAdapter.addHeader(homeHeader)
    }


    private fun getRecyclerAdapter(): RecyclerArrayAdapter<News> {

        val adapter = NewsChildAdapter(context)

        adapter.setOnItemClickListener {
            if (!clickAble) {
                return@setOnItemClickListener
            }
            clickAble = false
            handler.postDelayed({
                clickAble = true
            }, 1000)

            val news = adapter.allData[it]
            startActivity(Intent(context, NewsDetailActivity::class.java).putExtra(Constants.PARAM_ID,news.id))

        }
        return adapter
    }


    private fun getItemDecoration(): RecyclerView.ItemDecoration? {
        val itemDecoration = DividerDecoration(
            ContextCompat.getColor(context!!, R.color.colorLine),
            SizeUtils.dp2px(0.5f),
            SizeUtils.dp2px(15f),
            0
        )
        itemDecoration.setDrawHeaderFooter(false)
        return itemDecoration
    }

    override fun onRefresh(refreshLayout: RefreshLayout) = onRefresh()

    override fun onLoadMore(refreshLayout: RefreshLayout) = onLoadMore()

    private fun onRefresh() {
        page = 0
        presenter.loadHomeList(this, page)
    }

    private fun onLoadMore() {
        page++
        presenter.loadHomeList(this, page)
    }

    override fun setHeaderDada(banners: ArrayList<Banners>, products: ArrayList<Product>) {

        homeHeader?.apply {
            setNewBannerData(banners)
            setNewProductData(products)
            mAdapter.notifyItemChanged(0)
        }

    }

    override fun bindNewsList(list: ArrayList<News>, isFirstPage: Boolean, isLastPage: Boolean) {

        if (isFirstPage) {
            if (swipeLayout.isRefreshing) {
                swipeLayout.finishRefresh()
            }
            mAdapter.clear()
            // 第一个，空View
            if (mAdapter.footerCount > 0) {
                mAdapter.removeAllFooter()
            }
            if (list.isEmpty()) {
                mAdapter.addFooter(emptyImageFooter)
            }
        } else {
            if (swipeLayout.isLoading) {
                if (list.isEmpty()) {
                    // 加载完毕
                    swipeLayout.finishLoadMoreWithNoMoreData()
                } else {
                    // 完成加载
                    swipeLayout.finishLoadMore()
                }
            }
        }

        mAdapter.addAll(list)
        swipeLayout.setEnableLoadMore(!isLastPage) // 是否可以上拉加载

        // 增加最后一页ite
        if (isLastPage && mAdapter.allData!!.size > 0) {
            if (mAdapter.footerCount > 0) mAdapter.removeAllFooter()
            mAdapter.addFooter(noMoreItemView)
        }
    }

    override fun showError(code: Int, message: String) {
        super.showError(code, message)
        if (swipeLayout.isRefreshing) {
            swipeLayout.finishRefresh(false)
        }
        if (swipeLayout.isLoading) {
            swipeLayout.finishLoadMore(false)
        }
        if (mAdapter.allData?.size == 0) {
            mAdapter.removeAllFooter()
            mAdapter.addFooter(emptyRetryFooter)
        }
    }

    @OnClick(R.id.share_iv)
    fun click(view: View) {
        if (AndroidUtil.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.share_iv -> {
                ToastUtils.showShort("分享功能开发中")
            }
            else -> {
            }
        }

    }


}