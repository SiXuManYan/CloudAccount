package com.fatcloud.account.feature.news.child

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.list.BaseRefreshListFragment2
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.news.News
import com.fatcloud.account.event.entity.NewsProductScrollToTopEvent
import com.fatcloud.account.event.entity.TabRefreshEvent
import com.fatcloud.account.feature.home.adapters.NewsChildAdapter
import com.fatcloud.account.feature.news.detail.NewsDetailActivity
import com.blankj.utilcode.util.SizeUtils
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.jude.easyrecyclerview.decoration.DividerDecoration
import io.reactivex.functions.Consumer

/**
 * Created by Wangsw on 2020/5/29  16:56.
 * </br>
 * 资讯子列表
 * 请求回来的list size < pageSize 则为最后一页
 */
class NewsChildFragment : BaseRefreshListFragment2<News, NewsChildPresenter>(), NewsChildView {


    companion object {

        /**
         * @param categoryValue tab类别，全部时传空
         */
        fun newInstance(categoryValue: String?): NewsChildFragment {
            val fragment = NewsChildFragment()
            val args = Bundle()
            args.putString(Constants.PARAM_TYPE, categoryValue)
            fragment.arguments = args
            return fragment
        }
    }

    private val handler = Handler()
    private var clickAble = true
    private var categoryValue: String? = null

    override fun initViews(parent: View) {
        super.initViews(parent)
        categoryValue = arguments?.getString(Constants.PARAM_TYPE)
        initEvent()
    }


    private fun initEvent() {

        // 双击tab 刷新
        presenter.subsribeEventEntity<TabRefreshEvent>(Consumer {
            if (!isViewVisible) {
                return@Consumer
            }
            recyclerView.smoothScrollToPosition(0)
            swipeLayout.postDelayed({
                swipeLayout.autoRefresh()
            }, 200)
        })

        // 双击title返回
        presenter.subsribeEventEntity<NewsProductScrollToTopEvent>(Consumer {
            if (!isViewVisible) {
                return@Consumer
            }
            recyclerView.smoothScrollToPosition(0)
        })
    }


    override fun getRecyclerAdapter(): RecyclerArrayAdapter<News> {

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
            startActivity(Intent(context,NewsDetailActivity::class.java).putExtra(Constants.PARAM_ID,news.id))
        }
        return adapter
    }


    override fun getItemDecoration(): RecyclerView.ItemDecoration? {
        val itemDecoration = DividerDecoration(
            ContextCompat.getColor(context!!, R.color.colorLine),
            SizeUtils.dp2px(0.5f),
            SizeUtils.dp2px(15f),
            0
        )
        itemDecoration.setDrawHeaderFooter(false)
        return itemDecoration
    }

    override fun onRefresh() {
        super.onRefresh()
        presenter.loadNewsList(this, pageSize, lastItemId, categoryValue)
    }

    override fun onLoadMore() {
        super.onLoadMore()
        presenter.loadNewsList(this, pageSize, lastItemId, categoryValue)
    }


}