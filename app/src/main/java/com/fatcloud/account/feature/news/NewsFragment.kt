package com.fatcloud.account.feature.news

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.OnClick
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseFragment
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.entity.news.NewsCategory
import com.fatcloud.account.event.RxBus
import com.fatcloud.account.event.entity.NewsProductScrollToTopEvent
import com.fatcloud.account.event.entity.TabRefreshEvent
import com.fatcloud.account.feature.news.child.NewsChildFragment
import com.fatcloud.account.view.tabs.SlidingTabLayout
import com.blankj.utilcode.util.BarUtils
import com.google.android.material.appbar.AppBarLayout
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_news.*

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
        val layoutParams = parent_cl.layoutParams as FrameLayout.LayoutParams
        layoutParams.topMargin = BarUtils.getStatusBarHeight()

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

        override fun getItem(position: Int): Fragment = NewsChildFragment.newInstance(typeList[position].value)

        override fun getCount() = typeList.size

        override fun getPageTitle(position: Int) = typeList[position].name
    }


}