package com.account.feature.home

import android.os.Handler
import android.view.View
import android.view.ViewGroup
import com.account.base.ui.list.BaseRefreshListFragment2
import com.account.entity.home.News
import com.account.feature.home.header.HomeHeader
import com.account.feature.home.holder.NewsHolderSingle
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter

/**
 * Created by Wangsw on 2020/5/25 0025 16:41.
 * </br>
 * 首页 混合信息
 */
open class HomeFragment : BaseRefreshListFragment2<News, HomePresenter>(), HomeView {

    init {
        needMenuControl = true
    }

    private val handler = Handler()
    private var clickAble = true
    private var homeHeader: HomeHeader? = null




    override fun loadOnVisible() {
        onRefresh(swipeLayout)
    }


    override fun initViews(parent: View) {
        super.initViews(parent)
        initEvent()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        //  添加Header
        homeHeader = HomeHeader(context!!)
    }


    private fun initEvent() {

    }

    override fun getRecyclerAdapter(): RecyclerArrayAdapter<News> {

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
//            val name = adapter.allData.get(it).name
//            ToastUtils.showShort("热门产品点击name = :$name")

        }
        return adapter
    }


}