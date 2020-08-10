package com.fatcloud.account.feature.order.details.enterprise

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo
import com.fatcloud.account.entity.order.enterprise.Shareholder
import com.fatcloud.account.entity.order.persional.PersonalInfo
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/4 0004 14:03.
 * </br>
 *  公司详情信息
 */
class CompanyRegisterInfoPresenter @Inject constructor(private var companyRegisterInfoView: CompanyRegisterInfoView) :
    BasePresenter(companyRegisterInfoView) {


    fun getEnterpriseInfo(
        lifecycleOwner: LifecycleOwner,
        orderWorkId: String?,
        productWorkType: String?,
        orderId: String?
    ) {
        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.getEnterpriseOrderDetail(orderWorkId),

            object : BaseHttpSubscriber<EnterpriseInfo>(companyRegisterInfoView) {


                override fun onSuccess(data: EnterpriseInfo?) {

                    var financeShareholder: Shareholder? = null

                    data?.let {
                        companyRegisterInfoView.bindDetailInfo(it)

                        // 为银行时回显时获取 额外请求接口财务负责人信息
                        if (productWorkType == Constants.PW3) {

                            financeShareholder = Shareholder(
                                idno = CommonUtils.convertString(it.financeIdno),
                                idnoAddr = "",
                                imgs = ArrayList<IdentityImg>().apply {
                                    add(IdentityImg(imgUrl = CommonUtils.convertString(it.financeIdnoImgUrlA), mold = Constants.I1))
                                    add(IdentityImg(imgUrl = CommonUtils.convertString(it.financeIdnoImgUrlB), mold = Constants.I2))
                                },
                                mold = Constants.SH4,
                                name = CommonUtils.convertString(it.financeName),
                                phone = CommonUtils.convertString(it.financePhone),
                                idnoDate = "",
                                shareProportion = CommonUtils.convertString(it.financeShares)
                            )

                        }

                    }


                    if (productWorkType == Constants.PW3) {
                        // 银行对公账户时，获取额外信息
                        getRegistrantInfo(lifecycleOwner, orderId, financeShareholder)
                    }


                }
            }
        )


    }


    /**
     * 银行对公账户时，获取额外信息
     */
    fun getRegistrantInfo(
        lifecycle: LifecycleOwner,
        orderId: String?,
        financeShareholder: Shareholder?
    ) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getPersonalOrderDetail(orderId),

            object : BaseHttpSubscriber<PersonalInfo>(companyRegisterInfoView) {

                override fun onSuccess(data: PersonalInfo?) {

                    if (data == null) {
                        return
                    }

                    //  添加财务负责人
                    financeShareholder?.let {
                        if (data.shareholders.isNullOrEmpty()) {
                            data.shareholders = ArrayList()
                        }
                        data.shareholders?.add(0, it)
                    }
                    companyRegisterInfoView.bindShareholdersInfo(data)

                }
            }
        )

    }


}