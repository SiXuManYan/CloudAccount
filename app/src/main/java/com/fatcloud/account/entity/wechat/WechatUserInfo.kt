package com.fatcloud.account.entity.wechat

/**
 * Created by Wangsw on 2020/6/18 0018 21:16.
 * </br>
 *
 */
class WechatUserInfo {


/*
    {
        "openid": "OPENID",
        "nickname": "NICKNAME",
        "sex": 1,
        "province": "PROVINCE",
        "city": "CITY",
        "country": "COUNTRY",
        "headimgurl": "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
        "privilege": ["PRIVILEGE1", "PRIVILEGE2"],
        "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"
    }


{
    "openid":"oHGlZ6MA14PNBBbrBr8x0yR6j21E",
    "nickname":"明天也很困",
    "sex":1,
    "language":"zh_CN",
    "city":"",
    "province":"",
    "country":"KP",
    "headimgurl":"http://thirdwx.qlogo.cn/mmopen/vi_32/8rgx7V7WuRKPMGeTtrQEDEpdYITcLzdKRt2FxQ3OURMqHLTXlibbVP2d58o220qK5nCkG6bAvib1gsaicAiaUoDsYg/132",
    "privilege":[

    ],
    "unionid":"oZ3Fg1f6A-0gZB5OOaXX3dD_e0o0"
}

    */

    var openid: String = ""
    var nickname: String = ""
    var sex: Int = 0
    var province: String = ""
    var city: String = ""
    var country: String = ""
    var headimgurl: String = ""
    var privilege: List<String> = ArrayList()


    var unionid: String = ""


}