package com.fatcloud.account.feature.extra

import com.blankj.utilcode.util.JsonUtils
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.entity.commons.BusinessScope
import com.fatcloud.account.feature.forms.enterprise.license.FormLicenseEnterpriseView
import com.google.gson.Gson
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/10 0010 18:30.
 * </br>
 *
 */
class BusinessScopePresenter @Inject constructor(private var view: BusinessScopeView) : BasePresenter(view) {


    fun getSelectPids(allData: List<BusinessScope>?): ArrayList<String> {
        val allPid = ArrayList<String>()

        if (allData.isNullOrEmpty()) {
            return allPid
        }

        allData.forEach {
            if (it.nativeIsSelect) {
                allPid.add(it.pid)
            }
        }

        return allPid
    }


}