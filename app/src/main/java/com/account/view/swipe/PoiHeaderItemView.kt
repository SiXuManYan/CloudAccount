package com.jz.yihua.activity.view.swipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.jude.easyrecyclerview.decoration.SpaceDecoration


class PoiHeaderItemView{
//    : RecyclerArrayAdapter.ItemView {
    /*private lateinit var vPoiInfoLine: View
    private lateinit var tvPoiName: TextView
    private lateinit var tvPoiPerMoney: TextView
    private lateinit var tvPoiSquare: TextView
    private lateinit var tvPoiFeedCount: TextView
    private lateinit var tvPoiRecommendCount: TextView
    private lateinit var tvPoiRecommendList: androidx.recyclerview.widget.RecyclerView
    private lateinit var  adapter: RecyclerArrayAdapter<UserInfo>
    override fun onBindView(headerView: View?) {
    }

    override fun onCreateView(parent: ViewGroup?): View {
        return LayoutInflater.from(parent!!.context).inflate(R.layout.view_poi_header, parent, false).apply {
            vPoiInfoLine = findViewById(R.id.v_poi_info_line)
            tvPoiName = findViewById(R.id.tv_poi_name)
            tvPoiPerMoney = findViewById(R.id.tv_poi_perMoney)
            tvPoiSquare = findViewById(R.id.tv_poi_square)
            tvPoiRecommendCount = findViewById(R.id.tv_poi_recommend_count)
            tvPoiFeedCount = findViewById(R.id.tv_poi_feed_count)

            tvPoiRecommendList = findViewById(R.id.rv_poi_recommend_list)
            tvPoiRecommendList.layoutManager= androidx.recyclerview.widget.LinearLayoutManager(context).apply { orientation = androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL }
            val spaceDecoration = SpaceDecoration(SizeUtils.dp2px(8f))
            spaceDecoration.setPaddingEdgeSide(false)
            spaceDecoration.setPaddingHeaderFooter(false)
            spaceDecoration.setPaddingStart(false)
            tvPoiRecommendList.addItemDecoration(spaceDecoration)
            adapter = object : RecyclerArrayAdapter<UserInfo>(context) {
                override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<UserInfo> {
                    val iv = ImageView(context)
                    iv.layoutParams = ViewGroup.LayoutParams(SizeUtils.dp2px(26f), SizeUtils.dp2px(26f))
                    return HeadHolder(iv)
                }
            }
            adapter.setOnItemClickListener {
                if (adapter.allData[it] != null && adapter.allData[it].storeId > 0) {
                    context.startActivity<StoreDetailActivity>(Constants.PARAM_STORE_ID to adapter.allData[it].storeId)
                } else {
                    context.startActivity<ProfileActivity>(Constants.PARAM_YIHUA_ID to adapter.allData[it].yihuaId)
                }
            }
            tvPoiRecommendList.adapter = adapter
        }
    }

    fun setData(info: PoiInfo) {
        tvPoiName.text = info.poiName
        info.perMoney?.let { tvPoiPerMoney.text = tvPoiPerMoney.context.getString(R.string.poi_per_money, it) }
        info.square?.let { tvPoiSquare.text = it }

        vPoiInfoLine.visibility = if (!info.perMoney.isNullOrEmpty() && !info.square.isNullOrEmpty()) View.VISIBLE else View.GONE
        (vPoiInfoLine.parent as View).visibility = if (!info.perMoney.isNullOrEmpty() || !info.square.isNullOrEmpty()) View.VISIBLE else View.GONE
        tvPoiFeedCount.text = tvPoiFeedCount.context.getString(R.string.poi_feed_count, info.feedCount.toString())


        if(info.poiRecommendDto.userInfoList.isNullOrEmpty()){
            ( tvPoiRecommendCount.parent as View).visibility = View.GONE
        }else {
            ( tvPoiRecommendCount.parent as View).visibility = View.VISIBLE
            tvPoiRecommendCount.text = tvPoiRecommendCount.context.getString(R.string.poi_recommend_count, info.poiRecommendDto.followNumber.toString())
            adapter.clear()
            adapter.addAll(info.poiRecommendDto.userInfoList.filterIndexed { index, _ -> index<4 })
        }
    }

    *//**
     * 头像列表项
     *//*
    private class HeadHolder(view: View) : BaseItemViewHolder<UserInfo>(view) {

        override fun setData(data: UserInfo?) {
            val imageView = itemView as ImageView
            Glide.with(context!!).load(data?.userPhoto*//*.plus(CommonUtils.getThumbnailUriBySize(360))*//*)
                    .apply(RequestOptions().placeholder(R.drawable.ic_avatar_circle).error(R.drawable.ic_avatar_circle).circleCrop())
                    .into(imageView)
        }
    }*/
}