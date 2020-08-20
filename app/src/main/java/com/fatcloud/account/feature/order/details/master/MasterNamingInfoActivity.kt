package com.fatcloud.account.feature.order.details.master

import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.order.detail.MasterNamingDetail
import kotlinx.android.synthetic.main.activity_master_naming_info.*

/**
 * Created by Wangsw on 2020/8/20 0020 13:25.
 * </br>
 *  大师起名回显
 */
class MasterNamingInfoActivity : BaseMVPActivity<MasterNamingInfoPresenter>(), MasterNamingInfoView{



    /**
     * 订单id，
     * 当为 产品流程类型为：PW3银行账户办理时，tOrderWork/detail 接口不返回相关法人信息
     * 需要使用订单id调用  tOrder/detail 接口请求法人股东信息
     *
     */
    private var orderId: String? = ""


    override fun getLayoutId() = R.layout.activity_master_naming_info


    private fun initExtra() {
        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_ORDER_ID)) {
            finish()
            return
        }
        orderId = intent.extras!!.getString(Constants.PARAM_ORDER_ID)
    }


    override fun initViews() {
        initExtra()

        setMainTitle("办理信息")
        presenter.getDetailInfo(this, orderId)
    }

    override fun bindDetailInfo(data: MasterNamingDetail) {
        ProductUtils.setPaymentStatus(data.state, data.stateText, payment_status_iv, payment_status_tv)
        name_tv.text = data.name
        phone_tv.text = data.phone
        birthday_tv.text = data.birthday
        entity_tv.text = data.businessEntity
        business_scope_tv.text = data.businessScopeNames
        product_tv.text = data.businessProduct
        remark_tv.text = data.remark
    }
}