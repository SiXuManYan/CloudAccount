package com.fatcloud.account.feature.order.details.bookkeeping

import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.persional.PersonalInfo
import kotlinx.android.synthetic.main.activity_bookkeeping_info.*

/**
 * Created by Wangsw on 2020/6/19 0019 18:23.
 * </br>
 * 代理记账回显信息页
 */
class BookkeepingInfoActivity : BaseMVPActivity<BookkeepingInfoPresenter>(), BookkeepingInfoView {


    private var orderId: String? = ""

    /**
     * 产品类型
     * PW1  营业执照办理
     * PW2  税务登记办理
     */
    private var productWorkType: String? = ""

    override fun getLayoutId(): Int = R.layout.activity_bookkeeping_info


    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()


    override fun initViews() {
        setMainTitle("订单详情")
        initExtra()


    }

    private fun initExtra() {

        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_ID)) {
            finish()
            return
        }
        orderId = intent.extras!!.getString(Constants.PARAM_ORDER_ID)
        productWorkType = intent.extras!!.getString(Constants.PARAM_PRODUCT_WORK_TYPE)
        presenter.getRegistrantInfo(this, orderId)
    }

    override fun bindDetailInfo(data: PersonalInfo) {


        when (data.state) {
            Constants.OS1 -> {
                payment_status_iv.setImageResource(R.drawable.ic_status_daizhifu)
                content_tv.text = "您的代理记账尚未支付，请前往支付"
            }
            Constants.OS2 -> {
                payment_status_iv.setImageResource(R.drawable.ic_status_daizhifu)
                content_tv.text = data.stateText
            }

            Constants.OS3 -> {
                payment_status_iv.setImageResource(R.drawable.ic_status_dingdanshixiao)
                content_tv.text = "您的代理记账支付已超时"
            }

            Constants.OS4 -> {
                payment_status_iv.setImageResource(R.drawable.ic_status_zhifuzhong)
                content_tv.text = "您的代理记账正在支付中，请前往支付"
            }

            Constants.OS5 -> {
                payment_status_iv.setImageResource(R.drawable.ic_status_banlizhong)
                content_tv.text = data.stateText
            }

            Constants.OS6 -> {
                payment_status_iv.setImageResource(R.drawable.ic_status_banlizhong)
                content_tv.text = data.stateText
            }

            Constants.OS7 -> {
                payment_status_iv.setImageResource(R.drawable.ic_status_banlizhong)
                content_tv.text = "您的代理记账正在办理中，请耐心等待"
            }
            Constants.OS8 -> {
                payment_status_iv.setImageResource(R.drawable.ic_status_yibanjie)
                content_tv.text = data.stateText
            }
            else -> {
                payment_status_iv.setImageResource(R.drawable.ic_status_banlizhong)
                content_tv.text = data.stateText
            }
        }


    }

}