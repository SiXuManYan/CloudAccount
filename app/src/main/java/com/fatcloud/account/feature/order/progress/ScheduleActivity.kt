package com.fatcloud.account.feature.order.progress

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.Order
import com.fatcloud.account.entity.order.progress.BusinessProgress
import kotlinx.android.synthetic.main.activity_schedule.*
import java.util.*

/**
 * Created by Wangsw on 2020/6/4 0004 11:58.
 * </br>
 * 业务办理流程
 */
class ScheduleActivity : BaseMVPActivity<SchedulePresenter>(), ScheduleView {

    var orderId: String? = ""

    /**
     * @see Order.mold
     */
    var mold: String? = ""

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()

    override fun getLayoutId() = R.layout.activity_schedule

    override fun initViews() {
        setMainTitle(getString(R.string.business_progress_title))

        initExtra()
        presenter.getProgressData(this, orderId)
    }

    private fun initExtra() {

        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_ID)) {
            finish()
            return
        }
        orderId = intent.extras!!.getString(Constants.PARAM_ORDER_ID)
        mold = intent.extras!!.getString(Constants.PARAM_MOLD)
    }

    override fun bindProgressData(list: ArrayList<BusinessProgress>) {


        list.forEachIndexed { index, businessProgress ->
            if (index > 3) {
                return@forEachIndexed
            }
            when (index) {
                0 -> {
                    setItemData(image_00_iv, card_00_cv, title_00_tv, content_00_tv, status_00_tv, businessProgress)
                }
                1 -> {

                    setItemData(image_01_iv, card_01_cv, title_01_tv, content_01_tv, status_01_tv, businessProgress)
                }
                2 -> {

                    setItemData(image_02_iv, card_02_cv, title_02_tv, content_02_tv, status_02_tv, businessProgress)
                }
                3 -> {

                    setItemData(image_03_iv, card_03_cv, title_03_tv, content_03_tv, status_03_tv, businessProgress)
                }
                else -> {
                }
            }

        }


    }

    private fun setItemData(
        imageTag: ImageView,
        card_index_cv: CardView,
        title_index_tv: TextView,
        content_index_tv: TextView,
        status_index_tv: TextView,
        businessProgress: BusinessProgress
    ) {
        imageTag.visibility = View.VISIBLE
        card_index_cv.visibility = View.VISIBLE
        title_index_tv.text = businessProgress.productWorkName
        content_index_tv.text = businessProgress.productWorkIntroduce
        status_index_tv.text = businessProgress.stateText
    }


}