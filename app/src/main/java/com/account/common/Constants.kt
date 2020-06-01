package com.account.common

object Constants {


    const val PR_DEFAULT = "cloud"
    const val PR_LOCATION = "location"

    const val SP_DEV_URL = "dev_url"
    const val SP_LOGIN = "login"
    const val SP_DATA = "data"
    const val SP_LOCAL_CODE = "local_code"
    const val SP_LOCAL_NAME = "local_name"
    const val SP_SELECT_LOCAL_CODE = "select_code"
    const val SP_SELECT_LOCAL_NAME = "select_name"
    const val SP_LONGITUDE = "longitude"
    const val SP_LATITUDE = "latitude"
    const val SP_ADDRESS = "address"
    const val SP_NOVICE = "novice"

    const val SP_AES_LOGIN_TIME = "SP_AES_LOGIN_TIME"// 上次登录时间
    const val SP_AES_LOGIN_SERVICE_TIME = "SP_AES_LOGIN_SERVICE_TIME"// 上次登录成功时的服务器时间
    const val SP_SHOW_CITY = "sp_show_city"// 上次获取的城市首页信息
    const val SP_AUTO_PLAY_VIDEO = "sp_auto_play_video"// 是否自动播放视频




    /** 定位间隔 */
    const val LOCATION_INTERVAL = 300000L

    /** 防连点时间间隔 **/
    const val CLICK_INTERVAL = 1000L

    /** WebView 参数 */
    const val WEB_TITLE = "web_title"
    const val WEB_ACTION_TITLE = "web_action_title"
    const val WEB_ACTION = "web_action"
    const val WEB_URL = "web_url"
    const val WEB_FINISH = "web_finish"
    const val WEB_CONTENT = "web_content"
    const val WEB_API = "web_api"
    const val WEB_API_PARAMS = "web_params"

    const val WEB_STYLE = "<style>* {font-size:14px;line-height:20px;} p {color:#333;} a {color:#3E62A6;} " +
            "img {max-width:100%;} img.alignleft {float:left;max-width:120px;margin:0 10px 5px 0;border:1px solid #ccc;background:#fff;padding:2px;} " +
            "pre {font-size:9pt;line-height:12pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;overflow: auto;} " +
            "a.tag {font-size:15px;text-decoration:none;background-color:#bbd6f3;border-bottom:2px solid #3E6D8E;border-right:2px solid #7F9FB6;color:#284a7b;margin:2px 2px 2px 0;padding:2px 4px;white-space:nowrap;}</style>"


    const val DEVICE_ANDROID = "1"


    const val PARAM_TITLE = "param_title"

    const val PARAM_FROM_NOTIFICATION = "param_from_notification"// 页面来源于推送
    const val PARAM_NOTIFICATION_OPEN_MESSAGE = "param_notification_open_message"// 推送打开主页面tab位置
    const val PARAM_URL = "param_url"
    const val PARAM_HANDLE_BACK = "param_handle_back" // 处理返回键
    const val PARAM_WEB_REFRESH = "param_web_refresh" // 是否可刷新
    const val PARAM_WEB_CHANGETITLE = "param_web_changetitle" // 自动改变标题
    const val PARAM_PRODUCT_ID: String = "param_productId"// 产品ID
    const val PARAM_TYPE = "param_type"
    const val PARAM_ID = "param_id"


    /** RxBus Event */
    const val EVENT_STARTUP_DONE = 0x59//新手接口
    const val EVENT_FINISH_ALL = 0x60
    const val EVENT_NEED_REFRESH = 0x62//登录登出后刷新界面
    const val EVENT_LOGOUT = 0x76//登录登出后刷新界面
    const val EVENT_LOGIN = 0x78//登录后刷新界面
    const val EVENT_FOLLOW_USER = 0x77//关注用户


    const val EVENT_AUTH_SUCCESS = 0x80 // 微信授权成功
    const val EVENT_AUTH_CANCEL = 0x81 // 微信授权取消
    const val EVENT_AUTH_FAIL = 0x82 // 微信授权取消
    const val EVENT_WECHAT_REGISTER = 0x83 // 微信注册成功

    const val EVENT_MORE_TRANSPARENT = 0x94
    const val EVENT_MORE_SATURATION = 0x95

    // 列表数据 key
    const val KEY_DATA = "data"


    /**
     * 应用初始化数据
     */
    const val ACTION_SYNC = 1
    const val ACTION_DATA_WORK = "action"

    /**
     * 客服热线
     */
    const val CONSUMER_HOT_LINE = "4007772556"


}