package com.fatcloud.account.feature.forms.personal.bank

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.order.persional.BankPersonal
import com.google.gson.Gson
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/7/16 0016 11:02.
 * </br>
 *
 */
class FormPersonalBankPresenter @Inject constructor(private var view: FormPersonalBankView) : BasePresenter(view) {



    private val gson = Gson()

    /**
     * 添加个体营业执照注销
     */
    fun addLicenseChangePersonal(lifecycle: LifecycleOwner, model: BankPersonal) {

        val bodyJsonStr = gson.toJson(model)

        val jsonObject: JsonObject = gson.fromJson(bodyJsonStr, JsonObject::class.java)

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.addSelfemployedBank(jsonObject),
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