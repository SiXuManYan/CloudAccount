package com.fatcloud.account.feature.news.child

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.list.BaseRefreshListFragment
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.news.News
import com.fatcloud.account.event.entity.NewsProductScrollToTopEvent
import com.fatcloud.account.event.entity.TabRefreshEvent
import com.fatcloud.account.feature.home.adapters.NewsChildAdapter
import com.fatcloud.account.feature.news.detail.NewsDetailActivity
import com.blankj.utilcode.util.SizeUtils
import com.fatcloud.account.event.Event
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.jude.easyrecyclerview.decoration.DividerDecoration
import io.reactivex.functions.Consumer

/**
 * Created by Wangsw on 2020/5/29  16:56.
 * </br>
 * 资讯子列表
 * 请求回来的list size < pageSize 则为最后一页
 */
class NewsChildFragment : BaseRefreshListFragment<News, NewsChildPresenter>(), NewsChildView {


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
        pageSize = 20
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

        presenter.subsribeEvent(Consumer {
            when (it.code) {
                Constants.EVENT_ADD_NEWS_PAGE_VIEWS -> addPageViews(it)
                else -> {
                }
            }
        })
    }

    /**
     * 增加浏览量
     */
    private fun addPageViews(it: Event) {

        val newsId = it.content
        if (!newsId.isNullOrBlank()) {
            val allData = getAdapter()?.allData
            allData?.forEachIndexed { index, news ->
                if (news.id == newsId) {
                    news.readCount = (news.readCount + 1)
                    getAdapter()?.notifyItemChanged(index)
                    return@forEachIndexed
                }

            }

        }
    }


    override fun getRecyclerAdapter(): RecyclerArrayAdapter<News> {

        val adapter = NewsChildAdapter(context)

        adapter.setOnItemClickListener {
            if (!clickAble) {
                return@setOnItemClickListener
            }
            val news = adapter.allData[it]
            startActivity(Intent(context, NewsDetailActivity::class.java).putExtra(Constants.PARAM_ID, news.id))

            clickAble = false
            handler.postDelayed({
                clickAble = true
            }, 1000)

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