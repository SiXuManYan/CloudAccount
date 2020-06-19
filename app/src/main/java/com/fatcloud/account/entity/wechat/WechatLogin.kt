package com.fatcloud.account.entity.wechat

import java.util.*

/**
 * Created by Wangsw on 2020/6/18 0018 19:10.
 * </br>
 *
 */
class WechatLogin {

/*
    {
        "code" : "200",
        "msg" : "成功",
        "data" : {
        "mold" : "L0",
        "info" : "{\"access_token\":\"34_0JlJfDcj3PWkQgnfr55UG1pBdigVtuBPgOaeaEtajR84nRgmd8uLbcqVcTvV2VofiR9-CwBPQ9DtpfRR81T07Py8Fvz4aaTLam-CRhB-zNI\",\"expires_in\":7200,\"refresh_token\":\"34_WBRyRUs0goXEDqLexV3GXPQWLoNKJPiVx-wz-kVpbiAV5hOGduaChBEOPwY91vSq2wyj2WPZupXMjlzL4vR9akcCHB1rTcVmqecoe8xwH88\",\"openid\":\"oHGlZ6MA14PNBBbrBr8x0yR6j21E\",\"scope\":\"snsapi_userinfo\",\"unionid\":\"oZ3Fg1f6A-0gZB5OOaXX3dD_e0o0\"}"
                 }
    }
*/

/*
    "code" : "200",
    "msg" : "成功",
    "data" : {
        "mold" : "L1",
        "info" : {
            "token" : "01f8297c6aca41cea78bf047ef56d61f",
            "username" : "17600001112",
            "nickName" : "明天也很困",
            "headUrl" : "http://thirdwx.qlogo.cn/mmopen/vi_32/8rgx7V7WuRKPMGeTtrQEDEpdYITcLzdKRt2FxQ3OURMqHLTXlibbVP2d58o220qK5nCkG6bAvib1gsaicAiaUoDsYg/132"
        }
    }
}
    */

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

    var mold = ""
    var info: Objects? = null


}