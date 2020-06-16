package com.fatcloud.account.backstage

import android.text.TextUtils
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.base.common.BaseNoDaggerPresenter
import com.fatcloud.account.base.net.BaseJsonArrayHttpSubscriber
import com.fatcloud.account.data.CloudDataBase
import com.fatcloud.account.entity.news.NewsCategory
import com.blankj.utilcode.util.Utils
import com.fatcloud.account.BuildConfig
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.commons.Commons
import com.fatcloud.account.entity.upgrade.Upgrade
import com.google.gson.JsonArray
import java.util.*

class DataWorkPresenter constructor(private var serviceView: ServiceView) : BaseNoDaggerPresenter(serviceView) {


    val database: CloudDataBase by lazy { (Utils.getApp() as CloudAccountApplication).database }


    /**
     * 获取资讯分类并存储db
     */
    fun getNewsCategoryToDataBase() {
        addSubscribe(
            apiService.getNewsCategory()
                .compose(flowableUICompose())
                .subscribeWith(object : BaseJsonArrayHttpSubscriber<NewsCategory>(serviceView) {
                    override fun onSuccess(jsonArray: JsonArray?, list: ArrayList<NewsCategory>, lastItemId: String?) {
                        if (list.isNotEmpty()) database.newsCategoryDao().addAllServiceSubTypes(list)
                    }
                })
        )
    }


    /**
     * 检查app版本号
     */
    fun checkAppVersion() {
        addSubscribe(
            apiService.checkAppVersion()
                .compose(flowableUICompose())
                .subscribeWith(object : BaseHttpSubscriber<Upgrade>(serviceView) {
                    override fun onSuccess(data: Upgrade?) {

                        data?.let {
                            if (data.version > BuildConfig.VERSION_CODE && data.appUrl.isNotEmpty()) {
                                serviceView.doAppUpgrade(data)
                            }
                        }
                    }

                })
        )

    }


}