package com.account.feature

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.account.R
import com.account.base.ui.BaseMVPActivity
import com.account.common.Constants
import com.account.entity.TabItem
import com.account.event.RxBus
import com.account.event.entity.TabRefreshEvent
import com.account.feature.home.HomeFragment
import com.account.feature.my.MyPageFragment
import com.account.feature.news.NewsFragment
import com.account.feature.services.ServiceFragment
import com.blankj.utilcode.util.ToastUtils
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import kotlinx.android.synthetic.main.activity_main.*


/**
 * 主页面
 */
class MainActivity : BaseMVPActivity<MainPresenter>(), MainView {

    /**
     * 用户点击返回键时间戳
     */
    private var tapTime = 0L
    private var lastTab = 0

    /** Tab数据 */
    private val tabs = ArrayList<CustomTabEntity>()

    /** Tab项数据 */
    private lateinit var adapter: FragmentAdapter

    private var reSelect = Pair(-1, 0L)

    override fun getLayoutId() = R.layout.activity_main


    override fun initViews() {
        initTabs(1)


        // todo 权限检测
//        presenter.checkPermissions(this)
        loginInit()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)


    }

    private fun initTabs(firstIndexTabType: Int) {

        tabs.add(
            TabItem(
                getString(R.string.home),
                R.drawable.ic_tab_home_normal,
                R.drawable.ic_tab_home_selected,
                HomeFragment::class.java
            )
        )
        tabs.add(
            TabItem(
                getString(R.string.service),
                R.drawable.ic_tab_service_normal,
                R.drawable.ic_tab_service_selected,
                ServiceFragment::class.java
            )
        )
        tabs.add(
            TabItem(
                getString(R.string.tab_news),
                R.drawable.ic_tab_news_normal,
                R.drawable.ic_tab_news_selected,
                NewsFragment::class.java
            )
        )
        tabs.add(
            TabItem(
                getString(R.string.my),
                R.drawable.ic_tab_my_normal,
                R.drawable.ic_tab_my_selected,
                MyPageFragment::class.java
            )
        )

        tabs_navigator.setTabData(tabs)
        tabs_navigator.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                this@MainActivity.onTabSelect(position)
            }

            override fun onTabReselect(position: Int) {
                this@MainActivity.onTabReselect(position)
            }

        })
        adapter = FragmentAdapter(supportFragmentManager)

    }


    private fun loginInit() {

        /*     if (!User.isLogon() || User.get().id == null) {
                 tabs_navigator.hideMsg(preferentialIndex)
                 tabs_navigator.hideMsg(2)
                 tabs_navigator.hideMsg(3)
                 //TODO 兼容老版本
     //            sendBroadcast(Intent(CommonAction.ACTION_LINKTASK_VISITOR_START))
                 DataService.startService(this, Constants.ACTION_REQUEST_EXCONDTION)
                 return
             }*/

//        DataService.startService(this, Constants.ACTION_LINKTASK_START)
    }


    override fun onBackPressed() {
        if (System.currentTimeMillis() - tapTime > 2000) {
            ToastUtils.showShort("再按一次退出")
            tapTime = System.currentTimeMillis()
        } else {
            finish()
            System.exit(0)
        }
    }


    /**
     * Tab选中
     * @param position 位置
     */
    fun onTabSelect(position: Int) {
//        if ((position == 2 || position == 3) && !User.isLogon()) {
//            tabs_navigator.currentTab = lastTab
//
//            // todo 跳转至注册页
////            startActivity(SignUpActivity::class.java)
//            return
//        }

        lastTab = position
        adapter.onTabSelect(position)
        // todo 增加页面切换请求
    }


    /**
     * Tabs双击事件
     * @param position 位置
     */
    fun onTabReselect(position: Int) {

        if (reSelect.first == position && System.currentTimeMillis() - reSelect.second < 500) {
            RxBus.post(TabRefreshEvent((tabs[position] as TabItem).clx))
            reSelect = Pair(-1, 0L)
        } else {
            reSelect = Pair(position, System.currentTimeMillis())
        }
    }


    /**
     * Tab数据适配器
     */
    private inner class FragmentAdapter internal constructor(val fm: FragmentManager) :
        FragmentPagerAdapter(fm) {

        init {
            if (fm.fragments.size > 0) {
                fm.beginTransaction().apply {
                    fm.fragments.forEach {
                        remove(it)
                    }
                    commit()
                }
            }
            tabs_navigator.post {
                tabs_navigator.currentTab = 0
                this@MainActivity.onTabSelect(0)
            }
        }

        fun onTabSelect(position: Int) {
            val fragment = adapter.instantiateItem(fl_content, position) as Fragment
            adapter.setPrimaryItem(fl_content, 0, fragment)
            adapter.finishUpdate(fl_content)

        }

        override fun getCount(): Int {
            return tabs.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return (obj as Fragment).view === view
        }

        override fun getItem(position: Int): Fragment {
//            var fragment: Fragment? = null
//            when (position) {
//
//                0 -> fragment = HomeFragment()
//                1 -> fragment = ServiceFragment()
//                2 -> fragment = NewsFragment()
//                3 -> fragment = MyPageFragment()
//            }
//            return fragment!!


            return fm.fragmentFactory.instantiate(classLoader, (tabs[position] as TabItem).clx.name)
                .apply {
                    if (position == 0) {
                        arguments = Bundle().apply {
                            putBoolean(Constants.SP_SHOW_CITY, true)
                        }
                    }
                }

        }
    }
}
