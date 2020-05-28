package com.account.base.ui.list

import androidx.core.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import butterknife.BindView
import com.blankj.utilcode.util.SizeUtils
import com.account.R
import com.account.base.common.BasePresenter
import com.account.base.ui.BaseFragment
import com.jude.easyrecyclerview.EasyRecyclerView
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.jude.easyrecyclerview.decoration.DividerDecoration
import com.jz.yihua.activity.base.BaseNoJsonListView2
import com.jz.yihua.activity.feature.main.explore.category.footer.EmptyImageFooter
import com.jz.yihua.activity.feature.main.explore.category.footer.EmptyLoadingFooter
import com.jz.yihua.activity.feature.main.explore.category.footer.EmptyRetryFooter
import com.jz.yihua.activity.view.swipe.NoMoreItemView

import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import java.util.*

/**
 * 列表Fragment基类
 * 可下拉刷新上拉加载
 * emptyView 会覆盖 headerView
 * 使用 emptyFooter 代替 accidentViewView
 */
abstract class BaseRefreshListFragment2<T, P : BasePresenter> : BaseFragment<P>(), BaseNoJsonListView2<T>,
    OnRefreshLoadMoreListener {


    @BindView(R.id.parent_container)
    lateinit var parent_container: ViewGroup

    @BindView(R.id.swipe)
    lateinit var swipeLayout: SmartRefreshLayout

    @BindView(R.id.recycler)
    lateinit var easyRecyclerView: EasyRecyclerView
    lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView

    protected lateinit var noMoreItemView: NoMoreItemView
    protected var emptyImageFooter: EmptyImageFooter? = null
    protected var emptyRetryFooter: EmptyRetryFooter? = null
    protected var emptyLoadingFooter: EmptyLoadingFooter? = null


    private var adapter: RecyclerArrayAdapter<T>? = null
    var page = 0
    var pageSize = 10

    override fun getLayoutId() = R.layout.fragment_refresh_list2

    open fun emptyMessage() = getString(R.string.hint_data_no_found)
    open fun emptyImage() = R.drawable.img_data_no_found

    override fun initViews(parent: View) {

        // 刷新和加载
        swipeLayout.setOnRefreshLoadMoreListener(this)
        swipeLayout.setEnableLoadMore(false)
        swipeLayout.setEnableAutoLoadMore(true)

        // header 、 footer 空view (footer 代替 emptyView)
        noMoreItemView = NoMoreItemView()
        emptyImageFooter = EmptyImageFooter(context!!)
        emptyImageFooter?.emptyImageResId = emptyImage()
        emptyImageFooter?.emptyText = emptyMessage()

        emptyLoadingFooter = EmptyLoadingFooter(context!!)
        emptyRetryFooter = EmptyRetryFooter(context!!)
        emptyRetryFooter?.accentListener = object : EmptyRetryFooter.AccentRetryClickListener {
            override fun onRetryClick() {
                onRefresh(swipeLayout)
            }
        }

        // 列表
        adapter = getRecyclerAdapter()
        recyclerView = easyRecyclerView.recyclerView
        easyRecyclerView.setLayoutManager(androidx.recyclerview.widget.LinearLayoutManager(context))
        easyRecyclerView.setAdapterWithProgress(adapter)
        easyRecyclerView.showRecycler()
        getItemDecoration()?.let {
            easyRecyclerView.addItemDecoration(it)
        }
    }


    override fun bindList(list: ArrayList<T>, isFirstPage: Boolean, last: Boolean) {

        if (isFirstPage) {
            if (swipeLayout.isRefreshing) {
                swipeLayout.finishRefresh()
            }
            adapter?.clear()
            // 第一个，空View
            if (adapter?.footerCount!! > 0) {
                adapter?.removeAllFooter()
            }
            if (list.isEmpty()) {
                adapter?.addFooter(emptyImageFooter)
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

        adapter?.addAll(list)

        // 是否可以上拉加载
        swipeLayout.setEnableLoadMore(!last)
        if (last && adapter?.allData!!.size > 0) {
            if (adapter?.footerCount!! > 0) adapter?.removeAllFooter()
            adapter?.addFooter(noMoreItemView)
        }

    }

    override fun loadOnVisible() {
        if (adapter?.footerCount!! > 0) {
            adapter?.removeAllFooter()
        }
        adapter?.addFooter(emptyLoadingFooter)
        onRefresh()
    }


    override fun showError(code: Int, message: String) {
        super.showError(code, message)
        if (swipeLayout.isRefreshing) {
            swipeLayout.finishRefresh(false)
        }
        if (swipeLayout.isLoading) {
            swipeLayout.finishLoadMore(false)
        }
        if (getAdapter()?.allData?.size == 0) {
            getAdapter()?.removeAllFooter()
            getAdapter()?.addFooter(emptyRetryFooter)
        }
    }

    protected fun getAdapter() = adapter

    abstract fun getRecyclerAdapter(): RecyclerArrayAdapter<T>

    open fun getItemDecoration(): androidx.recyclerview.widget.RecyclerView.ItemDecoration? {
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

    open fun onRefresh() {

        page = 0
        presenter.loadList(this, page)
        presenter.loadListWithPageSize(this, page, pageSize)
    }

    open fun onLoadMore() {
        page++
        presenter.loadList(this, page)
        presenter.loadListWithPageSize(this, page, pageSize)
    }


}