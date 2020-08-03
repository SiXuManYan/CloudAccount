package com.fatcloud.account.feature.forms.personal.packages

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.form.p9p10.NativeFormPersonalPackageP9P10
import com.google.gson.Gson
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/7/23 0023 10:22.
 * </br>
 * 个体户套餐
 */
class FormPersonalPackageP9P10Presenter @Inject constructor(private var view: FormPersonalPackageP9P10View) : BasePresenter(view) {


    private val gson = Gson()

    /**
     * 添加个体营业执照
     */
    fun addPersonalPackageP9P10(lifecycle: LifecycleOwner, model: NativeFormPersonalPackageP9P10, mold: String) {

        val bodyJsonStr = gson.toJson(model)

        val jsonObject: JsonObject = gson.fromJson(bodyJsonStr, JsonObject::class.java)



        when (mold) {
            Constants.P9 -> {
                requestApi(lifecycle, Lifecycle.Event.ON_DESTROY, apiService.addPersonalPackageCommonP9(jsonObject), object : BaseHttpSubscriber<PreparePay>(view) {
                    override fun onSuccess(data: PreparePay?) {
                        data?.let {
                            view.commitSuccess(it)
                        }

                    }
                })

            }

            Constants.P10 -> {
                requestApi(lifecycle, Lifecycle.Event.ON_DESTROY, apiService.addPersonalPackageSoleP10(jsonObject), object : BaseHttpSubscriber<PreparePay>(view) {
                    override fun onSuccess(data: PreparePay?) {
                        data?.let {
                            view.commitSuccess(it)
                        }

                    }
                })
            }

            else -> {
            }
        }



    }

}