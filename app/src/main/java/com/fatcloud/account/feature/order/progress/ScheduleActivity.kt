package com.fatcloud.account.feature.order.progress

import android.content.Intent
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.list.BaseRefreshListActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.progress.BusinessProgress
import com.fatcloud.account.event.entity.BankFormCommitSuccessEvent
import com.fatcloud.account.feature.forms.enterprise.bank.basic.FormBankBasicActivity
import com.fatcloud.account.feature.forms.personal.bank.basic.FormPersonalBankBasicActivity
import com.fatcloud.account.feature.order.details.bookkeeping.BookkeepingInfoActivity
import com.fatcloud.account.feature.order.details.enterprise.CompanyRegisterInfoActivity
import com.fatcloud.account.feature.order.details.master.MasterNamingInfoActivity
import com.fatcloud.account.feature.order.details.personal.bank.PersonalBankInfoActivity
import com.fatcloud.account.feature.order.details.personal.license.change.PersonalLicenseChangeInfoActivity
import com.fatcloud.account.feature.order.details.personal.license.handle.PersonalLicenseHandleInfoActivity
import com.fatcloud.account.feature.order.details.personal.license.logout.PersonalLicenseLogoutActivity
import com.fatcloud.account.feature.order.details.personal.packages.PersonalPackageInfoP9P10Activity
import com.fatcloud.account.feature.order.progress.holders.ScheduleHolder
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import io.reactivex.functions.Consumer
import java.util.*

/**
 * Created by Wangsw on 2020/6/4 0004 11:58.
 * </br>
 * 业务办理流程
 */
class ScheduleActivity : BaseRefreshListActivity<BusinessProgress, SchedulePresenter>(), ScheduleView {

    var orderId: String? = ""


    override fun getMainTitle(): Int? {
        return R.string.business_progress_title
    }


    override fun initViews() {
        super.initViews()
        easyRecyclerView.setBackgroundColor(ColorUtils.getColor(R.color.color_list_gray_background))
        swipeLayout.setEnableRefresh(false)
        initExtra()
        initEvent()
    }

    private fun initEvent() {

        // 更新当前页面
        presenter.subsribeEventEntity<BankFormCommitSuccessEvent>(Consumer {
            finish()
        })

    }

    override fun bindList(list: ArrayList<BusinessProgress>, isFirstPage: Boolean, last: Boolean) {
        super.bindList(list, isFirstPage, last)
        getAdapter()?.removeFooter(noMoreItemView)
    }

    private fun initExtra() {

        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_ID)) {
            finish()
            return
        }
        orderId = intent.extras!!.getString(Constants.PARAM_ORDER_ID)
        loadDetailData()
    }

    private fun loadDetailData() = presenter.getProgressData(this, orderId)

    override fun getRecyclerAdapter(): RecyclerArrayAdapter<BusinessProgress> {

        val adapter = object : RecyclerArrayAdapter<BusinessProgress>(context) {

            override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<BusinessProgress> {

                val holder = ScheduleHolder(parent)

                holder.itemView.findViewById<TextView>(R.id.look_detail_tv).setOnClickListener {
                    val adapter = getAdapter()
                    val adapterPosition = holder.adapterPosition
                    val model = adapter?.allData?.get(adapterPosition)
                    model?.let {
                        when (it.mold) {
                            Constants.P2 -> handleEnterpriseProduct(it)
                            Constants.P7 -> handleMasterNamingProduct(it)
                            else -> handlePersonalProduct(it)
                        }
                    }
                }
                return holder
            }

        }


        return adapter
    }

    private fun handleMasterNamingProduct(it: BusinessProgress) {
        startActivity(
            Intent(this@ScheduleActivity, MasterNamingInfoActivity::class.java)
                .putExtra(Constants.PARAM_ORDER_ID, orderId)
                .putExtra(Constants.PARAM_PRODUCT_WORK_TYPE, it.code)
        )
    }


    override fun getItemDecoration(): RecyclerView.ItemDecoration? = null


    /**
     * 个人业务
     */
    private fun handlePersonalProduct(it: BusinessProgress) {
        when (it.code) {
            Constants.PW1 -> {

                when (it.mold) {
                    Constants.P1 -> {
                        startActivity(
                            Intent(this@ScheduleActivity, PersonalLicenseHandleInfoActivity::class.java)
                                .putExtra(Constants.PARAM_ORDER_ID, orderId)
                                .putExtra(Constants.PARAM_PRODUCT_WORK_TYPE, it.code)
                        )
                    }
                    Constants.P5 -> {
                        startActivity(
                            Intent(this@ScheduleActivity, PersonalLicenseChangeInfoActivity::class.java)
                                .putExtra(Constants.PARAM_ORDER_ID, orderId)
                        )
                    }

                    Constants.P6 -> {
                        startActivity(
                            Intent(this@ScheduleActivity, PersonalLicenseLogoutActivity::class.java)
                                .putExtra(Constants.PARAM_ORDER_ID, orderId)
                        )
                    }
                    Constants.P9,
                    Constants.P10 -> {
                        startActivity(
                            Intent(this@ScheduleActivity, PersonalPackageInfoP9P10Activity::class.java)
                                .putExtra(Constants.PARAM_ORDER_ID, orderId)
                        )
                    }

                    else -> {
                    }
                }
            }
            Constants.PW2 -> {
                startActivity(
                    Intent(this@ScheduleActivity, PersonalLicenseHandleInfoActivity::class.java)
                        .putExtra(Constants.PARAM_ORDER_ID, orderId)
                        .putExtra(Constants.PARAM_PRODUCT_WORK_TYPE, it.code)
                )
            }
            Constants.PW3 -> {
                when (it.mold) {
                    Constants.P8 -> {
                        startActivity(
                            Intent(this@ScheduleActivity, PersonalBankInfoActivity::class.java)
                                .putExtra(Constants.PARAM_ORDER_ID, orderId)
                                .putExtra(Constants.PARAM_MOLD, it.mold)
                        )
                    }
                    Constants.P9,
                    Constants.P10 -> {
                        if (it.state == Constants.OW1) {
                            // 编辑
                            startActivity(
                                Intent(this, FormPersonalBankBasicActivity::class.java).putExtra(Constants.PARAM_MOLD, Constants.P9)
                                    .putExtra(Constants.PARAM_ORDER_WORK_ID, it.id)
                                    .putExtra(Constants.PARAM_MOLD, it.mold)
                            )
                        } else {
                            // 回显
                            startActivity(
                                Intent(this@ScheduleActivity, PersonalBankInfoActivity::class.java)
                                    .putExtra(Constants.PARAM_ORDER_ID, orderId)
                                    .putExtra(Constants.PARAM_ORDER_WORK_ID, it.id)
                                    .putExtra(Constants.PARAM_MOLD, it.mold)
                            )
                        }
                    }
                }
            }
            Constants.PW4 -> {
                startActivity(
                    Intent(this@ScheduleActivity, BookkeepingInfoActivity::class.java)
                        .putExtra(Constants.PARAM_ORDER_ID, orderId)
                        .putExtra(Constants.PARAM_PRODUCT_WORK_TYPE, it.code)
                )
            }

            Constants.PW99 -> {
                startActivity(
                    Intent(this@ScheduleActivity, PersonalLicenseHandleInfoActivity::class.java)
                        .putExtra(Constants.PARAM_ORDER_ID, orderId)
                        .putExtra(Constants.PARAM_PRODUCT_WORK_TYPE, it.code)
                )

            }

        }

    }

    /**
     * 企业业务
     */
    private fun handleEnterpriseProduct(it: BusinessProgress) {
        when (it.code) {
            Constants.PW1 -> {
                startActivity(
                    Intent(this@ScheduleActivity, CompanyRegisterInfoActivity::class.java)
                        .putExtra(Constants.PARAM_ORDER_WORK_ID, it.id)
                        .putExtra(Constants.PARAM_PRODUCT_WORK_TYPE, it.code)
                )
            }
            Constants.PW2 -> {
                // 企业税务登记无事件
            }
            Constants.PW3 -> {
                if (it.state == Constants.OW1) {
                    // 编辑页
                    startActivity(
                        Intent(this@ScheduleActivity, FormBankBasicActivity::class.java)
                            .putExtra(Constants.PARAM_ORDER_WORK_ID, it.id)
                            .putExtra(Constants.PARAM_PRODUCT_WORK_TYPE, it.code)
                            .putExtra(Constants.PARAM_ORDER_ID, orderId)
                    )

                } else {
                    // 回显银行信息页
                    startActivity(
                        Intent(this@ScheduleActivity, CompanyRegisterInfoActivity::class.java)
                            .putExtra(Constants.PARAM_ORDER_WORK_ID, it.id)
                            .putExtra(Constants.PARAM_PRODUCT_WORK_TYPE, it.code)
                            .putExtra(Constants.PARAM_ORDER_ID, orderId)
                    )

                }
            }
            Constants.PW4 -> {
                startActivity(
                    Intent(this@ScheduleActivity, BookkeepingInfoActivity::class.java)
                        .putExtra(Constants.PARAM_ORDER_ID, orderId)
                        .putExtra(Constants.PARAM_PRODUCT_WORK_TYPE, it.code)
                )

            }

        }
    }

}