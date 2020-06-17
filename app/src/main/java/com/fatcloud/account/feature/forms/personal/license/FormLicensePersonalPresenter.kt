package com.fatcloud.account.feature.forms.personal.license

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.order.persional.PersonalInfo
import com.google.gson.Gson
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/12 0012 13:33.
 * </br>
 *
 */
class FormLicensePersonalPresenter @Inject constructor(private var view: FormLicensePersonalView) : BasePresenter(view) {

    private val gson = Gson()

    /**
     * 添加个体营业执照
     */
    fun addLicensePersonal(lifecycle: LifecycleOwner, enterpriseInfo: PersonalInfo) {

        val bodyJsonStr = gson.toJson(enterpriseInfo)


        val example: JsonObject = gson.fromJson(bodyJsonStr, JsonObject::class.java)

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.addLicensePersonal(example),
            object : BaseHttpSubscriber<PreparePay>(view) {
                override fun onSuccess(data: PreparePay?) {

                    data?.let {
                        view.addLicensePersonalSuccess(it)
                    }

                }

            }
        )
    }

}