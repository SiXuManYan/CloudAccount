package com.fatcloud.account.feature.order.progress.holders

import android.view.View
import android.view.ViewGroup
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.list.BaseItemViewHolder
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.progress.BusinessProgress
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_schedule.*

class ScheduleHolder(parent: ViewGroup?) : BaseItemViewHolder<BusinessProgress>(parent, R.layout.item_schedule), LayoutContainer {


    override val containerView: View? get() = itemView

    override fun setData(data: BusinessProgress?) {

        if (data == null) {
            return
        }
        val ownerAdapter = getOwnerAdapter<RecyclerArrayAdapter<BusinessProgress>>()

        // 设置图片
        if (adapterPosition == 0) {
            image_iv.setImageResource(R.drawable.ic_order_detail_progress_top)

        } else if (data.state == Constants.OW3) {
            if (adapterPosition == ownerAdapter?.allData?.size!! - 1) {
                image_iv.setImageResource(R.drawable.ic_order_detail_progress_end_red)
            } else {
                image_iv.setImageResource(R.drawable.ic_order_detail_progress_center_red)
            }
        } else {
            if (adapterPosition == ownerAdapter?.allData?.size!! - 1) {
                image_iv.setImageResource(R.drawable.ic_order_detail_progress_end_gray)
            } else {
                image_iv.setImageResource(R.drawable.ic_order_detail_progress_center_gray)
            }
        }

        title_tv.text = data.productWorkName
        content_tv.text = data.productWorkIntroduce
        status_tv.text = data.stateText



        when (data.mold) {
            Constants.P2 -> handleEnterpriseProduct(data)
            Constants.P9,
            Constants.P10 -> {
                handlePersonalProductP9P10(data)
            }
            Constants.P11 -> {
                handlePersonalProductP11(data)
            }
            else -> handlePersonalProduct(data)
        }
    }


    private fun handlePersonalProductP11(data: BusinessProgress) {
        image_iv.visibility = View.GONE
        look_detail_tv.visibility = View.INVISIBLE
    }


    private fun handlePersonalProductP9P10(data: BusinessProgress) {
        when (data.code) {
            Constants.PW1 -> {
                look_detail_tv.visibility = View.VISIBLE
                look_detail_tv.text = "查看"
            }
            Constants.PW2 -> {
                look_detail_tv.visibility = if (data.state == Constants.OW4) {
                    View.INVISIBLE
                } else {
                    View.VISIBLE
                }
            }
            Constants.PW3 -> {
                look_detail_tv.visibility = if (data.state == Constants.OW4) {
                    View.INVISIBLE
                } else {
                    View.VISIBLE
                }
            }
            Constants.PW4 -> {
                look_detail_tv.visibility = if (data.state == Constants.OW4) {
                    View.INVISIBLE
                } else {
                    View.VISIBLE
                }
            }

        }

    }

    /**
     * 个人业务
     */
    private fun handlePersonalProduct(data: BusinessProgress) {
        look_detail_tv.visibility = View.VISIBLE
        look_detail_tv.text = "查看"
    }

    /**
     * 企业套餐
     */
    private fun handleEnterpriseProduct(data: BusinessProgress) {
        when (data.code) {
            Constants.PW1 -> {
                look_detail_tv.visibility = View.VISIBLE
                look_detail_tv.text = "查看"
            }
            Constants.PW2 -> {
                look_detail_tv.visibility = View.INVISIBLE
            }
            Constants.PW3 -> {
                if (data.state != Constants.OW4) {
                    look_detail_tv.visibility = View.VISIBLE
                    look_detail_tv.text = "查看"
                } else {
                    look_detail_tv.visibility = View.GONE
                }
            }
            Constants.PW4 -> {

                if (data.state == Constants.OW2 || data.state == Constants.OS7) {
                    look_detail_tv.visibility = View.VISIBLE
                    look_detail_tv.text = "查看"
                }
            }

        }
    }
}