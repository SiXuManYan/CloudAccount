package com.fatcloud.account.feature.forms.personal.change

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.order.persional.PersonalLicenseChange
import com.google.gson.Gson
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/7/10 0010 15:37.
 * </br>
 *
 */
class FormLicenseChangePresenter @Inject constructor(private var view: FormLicenseChangeView) : BasePresenter(view) {

    private val gson = Gson()

    /**
     * 添加个体营业执照
     */
    fun addLicenseChangePersonal(lifecycle: LifecycleOwner, enterpriseInfo: PersonalLicenseChange) {

        val bodyJsonStr = gson.toJson(enterpriseInfo)

        val jsonObject: JsonObject = gson.fromJson(bodyJsonStr, JsonObject::class.java)

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.addLicensePersonalChange(jsonObject),
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