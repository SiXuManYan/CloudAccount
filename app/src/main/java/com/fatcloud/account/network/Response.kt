package com.fatcloud.account.network

/**
 * 接口回调
 *
 */
class Response<T> constructor() {

    constructor(code: Int?) : this() {
        this.code = code
    }


    /**
     * result code
     */
    var code: Int? = null

    /** 错误信息 */
    var msg: String? = null


    /** 接口数据 */
    var data: T? = null

    /**
     * 是否Api接口返回了错误
     */
    fun isApiError(): Boolean {
        return code != 200
    }
}