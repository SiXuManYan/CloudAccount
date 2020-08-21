package com.fatcloud.account.feature.message

import android.content.Intent
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.list.BaseRefreshListActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.message.Message
import com.fatcloud.account.feature.order.progress.ScheduleActivity
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.jude.easyrecyclerview.decoration.DividerDecoration

/**
 * Created by Wangsw on 2020/7/30 0030 16:01.
 * </br>
 *
 */
class MessageActivity : BaseRefreshListActivity<Message, MessagePresenter>(), MessageView {


    override fun getMainTitle(): Int? = R.string.message_title

    override fun initViews() {
        super.initViews()
        parent_container.setBackgroundColor(ColorUtils.getColor(R.color.color_list_gray_background))
        recyclerView.setBackgroundColor(ColorUtils.getColor(R.color.color_list_gray_background))

        CommonUtils.hasNotificationPermission(this,true)
    }

    override fun emptyMessage() = ""

    override fun getRecyclerAdapter(): RecyclerArrayAdapter<Message> {

        val adapter = object : RecyclerArrayAdapter<Message>(context) {

            override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<Message> {

                val holder = MessageHolder(parent)

                return holder
            }

        }

        adapter.setOnItemClickListener {
            val message = adapter.allData[it]
            when (message.mold) {
                Constants.NOTICE2 -> {
                    startActivity(Intent(this@MessageActivity, ScheduleActivity::class.java).putExtra(Constants.PARAM_ORDER_ID, message.businessId))
                }
                else -> {
                }
            }
        }
        return adapter
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration? {
        val itemDecoration = DividerDecoration(
            ContextCompat.getColor(context!!, R.color.color_list_gray_background),
            SizeUtils.dp2px(1f)
        )
        itemDecoration.setDrawLastItem(false)
        return itemDecoration
    }

}