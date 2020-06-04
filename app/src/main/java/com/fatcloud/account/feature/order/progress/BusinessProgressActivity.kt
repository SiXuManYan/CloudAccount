package com.fatcloud.account.feature.order.progress

import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.progress.BusinessProgress
import kotlinx.android.synthetic.main.item_business_progress.*
import java.util.ArrayList

/**
 * Created by Wangsw on 2020/6/4 0004 11:58.
 * </br>
 * （企业套餐）业务办理进度
 */
class BusinessProgressActivity : BaseMVPActivity<BusinessProgressPresenter>(), BusinessProgressView {

    var orderId: String? = ""


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId() = R.layout.item_business_progress

    override fun initViews() {
        setMainTitle(getString(R.string.business_progress_title))

        initExtra()
        presenter.getProgressData(this, orderId)
    }

    private fun initExtra() {

        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ID)) {
            finish()
            return
        }
        orderId = intent.extras!!.getString(Constants.PARAM_ID)
    }

    override fun bindProgressData(list: ArrayList<BusinessProgress>) {
        if (list.size < 4) {
            return
        }

        list.forEachIndexed { index, businessProgress ->
            if (index > 3) {
                return@forEachIndexed
            }
            when (index) {
                0 -> {
                    title_00_tv.text = businessProgress.name
                    content_00_tv.text = businessProgress.moldText
                }
                1 -> {
                    title_01_tv.text = businessProgress.name
                    content_01_tv.text = businessProgress.moldText
                }
                2 -> {
                    title_02_tv.text = businessProgress.name
                    content_02_tv.text = businessProgress.moldText
                }
                3 -> {
                    title_03_tv.text = businessProgress.name
                    content_03_tv.text = businessProgress.moldText
                }
                else -> {
                }
            }


        }


    }


}