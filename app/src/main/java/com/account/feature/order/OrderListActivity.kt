package com.account.feature.order

import android.os.Handler
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.account.R
import com.account.base.ui.list.BaseRefreshListActivity
import com.account.entity.order.Order
import com.account.feature.order.holders.OrderListHolder
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.jude.easyrecyclerview.decoration.DividerDecoration

/**
 * Created by Wangsw on 2020/6/3 0003 17:42.
 * </br>
 * 订单列表
 */
class OrderListActivity: BaseRefreshListActivity<Order, OrderListPresenter>(), OrderListView {

    private val handler = Handler()
    private var clickAble = true


    override fun getMainTitle(): Int? = R.string.order_list_title

    override fun initViews() {
        super.initViews()
        parent_container.setBackgroundColor(ColorUtils.getColor(R.color.color_list_gray_background))
        recyclerView.setBackgroundColor(ColorUtils.getColor(R.color.color_list_gray_background))
    }
    override fun getRecyclerAdapter(): RecyclerArrayAdapter<Order> {

        val adapter = object : RecyclerArrayAdapter<Order>(context) {

            override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<Order> {

                return OrderListHolder(parent)
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

//            startActivity(
//                Intent(activity, ProductDetailActivity::class.java).putExtra(
//                    Constants.PARAM_PRODUCT_ID,
//                    data.id
//                )
//            )
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