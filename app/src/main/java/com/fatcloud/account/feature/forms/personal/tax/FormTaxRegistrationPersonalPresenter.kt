package com.fatcloud.account.feature.forms.personal.tax

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.defray.prepare.PreparePay
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/13 0013 13:46.
 * </br>
 *
 */
class FormTaxRegistrationPersonalPresenter @Inject constructor(private var view: FormTaxRegistrationPersonalView) : BasePresenter(view) {


    /**
     * 添加个体户税务登记
     */
    fun addLicensePersonal(
        lifecycle: LifecycleOwner,
        money: String?,
        productId: String?,
        productPriceId: String?,
        taxpayerNo: String?,
        legalPersonName: String?,
        idno: String?,
        bankNo: String?,
        phoneOfBank: String?,
        businessLicenseImgUrl: String?,
        addr: String?,
        area: String
    ) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.addTaxRegistration(
                money,
                productId,
                productPriceId,
                taxpayerNo,
                legalPersonName,
                idno,
                bankNo,
                phoneOfBank,
                businessLicenseImgUrl,
                addr,
                area
            ),
            object : BaseHttpSubscriber<PreparePay>(view) {
                override fun onSuccess(data: PreparePay?) {
                    data?.let {
                        view.commitSuccess(it)
                    }
                }

            }
        )
    }


}