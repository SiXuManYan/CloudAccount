package com.account.base.ui.list

import androidx.core.content.ContextCompat
import android.view.View
import butterknife.BindView
import com.blankj.utilcode.util.SizeUtils
import com.account.R
import com.account.base.common.BasePresenter
import com.account.base.ui.BaseFragment
import com.account.view.error.AccidentView
import com.account.view.swipe.AnimLoadMoreFooterView
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.jude.easyrecyclerview.decoration.DividerDecoration
import com.jz.yihua.activity.base.BaseNoJsonListView
import com.jz.yihua.activity.view.swipe.NoMoreItemView

import java.util.*

/**
 * 列表Fragment基类
 * 上拉加载
 *
 */
abstract class BaseNoRefreshListFragment<T, P : BasePresenter> : BaseFragment<P>(),
    BaseNoJsonListView<T> {

    @BindView(R.id.recycler)
    lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    @BindView(R.id.accident)
    lateinit var accidentView: AccidentView

    private var adapter: RecyclerArrayAdapter<T>? = null
    private lateinit var noMoreItemView: NoMoreItemView
    override fun getLayoutId() = R.layout.fragment_no_refresh_list
    var showNoMore = false

    override fun initViews(parent: View) {
        if (showNoMore) {
            noMoreItemView = NoMoreItemView()
        }
//        (accidentView.parent as ViewGroup).removeView(accidentView)

        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        //设置分割线
        getItemDecoration()?.let {
            recyclerView.addItemDecoration(it)
        }
        adapter = getRecyclerAdapter()
        recyclerView.adapter = adapter
        accidentView.onRetryClickListener = object : AccidentView.OnRetryClickListener {
            override fun onRetryClick() {
                onRefresh()
            }
        }

        getAdapter()?.setError(R.layout.view_rv_error, object : RecyclerArrayAdapter.OnErrorListener {
            override fun onErrorShow() {
            }

            override fun onErrorClick() {
                getAdapter()?.resumeMore()
            }

        })


        val animLoadMoreFooterView = AnimLoadMoreFooterView(context)
        animLoadMoreFooterView.startAnimator()

        getAdapter()?.setMore(animLoadMoreFooterView, object : RecyclerArrayAdapter.OnMoreListener {

            override fun onMoreShow() = onLoadMore()

            override fun onMoreClick() = Unit
        })

//        showEmptyView()
        accidentView.showLoading()
    }


    open fun emptyMessage() = getString(R.string.hint_data_no_found)
    open fun emptyImage() = R.drawable.img_data_no_found


    override fun bindList(list: ArrayList<T>, isFirstPage: Boolean, last: Boolean) {
        if (isFirstPage) {
            getAdapter()?.clear()
            if (list.isEmpty()) {
                showEmptyView()
            } else {
                if (accidentView.visibility == View.VISIBLE) {
                    accidentView.hide()
                }
            }
        }
        getAdapter()?.addAll(list)

        if (last) {
            getAdapter()?.stopMore()
        }
    }

    override fun loadOnVisible() {

        onRefresh()
    }



    fun showEmptyView() {
        accidentView.showEmpty(emptyMessage(), emptyImage())
    }

    fun showLoading() {
        accidentView.showLoading()
    }

    override fun showError(code: Int, message: String) {
        super.showError(code, message)
        adapter?.let {
            if (it.allData.isNullOrEmpty()) {
                accidentView.showRetry()
            } else {
                it.pauseMore()
            }
        }

    }

    protected fun getAdapter() = adapter

    abstract fun getRecyclerAdapter(): RecyclerArrayAdapter<T>

    open fun getItemDecoration(): androidx.recyclerview.widget.RecyclerView.ItemDecoration? {
        val itemDecoration = DividerDecoration(ContextCompat.getColor(context!!, R.color.colorLine), SizeUtils.dp2px(0.5f), SizeUtils.dp2px(15f), 0)
        itemDecoration.setDrawHeaderFooter(false)
        return itemDecoration
    }
}