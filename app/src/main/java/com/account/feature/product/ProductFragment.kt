package com.account.feature.product

import android.content.Intent
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.account.R
import com.account.base.ui.list.BaseRefreshListFragment2
import com.account.common.CommonUtils
import com.account.common.Constants
import com.account.entity.product.Product2
import com.account.event.entity.TabRefreshEvent
import com.account.feature.home.HomeFragment
import com.account.feature.home.header.HomeHeader
import com.account.feature.product.detail.ProductDetailActivity
import com.account.feature.product.holder.ProductHolder
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
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
class ProductFragment : BaseRefreshListFragment2<Product2, ProductPresenter>(), ProductView {

    init {
        needMenuControl = true
    }

    private val handler = Handler()
    private var clickAble = true
    private var homeHeader: HomeHeader? = null

    override fun getLayoutId() = R.layout.fragment_product

    override fun initViews(parent: View) {
        super.initViews(parent)
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

            // TODO 资讯点击事件
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