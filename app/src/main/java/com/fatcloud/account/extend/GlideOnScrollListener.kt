package com.fatcloud.account.extend

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.fatcloud.account.app.Glide

/**
 * Created by Wangsw on 2020/05/26 13:55.
 * </br>
 * 列表滑动监听
 */
class GlideOnScrollListener constructor(private var context: Context) : RecyclerView.OnScrollListener() {


    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        when (newState) {
            RecyclerView.SCROLL_STATE_DRAGGING,
            RecyclerView.SCROLL_STATE_SETTLING -> {
                Glide.with(context).pauseRequests();
            }
            RecyclerView.SCROLL_STATE_IDLE -> {
                Glide.with(context).resumeRequests();
            }
            else -> {
            }
        }

    }

}