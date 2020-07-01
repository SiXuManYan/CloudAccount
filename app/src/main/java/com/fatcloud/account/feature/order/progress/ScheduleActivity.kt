package com.fatcloud.account.feature.order.progress

import android.content.Intent
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.list.BaseRefreshListActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.persional.Order
import com.fatcloud.account.entity.order.progress.BusinessProgress
import com.fatcloud.account.event.entity.BankFormCommitSuccessEvent
import com.fatcloud.account.event.entity.RefreshOrderEvent
import com.fatcloud.account.feature.forms.enterprise.bank.FormBankActivity
import com.fatcloud.account.feature.forms.enterprise.bank.basic.FormBankBasicActivity
import com.fatcloud.account.feature.order.details.bookkeeping.BookkeepingInfoActivity
import com.fatcloud.account.feature.order.details.enterprise.company.CompanyRegisterInfoActivity
import com.fatcloud.account.feature.order.details.personal.RegistrantInfoActivity
import com.fatcloud.account.feature.order.progress.holders.ScheduleHolder
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import io.reactivex.functions.Consumer
import java.util.*

/**
 * Created by Wangsw on 2020/6/4 0004 11:58.
 * </br>
 * 业务办理流程
 */
class ScheduleActivity : BaseRefreshListActivity<BusinessProgress, SchedulePresenter>(), ScheduleView {

    var orderId: String? = ""

    /**
     * @see Order.mold
     */
    var mold: String? = ""

    override fun getMainTitle(): Int? {
        return R.string.business_progress_title
    }


    override fun initViews() {
        super.initViews()
        easyRecyclerView.setBackgroundColor(ColorUtils.getColor(R.color.color_list_gray_background))
        swipeLayout.setEnableRefresh(false)
        initExtra()
        initEvent()
    }

    private fun initEvent() {

        // 更新当前页面
        presenter.subsribeEventEntity<BankFormCommitSuccessEvent>(Consumer {
            finish()
        })

    }

    override fun bindList(list: ArrayList<BusinessProgress>, isFirstPage: Boolean, last: Boolean) {
        super.bindList(list, isFirstPage, last)
        getAdapter()?.removeFooter(noMoreItemView)
    }

    private fun initExtra() {

        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_ID)) {
            finish()
            return
        }
        orderId = intent.extras!!.getString(Constants.PARAM_ORDER_ID)
        mold = intent.extras!!.getString(Constants.PARAM_MOLD)
        loadDetailData()
    }

    private fun loadDetailData() = presenter.getProgressData(this, orderId)

    override fun getRecyclerAdapter(): RecyclerArrayAdapter<BusinessProgress> {

        val adapter = object : RecyclerArrayAdapter<BusinessProgress>(context) {

            override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<BusinessProgress> {

                val holder = ScheduleHolder(parent)

                holder.itemView.findViewById<TextView>(R.id.look_detail_tv).setOnClickListener {
                    val adapter = getAdapter()
                    val adapterPosition = holder.adapterPosition
                    val model = adapter?.allData?.get(adapterPosition)
                    model?.let {
                        when (it.mold) {
                            Constants.P2 -> handleEnterpriseProduct(it)
                            else -> handlePersonalProduct(it)
                        }
                    }
                }
                return holder
            }

        }


        return adapter
    }


    override fun getItemDecoration(): RecyclerView.ItemDecoration? = null


    /**
     * 个人业务
     */
    private fun handlePersonalProduct(it: BusinessProgress) {
        when (it.code) {
            Constants.PW1, Constants.PW2 -> {
                startActivity(
                    Intent(this@ScheduleActivity, RegistrantInfoActivity::class.java)
                        .putExtra(Constants.PARAM_ORDER_ID, orderId)
                        .putExtra(Constants.PARAM_PRODUCT_WORK_TYPE, it.code)
                )
            }
            Constants.PW3 -> {

            }
            Constants.PW4 -> {
                startActivity(
                    Intent(this@ScheduleActivity, BookkeepingInfoActivity::class.java)
                        .putExtra(Constants.PARAM_ORDER_ID, orderId)
                        .putExtra(Constants.PARAM_PRODUCT_WORK_TYPE, it.code)
                )

            }

        }

    }

    /**
     * 企业业务
     */
    private fun handleEnterpriseProduct(it: BusinessProgress) {
        when (it.code) {
            Constants.PW1 -> {
                startActivity(
                    Intent(this@ScheduleActivity, CompanyRegisterInfoActivity::class.java)
                        .putExtra(Constants.PARAM_ORDER_WORK_ID, it.id)
                        .putExtra(Constants.PARAM_PRODUCT_WORK_TYPE, it.code)
                )
            }
            Constants.PW2 -> {
                // 企业税务登记无事件
            }
            Constants.PW3 -> {
                if (it.state == "OW1") {
                    // 编辑页

                    startActivity(
                        Intent(this@ScheduleActivity, FormBankBasicActivity::class.java)
                            .putExtra(Constants.PARAM_ORDER_WORK_ID, it.id)
                            .putExtra(Constants.PARAM_PRODUCT_WORK_TYPE, it.code)
                            .putExtra(Constants.PARAM_ORDER_ID, orderId)
                    )

                } else {
                    // 回显银行信息页
                    startActivity(
                        Intent(this@ScheduleActivity, CompanyRegisterInfoActivity::class.java)
                            .putExtra(Constants.PARAM_ORDER_WORK_ID, it.id)
                            .putExtra(Constants.PARAM_PRODUCT_WORK_TYPE, it.code)
                            .putExtra(Constants.PARAM_ORDER_ID, orderId)
                    )

                }
            }
            Constants.PW4 -> {
                startActivity(
                    Intent(this@ScheduleActivity, BookkeepingInfoActivity::class.java)
                        .putExtra(Constants.PARAM_ORDER_ID, orderId)
                        .putExtra(Constants.PARAM_PRODUCT_WORK_TYPE, it.code)
                )

            }

        }
    }

}