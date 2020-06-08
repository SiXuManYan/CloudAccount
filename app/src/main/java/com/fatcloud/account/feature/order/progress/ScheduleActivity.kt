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
import com.fatcloud.account.feature.order.details.enterprise.company.CompanyRegisterRegisterInfoActivity
import com.fatcloud.account.feature.order.details.personal.RegistrantInfoActivity
import com.fatcloud.account.feature.order.progress.holders.ScheduleHolder
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
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
        presenter.getProgressData(this, orderId)
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
    }

    override fun getRecyclerAdapter(): RecyclerArrayAdapter<BusinessProgress> {

        val adapter = object : RecyclerArrayAdapter<BusinessProgress>(context) {

            override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<BusinessProgress> {

                val holder = ScheduleHolder(parent)

                holder.itemView.findViewById<TextView>(R.id.look_detail_tv).setOnClickListener {
                    val adapter = getAdapter()
                    val adapterPosition = holder.adapterPosition
                    val model = adapter?.allData?.get(adapterPosition)
                    model?.let {


                        when (it.code) {
                            "PW1" -> {
                                //  营业执照办理
                                if (it.mold == "P2") {

                                    // 企业
                                    startActivity(
                                        Intent(this@ScheduleActivity, CompanyRegisterRegisterInfoActivity::class.java)
                                            .putExtra(Constants.PARAM_ORDER_WORK_ID, orderId)
                                            .putExtra(Constants.PARAM_PRODUCT_WORK_TYPE, it.mold)
                                    )
                                } else {
                                    // 个人
                                    startActivity(
                                        Intent(this@ScheduleActivity, RegistrantInfoActivity::class.java)
                                            .putExtra(Constants.PARAM_ORDER_ID, orderId)
                                            .putExtra(Constants.PARAM_PRODUCT_WORK_TYPE, it.code)
                                    )
                                }
                            }
                            "PW2" -> {
                                //  企业税务登记办理 无事件
                                if (it.mold != "P2") {
                                    // 个人
                                    startActivity(
                                        Intent(this@ScheduleActivity, RegistrantInfoActivity::class.java)
                                            .putExtra(Constants.PARAM_ORDER_ID, orderId)
                                            .putExtra(Constants.PARAM_PRODUCT_WORK_TYPE, it.code)
                                    )
                                }


                            }
                            "PW3" -> {
                                // 银行账户办理

                                if (it.mold == "P2") {
                                    // 企业
                                    if (it.state == "OW1") {

                                    } else {
                                        // 银行信息页
                                    }

                                }
                            }
                            "PW4" -> {
                                // 代理记账办理
                            }

                        }


                    }


                }
                return holder
            }

        }


        return adapter
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration? {
        return null
    }

}