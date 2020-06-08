package com.fatcloud.account.feature.order.lists

import android.content.Intent
import android.os.Handler
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.list.BaseRefreshListActivity
import com.fatcloud.account.entity.order.Order
import com.fatcloud.account.feature.order.lists.holders.OrderListHolder
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.feature.order.progress.ScheduleActivity
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.jude.easyrecyclerview.decoration.DividerDecoration

/**
 * Created by Wangsw on 2020/6/3 0003 17:42.
 * </br>
 * 订单列表
 */
class OrderListActivity : BaseRefreshListActivity<Order, OrderListPresenter>(), OrderListView {

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

            startActivity(
                Intent(this, ScheduleActivity::class.java)
                    .putExtra(Constants.PARAM_ORDER_ID, data.id)
                    .putExtra(Constants.PARAM_MOLD, data.mold)

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