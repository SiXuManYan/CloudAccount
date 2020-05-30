package com.account.network

import com.account.BuildConfig
import com.account.common.CommonUtils
import com.account.common.Constants

object UrlUtil {

    fun getdevUrlIndex(): Int {
        var index = 0
        for (i in URL_LIST.indices) {
            if (URL_LIST[i].second.first == BuildConfig.SERVER_HOST) {
                index = i
                break
            }
        }
        return CommonUtils.getShareDefault().getInt(Constants.SP_DEV_URL, index)
    }

    fun setdevUrlIndex(index: Int) {
        CommonUtils.getShareDefault().put(Constants.SP_DEV_URL, index)
    }



    val SERVER_HOST by lazy {
        if (BuildConfig.FLAVOR == "dev") {
            URL_LIST[getdevUrlIndex()].second.first
        } else {
            BuildConfig.SERVER_HOST
        }
    }

    val H5_BASE_URL by lazy {
        if (BuildConfig.FLAVOR == "dev") {
            URL_LIST[getdevUrlIndex()].second.second
        } else {
            BuildConfig.H5_BASE_URL
        }
    }
    val URL_LIST =
            listOf(
                    Pair("测试服", Pair("http://192.168.1.191:8881", "https://www.baidu.com")),
                    Pair("正式服", Pair("http://192.168.1.191:8881", "https://www.baidu.com"))
            )
}