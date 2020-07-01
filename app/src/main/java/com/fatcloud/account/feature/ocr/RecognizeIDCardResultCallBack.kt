package com.fatcloud.account.feature.ocr

import com.baidu.ocr.sdk.model.IDCardResult

/**
 * Created by Wangsw on 2020/7/1 0001 14:07.
 * </br>
 * 身份证扫描回调
 */
interface RecognizeIDCardResultCallBack {


    fun onResult(result: IDCardResult)
}