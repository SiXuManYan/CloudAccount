package com.account.network

import com.account.BuildConfig
import com.account.common.CommonUtils
import com.account.common.Constants

object UrlUtil {

    fun getdevUrlIndex(): Int {
        var index = 0
        for (i in URL_LIST.indices) {
            if (URL_LIST[i].second.first == BuildConfig.SERVER_HOST_V2) {
                index = i
                break
            }
        }
        return CommonUtils.getShareDefault().getInt(Constants.SP_DEV_URL, index)
    }

    fun setdevUrlIndex(index: Int) {
        CommonUtils.getShareDefault().put(Constants.SP_DEV_URL, index)
    }

    val SERVER_HOST_V2 by lazy {
        if (BuildConfig.FLAVOR == "dev") {
            URL_LIST[getdevUrlIndex()].second.first
        } else {
            BuildConfig.SERVER_HOST_V2
        }
    }

    val SERVER_HOST_V3 by lazy {
        if (BuildConfig.FLAVOR == "dev") {
            URL_LIST[getdevUrlIndex()].second.second
        } else {
            BuildConfig.SERVER_HOST_V3
        }
    }

    val H5_BASE_URL by lazy {
        if (BuildConfig.FLAVOR == "dev") {
            URL_LIST[getdevUrlIndex()].second.third
        } else {
            BuildConfig.H5_BASE_URL
        }
    }
    val URL_LIST =
            listOf(

                    Pair("formal server", Triple("https://www.baidu.com", "https://www.baidu.com", "https://www.baidu.com"))
            )
}