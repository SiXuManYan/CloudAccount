package com.fatcloud.account.feature.order.lists.holders

import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.fatcloud.account.R
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.list.BaseItemViewHolder
import com.fatcloud.account.common.AndroidUtil
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.persional.Order
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.RxBus
import com.fatcloud.account.extend.RoundTransFormation
import com.fatcloud.account.view.countdown.CountDownTextView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_order.*

/**
 * Created by Wangsw on 2020/6/3 0003 18:06.
 * </br>
 * 订单列表
 * // 支付中和待支付，显示倒计时
 */
class OrderListHolder(parent: ViewGroup?) : BaseItemViewHolder<Order>(parent, R.layout.item_order), LayoutContainer {

    override val containerView: View? get() = itemView

    override fun setData(data: Order?) {
        if (data == null) {
            return
        }


        if (data.state == Constants.OS_UN_SUBMITTED) {
            setDraftData(data)

        } else {
            setServerData(data)
        }


        content_tv.text = data.productName
        amount_tv.text = StringUtils.getString(R.string.money_symbol_format, data.money)
        time_tv.text = data.createDt
        order_status_tv.text = data.stateText


        val layoutParams = card_cv.layoutParams as LinearLayout.LayoutParams
        layoutParams.apply {
            topMargin = if (adapterPosition == 0) {
                SizeUtils.dp2px(10f)
            } else {
                SizeUtils.dp2px(0f)
            }
        }

        when (data.state) {
            Constants.OS1, Constants.OS4 -> {
                payment_tv.visibility = View.VISIBLE
                initCountDown(data)
            }
            else -> {
                payment_tv.visibility = View.GONE
                countdown_tv.visibility = View.GONE
            }
        }
    }


    private fun setServerData(data: Order) {
        look_detail_tv.setText(R.string.view)
        delete_draft_tv.visibility = View.GONE
        order_id_tv.apply {
            text = StringUtils.getString(R.string.order_id_format, data.no)
        }

        Glide.with(context)
            .load(data.imgUrl)
            .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
            .error(R.drawable.ic_error_image_load)
            .into(image_iv)
    }

    private fun setDraftData(data: Order) {
        look_detail_tv.setText(R.string.edit)
        delete_draft_tv.visibility = View.VISIBLE
        Glide.with(context)
            .load(getImageFromMold(data.mold))
            .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
            .error(R.drawable.ic_error_image_load)
            .into(image_iv)

    }

    private fun initCountDown(data: Order) {
        val createDt = data.createDt
        if (createDt.isBlank()) {
            return
        }

        // 订单创建时间一小时倒计时
        val endTime: Long = AndroidUtil.dataStringToLong("yyyy-MM-dd HH:mm:ss", createDt) + 1000 * 60 * 60
        val millisInFuture: Long = endTime - System.currentTimeMillis()
        if (millisInFuture <= 0) {
            countdown_tv.visibility = View.GONE
        } else {
            countdown_tv.apply {
                visibility = View.VISIBLE
                cancel()
                setTimeInFuture(SystemClock.elapsedRealtime() + millisInFuture)
                setAutoDisplayText(true)
                setTimeFormat(CountDownTextView.TIME_SHOW_D_H_M_S);
                setModifierText("订单有效期:");
                start()
                addCountDownCallback(object : CountDownTextView.CountDownCallback {


                    override fun onTick(countDownTextView: CountDownTextView?, millisUntilFinished: Long) = Unit

                    override fun onFinish(countDownTextView: CountDownTextView?) {
                        countdown_tv.visibility = View.GONE
                        payment_tv.visibility = View.GONE
                        RxBus.post(Event(Constants.EVENT_REFRESH_ORDER_LIST_FROM_END_COUNT_DOWN))

                    }

                })

            }
        }


    }

    private fun getImageFromMold(mold: String): Int {

        return when (mold) {
            Constants.P1 -> R.drawable.ic_image_p1
            Constants.P2 -> R.drawable.ic_image_p2
            Constants.P3 -> R.drawable.ic_image_p3
            Constants.P4 -> R.drawable.ic_image_p4
            Constants.P5 -> R.drawable.ic_image_p5
            Constants.P6 -> R.drawable.ic_image_p6
            Constants.P7 -> R.drawable.ic_image_p7
            Constants.P8 -> R.drawable.ic_image_p8
            Constants.P9 -> R.drawable.ic_image_p9
            Constants.P10 -> R.drawable.ic_image_p10
            Constants.P11 -> R.drawable.ic_image_p11
            else -> R.drawable.ic_image_p1
        }
    }


}