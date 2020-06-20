package com.fatcloud.account.feature.product

import android.content.Intent
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.list.BaseRefreshListFragment
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.product.Product2
import com.fatcloud.account.event.entity.TabRefreshEvent
import com.fatcloud.account.feature.home.header.HomeHeader
import com.fatcloud.account.feature.product.detail.ProductDetailActivity
import com.fatcloud.account.feature.product.holder.ProductHolder
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SizeUtils
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.jude.easyrecyclerview.decoration.DividerDecoration
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_product.*

/**
 * Created by Wangsw on 2020/5/25 0025 16:42.
 * </br>
 * 产品列表
 * 请求回来的list size < pageSize 则为最后一页
 */
class ProductFragment : BaseRefreshListFragment<Product2, ProductPresenter>(), ProductView {

    init {
        needMenuControl = true
    }

    private val handler = Handler()
    private var clickAble = true
    private var homeHeader: HomeHeader? = null

    override fun getLayoutId() = R.layout.fragment_product

    override fun initViews(parent: View) {
        disableLoadMoreView = true
        super.initViews(parent)

        val layoutParams = parent_container.layoutParams as FrameLayout.LayoutParams
        layoutParams.topMargin = BarUtils.getStatusBarHeight()


        title_tv.setOnClickListener {
            if (CommonUtils.isDoubleClick(it)) {
                recyclerView.smoothScrollToPosition(0)
            }

        }
        initEvent()
    }


    private fun initEvent() {


        presenter.subsribeEventEntity<TabRefreshEvent>(Consumer {
            if (it.clx == ProductFragment::class.java) {
                if (isViewVisible) {
                    recyclerView.smoothScrollToPosition(0)
                    swipeLayout.postDelayed({
                        swipeLayout.autoRefresh()
                    }, 200)
                }
            }

        })
    }


    override fun getRecyclerAdapter(): RecyclerArrayAdapter<Product2> {

        val adapter = object : RecyclerArrayAdapter<Product2>(context) {

            override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<Product2> {

                return ProductHolder(parent)
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

            val data = adapter.allData[it]

            startActivity(
                Intent(activity, ProductDetailActivity::class.java).putExtra(
                    Constants.PARAM_PRODUCT_ID,
                    data.id
                )
            )
        }
        return adapter
    }


    override fun getItemDecoration(): RecyclerView.ItemDecoration? {
        val itemDecoration = DividerDecoration(
            ContextCompat.getColor(context!!, R.color.color_list_gray_background),
            SizeUtils.dp2px(10f)
        )
        itemDecoration.setDrawLastItem(false)
        return itemDecoration
    }

}