package com.fatcloud.account.base.ui.list

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import butterknife.BindView
import com.blankj.utilcode.util.SizeUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.ui.BaseFragment
import com.fatcloud.account.view.swipe.NoMoreItemView
import com.fatcloud.account.view.swipe.footer.EmptyImageFooter
import com.fatcloud.account.view.swipe.footer.EmptyLoadingFooter
import com.fatcloud.account.view.swipe.footer.EmptyRetryFooter
import com.jude.easyrecyclerview.EasyRecyclerView
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.jude.easyrecyclerview.decoration.DividerDecoration
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 列表Fragment基类
 * 可下拉刷新上拉加载
 * emptyView 会覆盖 headerView
 * 使用 emptyFooter 代替 accidentViewView
 */
abstract class BaseRefreshListFragment<T, P : BasePresenter> : BaseFragment<P>(), BaseNoJsonListView2<T>, OnRefreshLoadMoreListener {


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
    protected var disableLoadMoreView = false


    private var mAdapter: RecyclerArrayAdapter<T>? = null

    /**
     * 兼容使用PageNumber方式请求列表
     */
    var page = 0

    /**
     * 请求的分页数量，默认10
     */
    var pageSize = 10

    /**
     * 用于请求下一页的最后一项 item id ,首页可不传
     */
    var lastItemId: String? = null

    override fun getLayoutId() = R.layout.fragment_refresh_list2

    open fun emptyMessage() = ""
    open fun emptyImage() = R.drawable.img_data_no_found

    override fun initViews(parent: View) {

        // 刷新和加载
        swipeLayout.apply {
            setOnRefreshLoadMoreListener(this@BaseRefreshListFragment)
            setEnableLoadMore(false)
            setEnableAutoLoadMore(true)
        }

        // header 、 footer 空view (footer 代替 emptyView)
        noMoreItemView = NoMoreItemView()
        emptyImageFooter = EmptyImageFooter(context!!).apply {
            emptyImageResId = emptyImage()
            emptyText = emptyMessage()
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
        easyRecyclerView.apply {
            setLayoutManager(androidx.recyclerview.widget.LinearLayoutManager(context))
            setAdapterWithProgress(mAdapter)
            showRecycler()
        }

        getItemDecoration()?.let {
            easyRecyclerView.addItemDecoration(it)
        }

    }

    override fun bindList(list: ArrayList<T>, lastItemId: String?) {
        bindList(list, TextUtils.isEmpty(this.lastItemId), list.size < pageSize)
        this.lastItemId = lastItemId
    }

    override fun bindList(list: ArrayList<T>, isFirstPage: Boolean, last: Boolean) {

        if (isFirstPage) {
            if (swipeLayout.isRefreshing) {
                swipeLayout.finishRefresh()
            }
            mAdapter?.clear()
            // 第一个，空View
            if (mAdapter?.footerCount!! > 0) {
                mAdapter?.removeAllFooter()
            }
            if (list.isEmpty()) {
                mAdapter?.addFooter(emptyImageFooter)
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

        mAdapter?.addAll(list)

        // 是否可以上拉加载
        swipeLayout.setEnableLoadMore(!last)

        if (!disableLoadMoreView) {
            if (last && mAdapter?.allData!!.size > 0) {
                if (mAdapter?.footerCount!! > 0) mAdapter?.removeAllFooter()
                mAdapter?.addFooter(noMoreItemView)
            }
        }


    }

    override fun loadOnVisible() {
        if (mAdapter?.footerCount!! > 0) {
            mAdapter?.removeAllFooter()
        }
        mAdapter?.addFooter(emptyLoadingFooter)
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

    protected fun getAdapter() = mAdapter

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
        lastItemId = null
        presenter.loadList(this, page)
        presenter.loadList(this, page, pageSize, lastItemId)
    }

    open fun onLoadMore() {
        page++
        presenter.loadList(this, page)
        presenter.loadList(this, page, pageSize, lastItemId)
    }


}