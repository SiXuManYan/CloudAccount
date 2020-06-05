package com.fatcloud.account.feature.order.progress

import android.view.View
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.progress.BusinessProgress
import kotlinx.android.synthetic.main.activity_schedule.*
import java.util.ArrayList

/**
 * Created by Wangsw on 2020/6/4 0004 11:58.
 * </br>
 * 业务办理流程
 */
class ScheduleActivity : BaseMVPActivity<SchedulePresenter>(), ScheduleView {

    var orderId: String? = ""


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId() = R.layout.activity_schedule

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
                    card_00_cv.visibility = View.VISIBLE
                    title_00_tv.text = businessProgress.productWorkName
                    content_00_tv.text = businessProgress.productWorkIntroduce
                }
                1 -> {
                    card_01_cv.visibility = View.VISIBLE
                    title_01_tv.text = businessProgress.productWorkName
                    content_01_tv.text = businessProgress.productWorkIntroduce
                }
                2 -> {
                    card_02_cv.visibility = View.VISIBLE
                    title_02_tv.text = businessProgress.productWorkName
                    content_02_tv.text = businessProgress.productWorkIntroduce
                }
                3 -> {
                    card_03_cv.visibility = View.VISIBLE
                    title_03_tv.text = businessProgress.productWorkName
                    content_03_tv.text = businessProgress.productWorkIntroduce
                }
                else -> {
                }
            }


        }


    }


}