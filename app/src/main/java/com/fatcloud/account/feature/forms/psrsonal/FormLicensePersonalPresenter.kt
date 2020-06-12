package com.fatcloud.account.feature.forms.psrsonal

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo
import com.fatcloud.account.entity.order.persional.PersonalInfo
import com.google.gson.Gson
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/12 0012 13:33.
 * </br>
 *
 */
class FormLicensePersonalPresenter  @Inject constructor(private var view: FormLicensePersonalView) : BasePresenter(view){

    private val gson = Gson()

    /**
     * 添加企业套餐
     */
    fun addLicensePersonal(lifecycle: LifecycleOwner, enterpriseInfo: PersonalInfo) {

        val bodyJsonStr = gson.toJson(enterpriseInfo)

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.addLicensePersonal(bodyJsonStr),
            object : BaseHttpSubscriber<JsonObject>(view) {
                override fun onSuccess(data: JsonObject?) {

                }

            }
        )
    }

}