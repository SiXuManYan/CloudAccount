package com.fatcloud.account.feature.extra

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.VibrateUtils
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.commons.BusinessScope
import com.fatcloud.account.entity.commons.Commons
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import kotlinx.android.synthetic.main.activity_business_scope.*

/**
 * Created by Wangsw on 2020/6/11 0010 18:30.
 * </br>
 *  经营范围
 */
class BusinessScopeActivity : BaseMVPActivity<BusinessScopePresenter>(), BusinessScopeView {


    private var firstAdapter: RecyclerArrayAdapter<BusinessScope>? = null
    private var secondAdapter: RecyclerArrayAdapter<BusinessScope>? = null

    /**
     * 产品类型
     * P1 P2 ....
     */
    private var mProductType: String = ""

    override fun getLayoutId() = R.layout.activity_business_scope

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun initViews() {
        initExtra()
        initView()
    }

    private fun initExtra() {
        mProductType = intent.getStringExtra(Constants.PARAM_PRODUCT_TYPE)

    }


    private fun initView() {
        setMainTitle("经营范围")
        setTextActionButton(R.string.confirm, View.OnClickListener { finish() })
        val loudAccountApplication = application as CloudAccountApplication
        val businessScope = loudAccountApplication.commonData?.businessScope

        if (businessScope.isNullOrEmpty() || businessScope[0].childs.isNullOrEmpty()) {
            presenter.getCommonList(this)
            return
        }
        initRecyclerView(businessScope)
    }

    private fun initRecyclerView(businessScope: ArrayList<BusinessScope>?) {
        firstAdapter = getFirstRecyclerAdapter().apply {
            addAll(businessScope)
        }
        first_rv.adapter = firstAdapter

        if (mProductType.isBlank() || mProductType == Constants.P2) {
            // 企业套餐显示二级列表
            secondAdapter = getSecondRecyclerAdapter()
            second_rv.adapter = secondAdapter
            second_rv.visibility = View.VISIBLE
        } else {
            second_rv.visibility = View.GONE
        }
    }


    private fun getFirstRecyclerAdapter(): RecyclerArrayAdapter<BusinessScope> {

        val adapter = object : RecyclerArrayAdapter<BusinessScope>(context) {
            override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<BusinessScope> = BusinessScopeHolder(parent)
        }

        adapter.setOnItemClickListener {

            val businessScope = adapter.allData[it]
            val nativeIsSelect = businessScope.nativeIsSelect

            if (!nativeIsSelect) {
                VibrateUtils.vibrate(10)
            }

            businessScope.nativeIsSelect = !nativeIsSelect
            adapter.notifyItemChanged(it)

            if (mProductType.isBlank() || mProductType == Constants.P2) {
                // 企业套餐才显示二级列表
                performFirstAdapterClick(businessScope.childs, businessScope.nativeIsSelect)
            }


        }

        return adapter
    }

    private fun performFirstAdapterClick(businessScope: List<BusinessScope>, firstIsSelect: Boolean) {

        secondAdapter?.apply {
            clear()


            businessScope.forEach {

                it.nativeIsSelect = firstIsSelect

            }

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

            if (!nativeIsSelect) {
                VibrateUtils.vibrate(10)
            }

            businessScope.nativeIsSelect = !nativeIsSelect

            adapter.notifyItemChanged(it)
        }
        return adapter
    }


    override fun finish() {
        val selectPids = presenter.getSelectPids(firstAdapter?.allData)
        val selectPidNames = presenter.getSelectPidNames(firstAdapter?.allData)
        if (selectPids.isNotEmpty() && selectPidNames.isNotEmpty()) {
            setResult(
                0,
                Intent().putStringArrayListExtra(Constants.PARAM_SELECT_PID, selectPids)
                    .putStringArrayListExtra(Constants.PARAM_SELECT_PID_NAME, selectPidNames)
            )
        }

        clearSelect()
        super.finish()
    }

    /**
     * 恢复数据初始状态
     * 一级列表清空选中，
     * 默认二级列表全部选中
     */
    private fun clearSelect() {
        val loudAccountApplication = application as CloudAccountApplication
        val businessScope = loudAccountApplication.commonData?.businessScope
        businessScope?.let {
            it.forEachIndexed { _, businessScope ->

                if (businessScope.nativeIsSelect) {
                    businessScope.nativeIsSelect = false

                    businessScope.childs.forEachIndexed { _, child ->
                        if (child.nativeIsSelect) {
                            child.nativeIsSelect = true
                        }
                    }

                }

            }

        }

    }

    override fun receiveCommonData(data: Commons) {
        val application = application as CloudAccountApplication
        application.receiveCommonData(data)
        initRecyclerView(data.businessScope)
    }


}