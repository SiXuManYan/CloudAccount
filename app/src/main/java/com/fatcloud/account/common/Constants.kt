package com.fatcloud.account.common

object Constants {


    const val PR_DEFAULT = "cloud"
    const val PR_LOCATION = "location"

    const val SP_DEV_URL = "dev_url"
    const val SP_LOGIN = "login"
    const val SP_TOKEN = "token"
    const val SP_DATA = "data"
    const val SP_LOCAL_CODE = "local_code"
    const val SP_LOCAL_NAME = "local_name"
    const val SP_SELECT_LOCAL_CODE = "select_code"
    const val SP_SELECT_LOCAL_NAME = "select_name"
    const val SP_LONGITUDE = "longitude"
    const val SP_LATITUDE = "latitude"
    const val SP_ADDRESS = "address"
    const val SP_NOVICE = "novice"
    const val SP_LAST_LOGIN_USER = "last_login_user"

    const val SP_AES_LOGIN_TIME = "SP_AES_LOGIN_TIME"// 上次登录时间
    const val SP_AES_LOGIN_SERVICE_TIME = "SP_AES_LOGIN_SERVICE_TIME"// 上次登录成功时的服务器时间
    const val SP_SHOW_CITY = "sp_show_city"// 上次获取的城市首页信息
    const val SP_AUTO_PLAY_VIDEO = "sp_auto_play_video"// 是否自动播放视频

    /** 选取附件 */
    const val REQUEST_MEDIA = 1003

    /** 验证码等待时间 */
    const val WAIT_DELAYS = 59


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

    const val IMAGE_URL_FORMAT = "Android_%1s"


    const val DEVICE_ANDROID = "1"


    const val PARAM_TITLE = "param_title"

    const val PARAM_FROM_NOTIFICATION = "param_from_notification"// 页面来源于推送
    const val PARAM_NOTIFICATION_OPEN_MESSAGE = "param_notification_open_message"// 推送打开主页面tab位置
    const val PARAM_URL = "param_url"
    const val PARAM_HANDLE_BACK = "param_handle_back" // 处理返回键
    const val PARAM_WEB_REFRESH = "param_web_refresh" // 是否可刷新
    const val PARAM_WEB_LOAD_LOCAL_HTML = "param_web_load_local_html"
    const val PARAM_WEB_CHANGETITLE = "param_web_changetitle" // 自动改变标题
    const val PARAM_PRODUCT_ID = "param_product_id"// 产品ID
    const val PARAM_PRODUCT_NAME = "param_product_name"
    const val PARAM_DATE = "param_date"
    const val PARAM_TYPE = "param_type"
    const val PARAM_ID = "param_id"
    const val PARAM_ORDER_ID = "param_order_id" // 订单id
    const val PARAM_ORDER_NUMBER = "param_order_number" // 订单号
    const val PARAM_ORDER_WORK_ID = "param_order_work_id"
    const val PARAM_PRODUCT_WORK_TYPE = "param_product_work_type" // 产品类型
    const val PARAM_MOLD = "param_mold"  // 类型
    const val PARAM_ACCOUNT = "param_account"// 账号
    const val PARAM_CAPTCHA_MODE = "param_captcha_mode"// 验证码用途类型
    const val PARAM_CAPTCHA = "param_captcha"// 验证码用途类型
    const val PARAM_IS_PASSWORD_REGISTER_SET_MODE = "param_is_password_register_set_mode"// 设置密码用途类型  (true注册设置密码 false 登录后修改密码)
    const val PARAM_DATA = "param_data"
    const val PARAM_PRICE_DATA = "param_price_data"
    const val PARAM_MONEY = "param_money"
    const val PARAM_IMAGE_URL = "param_image_url"
    const val PARAM_INCOME_MONEY = "param_income_money"
    const val PARAM_FINAL_MONEY = "param_final_money"
    const val PARAM_PRODUCT_PRICE_ID = "param_product_price_id"// 产品价格类型 eg:pp1 固定价格
    const val PARAM_SELECT_PID = "param_select_pid"// 用户选中的经营范围pid
    const val PARAM_SELECT_PID_NAME = "param_select_pid_name"
    const val PARAM_NAME = "param_name"
    const val PARAM_SELECT_FORM_PID = "param_select_pid"// 用户选中的组成形式id
    const val PARAM_NAME_SELECT_FORM_PID = "param_name_select_form_pid"// 用户选中的组成形式id 对应名字
    const val PARAM_ADD_SEAL = "param_add_seal"// 办理个体户税务登记时是否额外添加了刻章业务

    const val PARAM_APP_FORCE = "param_app_force"
    const val PARAM_APP_VERSION = "param_app_version"
    const val PARAM_APP_EXPLAIN = "param_app_explain"
    const val PARAM_APP_URL = "param_app_url"


    /** RxBus Event */
    const val EVENT_STARTUP_DONE = 0x59//新手接口
    const val EVENT_FINISH_ALL = 0x60
    const val EVENT_NEED_REFRESH = 0x62// 登录登出后刷新界面登录状态发生变化
    const val EVENT_LOGOUT = 0x76// 退出登录后刷新界面
    const val EVENT_LOGIN = 0x78// 登录后刷新界面

    const val EVENT_FOLLOW_USER = 0x77//关注用户


    const val EVENT_AUTH_SUCCESS = 0x80 // 微信授权成功
    const val EVENT_AUTH_CANCEL = 0x81 // 微信授权取消
    const val EVENT_AUTH_FAIL = 0x82 // 微信授权取消
    const val EVENT_WECHAT_REGISTER = 0x83 // 微信注册成功


    const val EVENT_MORE_TRANSPARENT = 0x94
    const val EVENT_MORE_SATURATION = 0x95
    const val EVENT_PASSWORD_RESET_SUCCESS = 0x96 //密码重置成功
    const val EVENT_CLOSE_PAY = 0x97 // 关闭支付页面
    const val EVENT_WECHAT_REGISTER_CAPTCHA_SUCCESS = 0x98 // 微信注册 验证码校验通过
    const val EVENT_FORM_COMMIT_SUCCESS = 0x99 // 表单提交成功
    const val EVENT_FORM_CLOSE = 0x100 // 表单关闭，未提交
    // 列表数据 key
    const val KEY_DATA = "data"


    /**
     * 应用初始化数据
     */
    const val ACTION_SYNC = 1
    const val ACTION_SYNC_OTHER = 2
    const val ACTION_DATA_WORK = "action"

    /**
     * 客服热线
     */
    const val CONSUMER_HOT_LINE = "4007772556"

    /**
     * 身份证
     */
    const val idNumberDigits = "1234567890xX"

    // 企业股东类型
    /** 企业股东类型 企业法人*/
    const val SH1 = "SH1"

    /** 企业股东类型 监事 */
    const val SH2 = "SH2"

    /** 企业股东类型 股东*/
    const val SH3 = "SH3"


    /** 财务负责人 */
    const val SH4_N = "SH4_N"


    // 产品类型
    /** 个体户营业执照办理 */
    const val P1 = "P1"

    /** 个体户代理记账 */
    const val P3 = "P3"

    /**
     *  企业套餐  包括 营业执照办理，代理记账，税务登记，银行对公账户
     */
    const val P2 = "P2"

    /** 个体户税务登记 */
    const val P4 = "P4"

    /** 个体户营业执照变更 */
    const val P5 = "P5"

    /** 个体户营业执照注销 */
    const val P6 = "P6"

    /** 大师起名 */
    const val P7 = "P7"


    // 产品流程类型 product_word
    // PW1 营业执照办理 PW2 税务登记办理 PW3  PW4

    /** 营业执照办理 */
    const val PW1 = "PW1"

    /** 税务登记办理 */
    const val PW2 = "PW2"

    /** 银行账户办理 */
    const val PW3 = "PW3"

    /** 代理记账办理 */
    const val PW4 = "PW4"


    /**
     * 代理记账价格类型
     * 固定价格
     */
    const val PP1 = "PP1"

    /**
     * 代理记账价格类型
     * 动态计算价格 >2000万，金额 = 手动填写金额*0.1%
     */
    const val PP2 = "PP2"

    /**
     * 服务业
     * 身份正面
     * 身份证正面
     */
    const val I1 = "I1"

    /**
     * 服务业
     * 商贸企业
     * 身份反面
     * 身份证反面
     */
    const val I2 = "I2"

    /**
     * 银行账户性质 类型 AccountNature 	基本户
     */
    const val AN1 = "AN1"


    /**
     * 银行账户性质 类型 AccountNature 	一般户
     */
    const val AN2 = "AN2"

    /**
     * 银行账户性质 类型 AccountNature 	专用户
     */
    const val AN3 = "AN3"


    /**
     * 订单流程状态类型 已激活
     */
    const val OW1 = "OW1"

    /**
     * 订单流程状态类型 办理中
     */
    const val OW2 = "OW2"

    /**
     * 订单流程状态类型 已办结
     */
    const val OW3 = "OW3"

    /**
     * 订单流程状态类型 未激活
     */
    const val OW4 = "OW4"

    /** 首页Banner类型 网页H5 */
    const val B1 = "B1"

    /** 首页Banner类型 产品个体户营业执照 */
    const val B2 = "B2"

    /** 资讯 */
    const val B3 = "B3"

    /** 企业套餐 */
    const val B4 = "B4"


    /** 待支付 */
    const val OS1 = "OS1"

    /** 取消订单 */
    const val OS2 = "OS2"

    /** 支付超时 */
    const val OS3 = "OS3"

    /** 支付中 */
    const val OS4 = "OS4"

    /** 已支付 */
    const val OS5 = "OS5"

    /** 已受理 */
    const val OS6 = "OS6"

    /** 办理中 */
    const val OS7 = "OS7"

    /** 已办结 */
    const val OS8 = "OS8"

    /** 新用户 */
    const val L0 = "L0"

    /** 老用户，微信直接登陆 */
    const val L1 = "L1"

}