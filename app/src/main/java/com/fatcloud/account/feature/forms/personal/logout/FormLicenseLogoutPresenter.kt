package com.fatcloud.account.feature.forms.personal.logout

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.order.persional.PersonalLicenseChange
import com.fatcloud.account.entity.order.persional.PersonalLicenseLogout
import com.google.gson.Gson
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/7/13 0013 16:38.
 * </br>
 *
 */
class FormLicenseLogoutPresenter  @Inject constructor(private var view: FormLicenseLogoutView) : BasePresenter(view) {
    private val gson = Gson()

    /**
     * 添加个体营业执照注销
     */
    fun addLicenseChangePersonal(lifecycle: LifecycleOwner, enterpriseInfo: PersonalLicenseLogout) {

        val bodyJsonStr = gson.toJson(enterpriseInfo)

        val jsonObject: JsonObject = gson.fromJson(bodyJsonStr, JsonObject::class.java)

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.addLicensePersonalLogout(jsonObject),
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