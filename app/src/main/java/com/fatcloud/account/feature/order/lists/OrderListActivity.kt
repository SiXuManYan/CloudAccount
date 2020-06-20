package com.fatcloud.account.feature.order.lists

import android.content.Intent
import android.os.Handler
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.list.BaseRefreshListActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.persional.Order
import com.fatcloud.account.event.entity.BankFormCommitSuccessEvent
import com.fatcloud.account.event.entity.OrderPaySuccessEvent
import com.fatcloud.account.event.entity.RefreshOrderEvent
import com.fatcloud.account.feature.defray.PayActivity
import com.fatcloud.account.feature.order.lists.holders.OrderListHolder
import com.fatcloud.account.feature.order.progress.ScheduleActivity
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.jude.easyrecyclerview.decoration.DividerDecoration
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.item_order.*

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
        disableLoadMoreView = true
        super.initViews()
        initEvent()
        parent_container.setBackgroundColor(ColorUtils.getColor(R.color.color_list_gray_background))
        recyclerView.setBackgroundColor(ColorUtils.getColor(R.color.color_list_gray_background))
    }

    private fun initEvent() {
        presenter.subsribeEventEntity<BankFormCommitSuccessEvent>(Consumer {
            loadOnVisible()
        })
        presenter.subsribeEventEntity<OrderPaySuccessEvent>(Consumer {
            loadOnVisible()
        })
        presenter.subsribeEvent(Consumer {
            when (it.code) {
                Constants.EVENT_REFRESH_ORDER_LIST_FROM_END_COUNT_DOWN -> {
                    loadOnVisible()
                }
                else -> {
                }
            }
        })


    }

    override fun getRecyclerAdapter(): RecyclerArrayAdapter<Order> {

        val adapter = object : RecyclerArrayAdapter<Order>(context) {

            override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<Order> {

                val orderListHolder = OrderListHolder(parent)


                getAdapter()?.let {
                    val adapterPosition = orderListHolder.adapterPosition - it.headerCount
                    if (adapterPosition < 0 || adapterPosition >= it.allData.size) return@let

                    val order = it.allData[adapterPosition]
                }

                orderListHolder.itemView.findViewById<TextView>(R.id.payment_tv).setOnClickListener {
                    getAdapter()?.let {
                        val adapterPosition = orderListHolder.adapterPosition - it.headerCount
                        if (adapterPosition < 0 || adapterPosition >= it.allData.size) return@let

                        val order = it.allData[adapterPosition]

                        startActivity(
                            Intent(this@OrderListActivity, PayActivity::class.java)
                                .putExtra(Constants.PARAM_ORDER_ID, order.id)
                                .putExtra(Constants.PARAM_ORDER_NUMBER, order.no)
                                .putExtra(Constants.PARAM_MONEY, order.money.toPlainString())
                        )

                    }


                }


                orderListHolder.itemView.findViewById<TextView>(R.id.look_detail_tv).setOnClickListener {
                    getAdapter()?.let {
                        val adapterPosition = orderListHolder.adapterPosition - it.headerCount
                        if (adapterPosition < 0 || adapterPosition >= it.allData.size) return@let
                        val model = it.allData[adapterPosition]

                        startActivity(
                            Intent(this@OrderListActivity, ScheduleActivity::class.java)
                                .putExtra(Constants.PARAM_ORDER_ID, model.id)
                                .putExtra(Constants.PARAM_MOLD, model.mold)
                        )
                    }

                }


                return orderListHolder
            }

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