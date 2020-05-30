package com.account.feature.news

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.OnClick
import com.account.R
import com.account.base.ui.BaseFragment
import com.account.common.CommonUtils
import com.account.entity.news.NewsCategory
import com.account.event.RxBus
import com.account.event.entity.NewsProductScrollToTopEvent
import com.account.event.entity.TabRefreshEvent
import com.account.feature.news.child.NewsChildFragment
import com.account.view.tabs.SlidingTabLayout
import com.google.android.material.appbar.AppBarLayout
import io.reactivex.functions.Consumer

/**
 * Created by Wangsw on 2020/5/25 0025 16:42.
 * </br>
 *
 */
class NewsFragment : BaseFragment<NewsPresenter>(), NewsView {

    init {
        needMenuControl = true
    }

    @BindView(R.id.appbar_layout)
    lateinit var appBarLayout: AppBarLayout

    @BindView(R.id.tabs)
    lateinit var tabLayout: SlidingTabLayout

    @BindView(R.id.pager)
    lateinit var pager: ViewPager

    private var typeList: List<NewsCategory> = ArrayList()


    override fun getLayoutId() = R.layout.fragment_news

    override fun initViews(parent: View) {
        typeList = presenter.getNewCategoryList()

        pager.adapter = InnerPageAdapter(childFragmentManager)

        if (typeList.size > 1) {
            pager.offscreenPageLimit = typeList.size - 1
        }

        tabLayout.apply {
            setViewPager(pager)
            setSelectTextScale(1.0f)
            updateTabSelection(pager.currentItem)

        }

        presenter.subsribeEventEntity<TabRefreshEvent>(Consumer {
            if (isViewVisible) {
                appBarLayout.setExpanded(true, true)
            }
        })


    }


    override fun loadOnVisible() = Unit


    @OnClick(R.id.title_tv)
    fun onClick(view: View) {
        when (view.id) {
            R.id.title_tv -> {
                if (CommonUtils.isDoubleClick(view)) {
                    appBarLayout.setExpanded(true, true)
                    RxBus.post(NewsProductScrollToTopEvent())
                }
            }
            else -> {
            }
        }

    }


    private inner class InnerPageAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment = NewsChildFragment()

        override fun getCount() = typeList.size

        override fun getPageTitle(position: Int) = typeList[position].name
    }


}