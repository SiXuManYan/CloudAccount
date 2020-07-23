package com.fatcloud.account.feature.forms.personal.packages.common

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.form.p8.NativeFormPersonalPackage
import com.google.gson.Gson
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/7/23 0023 10:22.
 * </br>
 * 个体户套餐
 */
class FormPersonalPackageCommonPresenter @Inject constructor(private var view: FormPersonalPackageCommonView) : BasePresenter(view) {


    private val gson = Gson()

    /**
     * 添加个体营业执照
     */
    fun addLicensePersonal(lifecycle: LifecycleOwner, model: NativeFormPersonalPackage) {

        val bodyJsonStr = gson.toJson(model)


        val jsonObject: JsonObject = gson.fromJson(bodyJsonStr, JsonObject::class.java)

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.addPersonalPackageCommon(jsonObject),
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