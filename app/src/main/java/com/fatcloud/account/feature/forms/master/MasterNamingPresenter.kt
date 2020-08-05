package com.fatcloud.account.feature.forms.master

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.order.master.MasterNaming
import com.google.gson.Gson
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/7/15 0015 10:04.
 * </br>
 * 大师起名
 */
class MasterNamingPresenter @Inject constructor(private var view: MasterNamingView) : BasePresenter(view) {

    private val gson = Gson()


    fun addMasterNaming(lifecycle: LifecycleOwner, model: MasterNaming) {

        val bodyJsonStr = gson.toJson(model)

        val jsonObject: JsonObject = gson.fromJson(bodyJsonStr, JsonObject::class.java)

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.addMasterNamed(jsonObject),
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