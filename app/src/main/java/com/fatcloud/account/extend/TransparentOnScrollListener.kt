package com.fatcloud.account.extend

import androidx.recyclerview.widget.RecyclerView
import com.fatcloud.account.common.Constants
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.RxBus

/**
 * Created by Wangsw on 2020/05/26 13:55.
 * </br>
 * 列表滑动监听
 */
class TransparentOnScrollListener : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        when (newState) {
            RecyclerView.SCROLL_STATE_DRAGGING -> RxBus.post(Event(Constants.EVENT_MORE_TRANSPARENT))
            RecyclerView.SCROLL_STATE_IDLE -> RxBus.post(Event(Constants.EVENT_MORE_SATURATION))
            else -> {
            }
        }

    }

}