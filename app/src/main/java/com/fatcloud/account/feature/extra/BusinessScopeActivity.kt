package com.fatcloud.account.feature.extra

import android.content.Intent
import android.view.ViewGroup
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.commons.BusinessScope
import com.fatcloud.account.event.RxBus
import com.fatcloud.account.event.entity.BusinessScopeEvent
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import kotlinx.android.synthetic.main.activity_business_scope.*

/**
 * Created by Wangsw on 2020/6/11 0010 18:30.
 * </br>
 *  经营范围
 */
class BusinessScopeActivity() : BaseMVPActivity<BusinessScopePresenter>(), BusinessScopeView {


    private var firstAdapter: RecyclerArrayAdapter<BusinessScope>? = null
    private var secondAdapter: RecyclerArrayAdapter<BusinessScope>? = null

    override fun getLayoutId() = R.layout.activity_business_scope

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun initViews() {

        initView()
    }


    private fun initView() {
        setMainTitle("经营范围")
        val loudAccountApplication = application as CloudAccountApplication
        val businessScope = loudAccountApplication.commonData?.businessScope

        if (businessScope.isNullOrEmpty() || businessScope[0].childs.isNullOrEmpty()) {
            return
        }
        firstAdapter = getFirstRecyclerAdapter().apply {
            addAll(businessScope)
        }
        secondAdapter = getSecondRecyclerAdapter()
        performFirstAdapterClick(businessScope[0].childs)
        first_rv.adapter = firstAdapter
        second_rv.adapter = secondAdapter
    }


    private fun getFirstRecyclerAdapter(): RecyclerArrayAdapter<BusinessScope> {

        val adapter = object : RecyclerArrayAdapter<BusinessScope>(context) {
            override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<BusinessScope> = BusinessScopeHolder(parent)
        }

        adapter.setOnItemClickListener {
            val businessScope = adapter.allData[it]

            val nativeIsSelect = businessScope.nativeIsSelect
            businessScope.nativeIsSelect = !nativeIsSelect

            adapter.notifyItemChanged(it)

            performFirstAdapterClick(businessScope.childs)
        }

        return adapter
    }

    private fun performFirstAdapterClick(businessScope: List<BusinessScope>) {
        secondAdapter?.apply {
            clear()
            addAll(businessScope)
        }
    }


    private fun getSecondRecyclerAdapter(): RecyclerArrayAdapter<BusinessScope> {

        val adapter = object : RecyclerArrayAdapter<BusinessScope>(context) {

            override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<BusinessScope> = BusinessScopeHolder(parent)
        }
        adapter.setOnItemClickListener {
            val businessScope = adapter.allData[it]
            val nativeIsSelect = businessScope.nativeIsSelect
            businessScope.nativeIsSelect = !nativeIsSelect

            adapter.notifyItemChanged(it)
        }
        return adapter
    }


    override fun finish() {
        val selectPids = presenter.getSelectPids(firstAdapter?.allData)
        val selectPidNames = presenter.getSelectPidNames(firstAdapter?.allData)
        setResult(
            0,
            Intent()
                .putStringArrayListExtra(Constants.PARAM_SELECT_PID, selectPids)
                .putStringArrayListExtra(Constants.PARAM_SELECT_PID_NAME, selectPidNames)
        )


        super.finish()
    }


}