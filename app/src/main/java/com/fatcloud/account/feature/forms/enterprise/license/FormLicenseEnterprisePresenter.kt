package com.fatcloud.account.feature.forms.enterprise.license

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.feature.forms.enterprise.license.FormLicenseEnterpriseView
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/10 0010 18:30.
 * </br>
 *
 */
class FormLicenseEnterprisePresenter @Inject constructor(private var view: FormLicenseEnterpriseView) : BasePresenter(view) {

    /**
     * 添加企业套餐
     */
    fun addEnterprise(lifecycle: LifecycleOwner) {

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.addEnterprise(),

            object : BaseHttpSubscriber<JsonObject>(view){
                override fun onSuccess(data: JsonObject?) {

                }

            }
        )
    }


}