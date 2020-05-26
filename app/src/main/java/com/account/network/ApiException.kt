package com.account.network

import com.account.common.CommonUtils
import com.account.common.Constants
import com.google.gson.internal.LinkedTreeMap

/**
 * 接口错误处理
 */
class ApiException(var code: Int, message: String?, data: LinkedTreeMap<String, String>? = null) :

    Exception(message) {

    override var message: String? = null

    init {
        this.message = message ?: ""
        when (code) {
            9 -> {
                this.message = "设备时间错误，请重试"

                data?.let {

                    try {
                        if (it.contains("currMillSecs")) {
                            val shareDefault = CommonUtils.getShareDefault()
                            val serverTime = it["currMillSecs"] as String
                            shareDefault.put(
                                Constants.SP_AES_LOGIN_SERVICE_TIME,
                                serverTime.toLong()
                            )
                            shareDefault.put(
                                Constants.SP_AES_LOGIN_TIME,
                                System.currentTimeMillis()
                            )
                        }
                    } catch (e: Exception) {

                    }
                }
            }
            1005 -> this.message = "登录已过期，请重新登录"


        }
    }
}