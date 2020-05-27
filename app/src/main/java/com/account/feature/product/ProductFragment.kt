package com.account.feature.product

import android.os.Handler
import android.view.View
import android.view.ViewGroup
import com.account.R
import com.account.base.ui.BaseFragment
import com.account.base.ui.list.BaseRefreshListFragment2
import com.account.entity.product.Product2
import com.account.feature.home.header.HomeHeader
import com.blankj.utilcode.util.ToastUtils
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter

/**
 * Created by Wangsw on 2020/5/25 0025 16:42.
 * </br>
 *
 */
class ProductFragment: BaseFragment<ProductPresenter>(), ServiceView {
//class ProductFragment: BaseRefreshListFragment2<Product2,ProductPresenter>(), ServiceView {

    init {
        needMenuControl = true
    }

    private val handler = Handler()
    private var clickAble = true
    private var homeHeader: HomeHeader? = null



    private fun initEvent() {


    }

    override fun getLayoutId(): Int {
        TODO("Not yet implemented")
    }

    override fun loadOnVisible() {
        TODO("Not yet implemented")
    }

    override fun initViews(parent: View) {
        TODO("Not yet implemented")
    }


}