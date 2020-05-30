package com.account.feature.news.child

import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.account.R
import com.account.base.ui.list.BaseRefreshListFragment2
import com.account.entity.news.News
import com.account.event.entity.NewsProductScrollToTopEvent
import com.account.event.entity.TabRefreshEvent
import com.account.feature.home.adapters.NewsChildAdapter
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
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

    private val handler = Handler()
    private var clickAble = true

    override fun initViews(parent: View) {
        super.initViews(parent)
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

            // TODO 资讯点击事件
            val title = adapter.allData[it].title
            ToastUtils.showShort("资讯点击 = title:$title")

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

}