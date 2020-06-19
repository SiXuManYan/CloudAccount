package com.fatcloud.account.entity.wechat

import java.io.Serializable

/**
 * Created by Wangsw on 2020/6/18 0018 20:36.
 * </br>
 *
 */
class WechatAuthInfo : Serializable {

/*
    {

        "access_token":"34_0JlJfDcj3PWkQgnfr55UG1pBdigVtuBPgOaeaEtajR84nRgmd8uLbcqVcTvV2VofiR9-CwBPQ9DtpfRR81T07Py8Fvz4aaTLam-CRhB-zNI",
        "expires_in":7200,
        "refresh_token":"34_WBRyRUs0goXEDqLexV3GXPQWLoNKJPiVx-wz-kVpbiAV5hOGduaChBEOPwY91vSq2wyj2WPZupXMjlzL4vR9akcCHB1rTcVmqecoe8xwH88",
        "openid":"oHGlZ6MA14PNBBbrBr8x0yR6j21E",
        "scope":"snsapi_userinfo",
        "unionid":"oZ3Fg1f6A-0gZB5OOaXX3dD_e0o0"
    }
    */

    var access_token: String = ""
    var expires_in: Int = 0
    var refresh_token: String = ""
    var openid: String = ""
    var scope: String = ""
    var unionid: String = ""

}