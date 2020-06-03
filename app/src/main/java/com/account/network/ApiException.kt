package com.account.network

import com.account.common.CommonUtils
import com.account.common.Constants
import com.google.gson.internal.LinkedTreeMap

/**
 * 接口错误处理
 */
class ApiException(var code: Int, message: String?, data: LinkedTreeMap<String, String>? = null) : Exception(message) {

    var msg: String? = null

    init {
        this.msg = message ?: ""

        // 可很据code 重新定义错误信息
//        when (code) {
//            100 -> {
//                this.msg = "xxxx"
//            }
//        }
    }
}