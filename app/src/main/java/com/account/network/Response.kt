package com.account.network

/**
 * 接口回调
 * @date 2018/11/2
 */
class Response<T> constructor() {

    constructor(code: Int?, resultCode: String?) : this() {
        this.code = code
        this.resultCode = resultCode
    }

    /** V2接口 */
    var resultCode: String? = null
    /** V3接口 */
    var code: Int? = null
    /** 错误信息 */
    var message: String? = null
    /** 接口数据 */
    var data: T? = null

    /**
     * 是否Api接口返回了错误
     */
    fun isApiError(): Boolean {
        return if (resultCode.isNullOrEmpty()) {
            !(code == 200 || code == 100306001 || code==100209048)
        } else {
            !(resultCode == "1001" || resultCode == "1009" || resultCode == "3501" || resultCode == "3502" || resultCode == "3601" || resultCode == "3602" || resultCode == "3904"||resultCode == "1403")
        }
    }
}