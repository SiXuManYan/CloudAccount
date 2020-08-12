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
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.form.p9p10.NativeFormPersonalPackageP9P10Draft
import com.fatcloud.account.entity.local.form.*
import com.fatcloud.account.entity.order.persional.Order
import com.fatcloud.account.event.entity.BankFormCommitSuccessEvent
import com.fatcloud.account.event.entity.OrderPaySuccessEvent
import com.fatcloud.account.feature.defray.PayActivity
import com.fatcloud.account.feature.forms.enterprise.license.basic.FormEnterpriseBasicActivity
import com.fatcloud.account.feature.forms.personal.bank.basic.FormPersonalBankBasicActivity
import com.fatcloud.account.feature.forms.personal.bookkeeping.FormAgentBookkeepingPersonalActivity
import com.fatcloud.account.feature.forms.personal.license.FormLicensePersonalActivity
import com.fatcloud.account.feature.forms.personal.packages.FormPersonalPackageP9P10Activity
import com.fatcloud.account.feature.forms.personal.tax.FormTaxRegistrationPersonalActivity
import com.fatcloud.account.feature.order.lists.holders.OrderListHolder
import com.fatcloud.account.feature.order.progress.ScheduleActivity
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.jude.easyrecyclerview.decoration.DividerDecoration
import io.reactivex.functions.Consumer
import java.util.*

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
                Constants.EVENT_REFRESH_ORDER_LIST_FROM_END_COUNT_DOWN,
                Constants.EVENT_REFRESH_ORDER_LIST_FROM_DELETE_DRAFT,
                Constants.EVENT_NEED_REFRESH -> {
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

                orderListHolder.itemView.findViewById<TextView>(R.id.payment_tv).setOnClickListener {

                    getAdapter()?.let {
                        val adapterPosition = orderListHolder.adapterPosition - it.headerCount
                        if (adapterPosition < 0 || adapterPosition >= it.allData.size) return@let

                        val order = it.allData[adapterPosition]

                        startActivity(
                            Intent(this@OrderListActivity, PayActivity::class.java)
                                .putExtra(Constants.PARAM_ORDER_ID, order.id)
                                .putExtra(Constants.PARAM_ORDER_NUMBER, order.no)
                                .putExtra(Constants.PARAM_MONEY, order.money)
                        )

                    }

                }


                orderListHolder.itemView.findViewById<TextView>(R.id.look_detail_tv).setOnClickListener {
                    getAdapter()?.let {
                        val adapterPosition = orderListHolder.adapterPosition - it.headerCount
                        if (adapterPosition < 0 || adapterPosition >= it.allData.size) return@let
                        val model = it.allData[adapterPosition]

                        if (model.state != Constants.OS_UN_SUBMITTED) {
                            startActivity(Intent(this@OrderListActivity, ScheduleActivity::class.java).putExtra(Constants.PARAM_ORDER_ID, model.id))
                        } else {
                            unSubmittedOrderClick(model)
                        }

                    }

                }

                orderListHolder.itemView.findViewById<TextView>(R.id.delete_draft_tv).setOnClickListener {

                    doItemRemove(orderListHolder)
                    /*
                    AlertDialog.Builder(this@OrderListActivity)
                        .setTitle(getString(R.string.prompt))
                        .setMessage(getString(R.string.delete_draft_confirm))
                        .setCancelable(true)
                        .setPositiveButton(R.string.confirm, AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, which ->

                            dialog.dismiss()
                        })
                        .setNegativeButton(R.string.cancel, AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, which ->
                            dialog.dismiss()
                        })
                        .create()
                        .show()
                    */

                }



                return orderListHolder
            }

            private fun doItemRemove(orderListHolder: OrderListHolder) {
                getAdapter()?.let {
                    val adapterPosition = orderListHolder.adapterPosition - it.headerCount
                    if (adapterPosition < 0 || adapterPosition >= it.allData.size) return@let
                    val model = it.allData[adapterPosition]

                    if (model.state != Constants.OS_UN_SUBMITTED) {
                        return@let
                    }
                    it.remove(adapterPosition)
                    ProductUtils.deleteDraft(model.mold)

                }
            }

        }

        return adapter
    }

    /**
     * 未提交订单，进入目标页面
     */
    private fun unSubmittedOrderClick(model: Order) {
        when (model.mold) {
            Constants.P1 -> {
                val p1 = model.nativeExtraDraftObject as PersonalLicenseDraft
                startActivity(
                    Intent(this@OrderListActivity, FormLicensePersonalActivity::class.java)
                        .putExtra(Constants.PARAM_PRODUCT_ID, p1.productPriceId)
                        .putExtra(Constants.PARAM_FINAL_MONEY, p1.finalMoney)
                        .putExtra(Constants.PARAM_PRODUCT_PRICE_ID, p1.productPriceId)
                )
            }
            Constants.P2 -> {

                val p2 = model.nativeExtraDraftObject as EnterprisePackageDraft
                startActivity(
                    Intent(this@OrderListActivity, FormEnterpriseBasicActivity::class.java)
                        .putExtra(Constants.PARAM_PRODUCT_ID, p2.productId)
                        .putExtra(Constants.PARAM_INCOME_MONEY, p2.finalMoney)
                        .putExtra(Constants.PARAM_FINAL_MONEY, p2.finalMoney)
                        .putExtra(Constants.PARAM_PRODUCT_PRICE_ID, p2.productPriceId)
                )

            }
            Constants.P3 -> {
                val p3 = model.nativeExtraDraftObject as PersonalBookkeepingDraft

                startActivity(
                    Intent(this, FormAgentBookkeepingPersonalActivity::class.java)
                        .putExtra(Constants.PARAM_PRODUCT_ID, p3.productId)
                        .putExtra(Constants.PARAM_FINAL_MONEY, p3.finalMoney)
                        .putExtra(Constants.PARAM_PRODUCT_PRICE_ID, p3.productPriceId)
                )


            }
            Constants.P4 -> {

                val p4 = model.nativeExtraDraftObject as PersonalTaxDraft
                // 税务登记
                startActivity(
                    Intent(this, FormTaxRegistrationPersonalActivity::class.java)
                        .putExtra(Constants.PARAM_PRODUCT_ID, p4.productId)
                        .putExtra(Constants.PARAM_FINAL_MONEY, p4.finalMoney)
                        .putExtra(Constants.PARAM_PRODUCT_PRICE_ID, p4.productPriceId)
                )

            }
            Constants.P8 -> {
                val p8 = model.nativeExtraDraftObject as BankPersonalDraft

                startActivity(
                    Intent(this, FormPersonalBankBasicActivity::class.java)
                        .putExtra(Constants.PARAM_PRODUCT_ID, p8.productId)
                        .putExtra(Constants.PARAM_FINAL_MONEY, p8.finalMoney)
                        .putExtra(Constants.PARAM_PRODUCT_PRICE_ID, p8.productPriceId)
                        .putExtra(Constants.PARAM_MOLD, Constants.P8)
                )

            }
            Constants.P9,
            Constants.P10 -> {
                val p9p10 = model.nativeExtraDraftObject as NativeFormPersonalPackageP9P10Draft
                startActivity(
                    Intent(this, FormPersonalPackageP9P10Activity::class.java)
                        .putExtra(Constants.PARAM_PRODUCT_ID, p9p10.productId)
                        .putExtra(Constants.PARAM_FINAL_MONEY, p9p10.finalMoney)
                        .putExtra(Constants.PARAM_PRODUCT_PRICE_ID, p9p10.productPriceId)
                        .putExtra(Constants.PARAM_MOLD, model.mold)
                )
            }

        }
    }


    override fun getItemDecoration(): RecyclerView.ItemDecoration? {
        val itemDecoration = DividerDecoration(
            ContextCompat.getColor(context!!, R.color.color_list_gray_background),
            SizeUtils.dp2px(10f)
        )
        itemDecoration.setDrawLastItem(false)
        return itemDecoration
    }


    override fun bindList(list: ArrayList<Order>, isFirstPage: Boolean, last: Boolean) {
        val currentAdapter = getAdapter()

        if (isFirstPage) {
            if (swipeLayout.isRefreshing) {
                swipeLayout.finishRefresh(true)
            }

            currentAdapter?.clear()
            // 第一个，空View
            if (currentAdapter?.footerCount!! > 0) {
                currentAdapter?.removeAllFooter()
            }

            // 增加草稿箱
            val draftModule = presenter.getDraftModule()
            currentAdapter.addAll(draftModule)
            if (list.isEmpty()) {
                currentAdapter?.addFooter(emptyImageFooter)
            }
        } else {
            if (swipeLayout.isLoading) {
                if (list.isEmpty()) {
                    swipeLayout.finishLoadMoreWithNoMoreData()  // 加载完毕
                } else {
                    swipeLayout.finishLoadMore()  // 完成加载
                }
            }
        }

        currentAdapter?.addAll(list)

        // 是否可以上拉加载
        swipeLayout.setEnableLoadMore(!last)

        if (!disableLoadMoreView) {
            if (last && currentAdapter?.allData!!.size > 0) {
                if (currentAdapter?.footerCount!! > 0) currentAdapter?.removeAllFooter()
                currentAdapter?.addFooter(noMoreItemView)
            }
        }

    }


}