package com.fatcloud.account.feature.home.adapters

import android.content.Context
import android.view.ViewGroup
import com.fatcloud.account.entity.news.News
import com.fatcloud.account.feature.home.holder.NewsHolderPlural
import com.fatcloud.account.feature.home.holder.NewsHolderSingle
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter

/**
 * Created by Wangsw on 2020/5/29 0029 16:44.
 * </br>
 * 资讯列表 adapter
 */
class NewsChildAdapter (context: Context?) : RecyclerArrayAdapter<News>(context){

    override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<News> {

        return if (viewType == 0) {
            NewsHolderSingle(parent)
        } else {
            NewsHolderPlural(parent)
        }
    }

    override fun getViewType(position: Int): Int {
        val size = allData[position].imgUrls.size
        return if (size <= 1) {
            0
        } else {
            1
        }
    }


}