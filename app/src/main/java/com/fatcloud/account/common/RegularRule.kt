package com.fatcloud.account.common

/**
 * Created by Wangsw on 2020/6/20 0020 0:50.
 * </br>
 * https://github.com/any86/any-rule
 * 应用内正则
 */
object RegularRule {

    /**
     * 身份证号
     */
    const val IDENTITY_NUMBER = "/(^\\d{8}(0\\d|10|11|12)([0-2]\\d|30|31)\\d{3}\$)|(^\\d{6}(18|19|20)\\d{2}(0[1-9]|10|11|12)([0-2]\\d|30|31)\\d{3}(\\d|X|x)\$)/"

}