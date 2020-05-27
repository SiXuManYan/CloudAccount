package com.account.feature.home

import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import butterknife.BindView
import com.account.R
import com.account.base.ui.BaseFragment
import com.account.entity.home.Banners
import com.account.entity.home.News
import com.account.entity.home.Product
import com.account.feature.home.header.HomeHeader
import com.account.feature.home.holder.NewsHolderSingle
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jude.easyrecyclerview.EasyRecyclerView
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.jude.easyrecyclerview.decoration.DividerDecoration
import com.jz.yihua.activity.feature.main.explore.category.footer.EmptyImageFooter
import com.jz.yihua.activity.feature.main.explore.category.footer.EmptyLoadingFooter
import com.jz.yihua.activity.feature.main.explore.category.footer.EmptyRetryFooter
import com.jz.yihua.activity.view.swipe.NoMoreItemView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import java.util.ArrayList

/**
 * Created by Wangsw on 2020/5/25 0025 16:41.
 * </br>
 * 首页 混合信息
 */
open class HomeFragment : BaseFragment<HomePresenter>(), HomeView, OnRefreshLoadMoreListener {

    init {
        needMenuControl = true
    }


    @BindView(R.id.parent_container)
    lateinit var parent_container: FrameLayout

    @BindView(R.id.swipe)
    lateinit var swipeLayout: SmartRefreshLayout

    @BindView(R.id.recycler)
    lateinit var easyRecyclerView: EasyRecyclerView
    lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView


    private val handler = Handler()
    private var clickAble = true
    private var homeHeader: HomeHeader? = null

    private lateinit var noMoreItemView: NoMoreItemView
    private var emptyImageFooter: EmptyImageFooter? = null
    private var emptyRetryFooter: EmptyRetryFooter? = null
    private var emptyLoadingFooter: EmptyLoadingFooter? = null
    private var adapter: RecyclerArrayAdapter<News> = getRecyclerAdapter()
    var page = 0


    override fun getLayoutId() = R.layout.fragment_refresh_list2

    override fun loadOnVisible() {
        if (adapter.footerCount > 0) {
            adapter.removeAllFooter()
        }
        adapter.addFooter(emptyLoadingFooter)
        onRefresh()
    }

    override fun initViews(parent: View) {
        initEvent()
        initRecyclerView()
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
        adapter = getRecyclerAdapter()
        recyclerView = easyRecyclerView.recyclerView
        easyRecyclerView.apply {
            setLayoutManager(androidx.recyclerview.widget.LinearLayoutManager(context))
            setAdapterWithProgress(adapter)
            showRecycler()
        }
        getItemDecoration()?.let {
            easyRecyclerView.addItemDecoration(it)
        }

        //  首页Header
        homeHeader = HomeHeader(context!!)
        adapter.addHeader(homeHeader)
    }


    private fun initEvent() {

    }

    private fun getRecyclerAdapter(): RecyclerArrayAdapter<News> {

        val adapter = object : RecyclerArrayAdapter<News>(context) {

            override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<News> {

                return NewsHolderSingle(parent)
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

            // TODO 资讯点击事件
            val title = adapter.allData[it].title
            ToastUtils.showShort("资讯点击 = title:$title")

        }
        return adapter
    }


    private fun getItemDecoration(): androidx.recyclerview.widget.RecyclerView.ItemDecoration? {
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
            bannersData = banners
            productData = products
            adapter.notifyItemChanged(0)
        }

    }

    override fun bindNewsList(list: ArrayList<News>, isFirstPage: Boolean, isLastPage: Boolean) {

        if (isFirstPage) {
            if (swipeLayout.isRefreshing) {
                swipeLayout.finishRefresh()
            }
            adapter.clear()
            // 第一个，空View
            if (adapter.footerCount > 0) {
                adapter.removeAllFooter()
            }
            if (list.isEmpty()) {
                adapter.addFooter(emptyImageFooter)
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

        adapter.addAll(list)
        swipeLayout.setEnableLoadMore(!isLastPage) // 是否可以上拉加载

        // 增加最后一页ite
        if (isLastPage && adapter.allData!!.size > 0) {
            if (adapter.footerCount > 0) adapter.removeAllFooter()
            adapter.addFooter(noMoreItemView)
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
        if (adapter.allData?.size == 0) {
            adapter.removeAllFooter()
            adapter.addFooter(emptyRetryFooter)
        }
    }


}