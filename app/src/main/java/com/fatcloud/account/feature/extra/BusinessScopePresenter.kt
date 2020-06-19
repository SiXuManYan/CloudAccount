package com.fatcloud.account.feature.extra

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.JsonUtils
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.commons.BusinessScope
import com.fatcloud.account.entity.commons.Commons
import com.fatcloud.account.feature.forms.enterprise.license.FormLicenseEnterpriseView
import com.google.gson.Gson
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/10 0010 18:30.
 * </br>
 *
 */
class BusinessScopePresenter @Inject constructor(private var view: BusinessScopeView) : BasePresenter(view) {


    fun getCommonList(lifecycleOwner: LifecycleOwner) {
        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.getCommonList(), object : BaseHttpSubscriber<Commons>(view) {
                override fun onSuccess(data: Commons?) {
                    data?.let {


                        view.receiveCommonData(data)

                    }
                }

            }
        )
    }


    /**
     * 获取经营范围pid
     */
    fun getSelectPids(allData: List<BusinessScope>?): ArrayList<String> {
        val allPid = ArrayList<String>()

        if (allData.isNullOrEmpty()) {
            return allPid
        }
        allData.forEach {
            if (it.nativeIsSelect) {
                allPid.add(it.id)
            }
        }
        return allPid
    }


    /**
     * 获取经营范围pid
     */
    fun getSelectPidNames(allData: List<BusinessScope>?): ArrayList<String> {
        val allPid = ArrayList<String>()

        if (allData.isNullOrEmpty()) {
            return allPid
        }
        allData.forEach {
            if (it.nativeIsSelect) {
                allPid.add(it.name)
            }
        }
        return allPid
    }


}