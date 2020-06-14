package com.fatcloud.account.feature.forms.enterprise.license

import android.widget.LinearLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo
import com.fatcloud.account.entity.order.enterprise.Shareholder
import com.fatcloud.account.view.CompanyMemberEditView
import com.google.gson.Gson
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/10 0010 18:30.
 * </br>
 *
 */
class FormLicenseEnterprisePresenter @Inject constructor(private var view: FormLicenseEnterpriseView) : BasePresenter(view) {

    private val gson = Gson()

    /**
     * 添加企业套餐
     */
    fun addEnterprise(lifecycle: LifecycleOwner, enterpriseInfo: EnterpriseInfo) {

        val bodyJsonStr = gson.toJson(enterpriseInfo)

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.addEnterprise(bodyJsonStr),
            object : BaseHttpSubscriber<JsonObject>(view) {
                override fun onSuccess(data: JsonObject?) {
                    view.addEnterpriseSuccess()
                }
            }
        )
    }

    /**
     * 获取企业股东类型 集合信息
     */
    fun getShareHolders(
        shareHolder: Shareholder,
        shareHolder1: Shareholder,
        shareHolder2: Shareholder,
        shareholderMoreContainer: LinearLayout
    ): List<Shareholder> {

        val holders: ArrayList<Shareholder> = ArrayList()
        holders.add(shareHolder)
        holders.add(shareHolder1)
        holders.add(shareHolder2)

        val max = shareholderMoreContainer.childCount
        if (max > 0) {
            for (i in 0..max) {
                val companyMemberEditView = shareholderMoreContainer.getChildAt(i) as CompanyMemberEditView
                holders.add(companyMemberEditView.getShareHolder())
            }

        }


        return holders

    }


}