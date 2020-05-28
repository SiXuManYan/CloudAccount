package com.account.feature.product

import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.account.R
import com.account.base.ui.list.BaseRefreshListFragment2
import com.account.entity.product.Product2
import com.account.feature.home.header.HomeHeader
import com.account.feature.product.holder.ProductHolder
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.jude.easyrecyclerview.decoration.DividerDecoration

/**
 * Created by Wangsw on 2020/5/25 0025 16:42.
 * </br>
 * 资讯列表
 * 请求回来的list size < pageSize 则为最后一页
 */
class ProductFragment : BaseRefreshListFragment2<Product2, ProductPresenter>(), ProductView {

    init {
        needMenuControl = true
    }

    private val handler = Handler()
    private var clickAble = true
    private var homeHeader: HomeHeader? = null

    override fun getLayoutId() = R.layout.fragment_product

    override fun initViews(parent: View) {
        super.initViews(parent)
        initEvent()
    }


    private fun initEvent() = Unit


    override fun getRecyclerAdapter(): RecyclerArrayAdapter<Product2> {

        val adapter = object : RecyclerArrayAdapter<Product2>(context) {

            override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<Product2> {

                return ProductHolder(parent)
            }

        }

        adapter.setOnItemClickListener {
            if (!clickAble) {
                return@setOnItemClickListener
            }
            clickAble = false
            handler.postDelayed({
                clickAble = true
            }, 1000)

            // TODO 资讯点击事件
            val introduce = adapter.allData[it].introduce
            ToastUtils.showShort("资讯列表点击 = introduce:$introduce")

        }
        return adapter
    }


    override fun getItemDecoration(): RecyclerView.ItemDecoration? {
        val itemDecoration = DividerDecoration(
            ContextCompat.getColor(context!!, R.color.color_list_gray_background),
            SizeUtils.dp2px(10f)
        )
        itemDecoration.setDrawLastItem(false)
        return itemDecoration
    }

}