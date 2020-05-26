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
    const val SP_POST_TIP = "post_tip"
    const val SP_POST_TIP_CLOSE = "post_tip_close"
    const val SP_POST_MIN = "post_min"
    const val SP_POST_ENABLE = "post_enable"
    const val SP_POST_CACHE = "post_cache"
    const val SP_SIGN_RULES = "show_rules"
    const val SP_SOUND_ENABLE = "sound_enable"
    const val SP_NOTIFICATION_ENABLE = "notification_enable"
    const val SP_LAST_FROM_YIHUAID = "last_from_yihuaid"
    const val SP_LAST_ORAL_TOKEN = "last_oral_token"
    const val SP_LAST_LOGIN_USER = "last_login_user"
    const val SP_POST_DRAFT = "post_draft"
    const val SP_COMMENT_DRAFT = "comment_draft"
    const val SP_POST_FIRST_LIKE = "post_first_like"
    const val SP_TPOIC_FIRST_COLLECT = "tpoic_first_collect"
    const val SP_ALREADY_GUIDE_TIP = "sp_already_guide_tip"// 展示过新手引导
    const val SP_AES_LOGIN_TIME = "SP_AES_LOGIN_TIME"// 上次登录时间
    const val SP_AES_LOGIN_SERVICE_TIME = "SP_AES_LOGIN_SERVICE_TIME"// 上次登录成功时的服务器时间
    const val SP_LAST_CITY_CONFIGURATION_INFO = "sp_last_city_configuration_info"// 上次获取的城市首页信息
    const val SP_SHOW_CITY = "sp_show_city"// 上次获取的城市首页信息
    const val SP_POST_RE_EDIT = "sp_post_re_edit"// 上次获取的城市首页信息
    const val SP_POPUP_ACTIVITY = "sp_popup_activity"// 是否自动进入过分销界面
    const val SP_AUTO_PLAY_VIDEO = "sp_auto_play_video"// 是否自动播放视频
    //    const val SP_SEARCH_LOCATION = "search_local"
    const val SP_IS_STAGGERED_GRID_DISCOUNT = "sp_is_staggered_grid_discount"// 是否是瀑布流形式的推荐列表
    const val SP_ORDER_ADDRESS = "sp_order_address"// 订单默认地址
    const val SP_IS_SHOW_USER_AGREEMENT = "sp_is_show_user_agreement"// 是否展示过用户协议

    /** 数据版本 */
    const val DATA_VERSION = 1

    /** 请求码 */
    const val REQUEST_CAPTCHA = 1000
    /** 更新数据 */
    const val REQUEST_UPDATE = 1001
    /** 是否更新 */
    const val REQUEST_REFRESH = 1002
    /** 选取附件 */
    const val REQUEST_MEDIA = 1003
    /** 话题 */
    const val REQUEST_TOPIC = 1004
    /** 关注 */
    const val REQUEST_AT = 1005
    /** 获取图片 */
    const val REQUEST_PHOTO = 1006
    /** 裁剪 */
    const val REQUEST_CROP = 1007
    /** 拍摄 */
    const val REQUEST_CAMERA = 1008
    /** 录制 */
    const val REQUEST_VIDEO = 1009
    /** 微信登录 */
    const val REQUEST_WECHAT_LOGIN = 1010

    /** 加密接口用 */
    const val ACTION_CODE = "verifycode"
    const val ACTION_LOGIN = "login"
    const val ACTION_USER = "user"
    const val ACTION_UPVOTE = "upvote"
    const val ACTION_DOSHIELD = "doShield"
    const val ACTION_COMMUNITYACCUSE = "communityAccuse"
    const val ACTION_DELETE = "delete"
    const val ACTION_CONCERN = "concern"
    const val ACTION_START = "appStartUp"
    const val ACTION_TOKEN = "token"
    const val ACTION_ADD = "add"
    const val ACTION_PAY = "pay"
    const val ACTION_ORDER = "order"
    const val ACTION_COMMUNITY = "communityUserConcern"
    const val ACTION_SHIELD = "removeShield"
    const val ACTION_PREVIOUS = "checkOldPhone"
    const val ACTION_CHECKWAY = "checkWay"
    const val ACTION_COMMENTVOTE = "commentVote"
    const val ACTION_DELETECOMMENT = "deleteComment"
    const val ACTION_DELETETOPICCOMMENT = "deleteTopicComment"
    const val ACTION_DELETEFEED = "deleteFeed"
    const val ACTION_BIND_WECHAT = "bindWechat"
    const val ACTION_UNBIND_WECHAT = "unbindWechat"
    const val ACTION_ADDCOMMENT = "addComment"
    const val ACTION_DISTRIBUTION = "distribution"
    const val ACTION_UNIFIED_LOGIN = "unifiedLogin"

    const val VERIFY_BIND = "08" // 换绑
    const val VERIFY_PROTECTION = "09" // 修改密保

    const val LOGIC_PHONE = 0
    const val LOGIC_QUESTION = 1

    const val FLAG_LOGOUT = 0
    const val FLAG_LOGON = 1

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

    /** 经验值 */
    const val WEB_EXP = 0
    /** 客服 */
    const val WEB_CUS = 1

    /** Json数据键值 */
    const val KEY_CONTENT = "content"
    const val KEY_NOTIFY = "notify"
    const val KEY_FLASH_SALES = "startSeckillList"
    const val KEY_PREPARE_FLASH_SALES = "unStartSeckillList"
    const val KEY_BANNER_LIST = "notifyList"
    const val KEY_POST_LIST = "feedList"
    const val KEY_AD_LIST = "adList"
    const val KEY_FANS_LIST = "fansList"
    const val KEY_PRAISE_LIST = "upvoteList"
    const val KEY_AT_LIST = "mentionFeedList"
    const val KEY_COMMENT_LIST = "commentList"
    const val KEY_SNIPING_LIST = "resList"
    const val KEY_FOLLOW_LIST = "concernList"
    const val KEY_TOPIC_LIST = "topicList"
    const val KEY_FAQ_LIST = "faqList"
    const val KEY_EXP_LIST = "ExpList"
    const val KEY_TASK_LIST = "taskList"
    const val KEY_DOWN_LINE_LIST = "followerList"
    const val KEY_REMIND_LIST = "appoList"
    const val KEY_CONTACT_LIST = "phoneBookList"
    const val KEY_STORE_LIST = "storeList"
    const val KEY_USER_LIST = "userList"
    const val KEY_VOUCHER_LIST = "exSimpleList"
    const val KEY_MENTIONABLEEXES_LIST = "mentionableExes"
    const val KEY_ROLLING_LIST = "systemNotifyList"

    const val KEY_SELECT_COUPON_LIST = "resMap"
    const val KEY_MY_COUPON_LIST = "dtoList"
    const val KEY_MY_VOUCHER_LIST = "orderList"
    const val KEY_MY_FAN_LIST = "userFansList"
    const val KEY_USER = "user"
    const val KEY_LIST = "list"
    const val KEY_YIHUA_ID = "yihuaId"
    const val KEY_EX_LIST = "exchangeList"
    const val KEY_COMMENTS = "comments"
    const val KEY_FEED_UPVOTE_LIST = "feedUpvoteList"
    const val KEY_STORE_POI_LIST = "storePoiList"
    const val KEY_FEED_MEDIA_LIST = "feedMediaList"
    const val KEY_FEED_LIST = "feedList"
    const val KEY_DATA = "data"
    const val KEY_FANS_MESSAGE_LIST = "fansMessageList"// 粉丝消息
    const val KEY_MESSAGE_LIST = "messageList"// 评论，艾特消息
    const val KEY_MESSAGE_INFO_LIST = "messageInfoList"// 消息首页
    const val KEY_TOPIC_RANKING = "topicRecommendList"// topic排行榜
    const val KEY_TOPIC_CONFIG = "topicConfig"// 首页配置信息
    const val KEY_USER_INCOME_DTO_LIST = "userIncomeDtoList"
    const val KEY_MIX_HOME_DTO_LIST = "mixHomeDtoList"

    const val DEVICE_ANDROID = "1"

    /** 券类型 代金券 */
    const val TYPE_VOUCHER = 1
    const val TYPE_PACKAGE = 2
    const val TYPE_CARD = 3
    /** 消息类型 */
    const val TYPE_FANS = 0
    const val TYPE_LIKE = 1
    const val TYPE_TAIL = 22
    const val TYPE_COMMENT = 3
    const val TYPE_NEWEST = 0// 话题最新
    const val TYPE_HOT = 1// 话题最热
    const val TYPE_NAME = 0
    const val TYPE_REFEREE = 1
    const val TYPE_ADDRESS = 2
    const val TYPE_ALIPAY = 3
    const val TYPE_SEARCH = 0
    const val TYPE_FILTER = 1

    const val PARAM_MOBILE = "param_mobile"
    const val PARAM_MODE = "param_mode"
    const val PARAM_ID = "param_id"
    const val PARAM_ORDER_ID = "PARAM_ORDER_ID"
    const val PARAM_DISCOUNT = "param_discount"
    const val PARAM_BUSINESS_ID = "param_business_id"
    const val PARAM_EXCHANGE_ID = "param_exchange_id"
    const val PARAM_OWNER_ID = "param_owner_id"
    const val PARAM_NAME = "param_name"
    const val PARAM_TYPE = "param_type"
    const val PARAM_LOGIC = "param_logic"
    const val PARAM_ACTION = "param_action"
    const val PARAM_LIST = "param_list"
    const val PARAM_TIMES = "param_times"
    const val PARAM_INDEX = "param_index"
    const val PARAM_TITLE = "param_title"
    const val PARAM_EXCLUDE = "param_exclude"
    const val PARAM_FINISH = "param_finish"
    const val PARAM_ORDER_NUMBER = "param_order_number"
    const val PARAM_STORE_ID = "param_store_id"
    const val PARAM_POI_ID = "param_poi_id"
    const val PARAM_VOUCHER_ID = "param_voucher_id"
    const val PARAM_OPEN_SHARE = "param_open_share"
    const val PARAM_CARD_ID = "param_card_id"
    const val PARAM_POST_ID = "param_post_id"
    const val PARAM_YIHUA_ID = "param_yihua_id"
    const val PARAM_FEED_ID = "param_feed_id"
    const val PARAM_FEED_EXPANDED = "param_feed_expanded"
    const val PARAM_FEED_PRAISE_EXPANDED = "param_feed_praise_expanded"// 点赞列表上弹
    const val PARAM_FEED_SHOW_DIALOG = "param_feed_show_dialog"
    const val PARAM_REPLY_COMMENT_ID = "param_reply_comment_id"
    const val PARAM_COMMENT_HINT = "param_comment_hint"
    const val PARAM_COMMENT_PID = "param_comment_pid"
    const val PARAM_COMMENT_ID = "param_comment_id"
    const val PARAM_SAVE = "param_save"
    const val PARAM_SELECT = "param_select"
    const val PARAM_AT = "param_at"
    const val PARAM_FILTER = "param_filter"
    const val PARAM_SEARCH_TYPE = "param_search_type"
    const val PARAM_SEARCH_TEXT = "param_search_text"
    const val PARAM_DISTRIBUTION_TYPE = "param_distribution_type"
    const val PARAM_IMAGE_URL = "param_image_url"
    const val PARAM_HINT = "param_hint"
    const val PARAM_TOPIC_ID = "param_topic_id"
    const val PARAM_VIDEO_PROGRESS = "param_video_progress"
    const val PARAM_TOPIC_NAME = "param_topic_name"
    const val PARAM_TOPIC_TYPE = "param_topic_type"
    const val PARAM_TOPIC_FROM_TOPIC = "param_topic_from_topic"
    const val PARAM_TRY_TOPIC = "param_try_topic"
    const val PARAM_FROM_UNPUBLISHED = "param_from_unpublished"
    const val PARAM_POST_EXTRAS = "param_post_extras"
    const val PARAM_SHOW_SHARE_DIALOG = "param_show_share_dialog"
    const val PARAM_HASH_CODE = "param_hash_code"
    const val PARAM_POSITION = "param_position"
    const val PARAM_SEND_EVENT = "param_send_event"
    const val PARAM_SEX = "param_sex"
    const val PARAM_ONLY_EDIT_PASSWORD = "param_only_edit_password"
    const val PARAM_SHOW_BACK = "param_show_back"
    const val PARAM_HIDE_EARN_SHARE = "param_hide_earn_share"// 隐藏分享赚
    const val PARAM_MOUNT_LINK_AFTER_LOGIN = "param_mount_link_after_login"// 登录成功后挂载分销链
    const val PARAM_FROM_YIHUA_ID = "param_from_yihua_id"// 分销益划id
    const val PARAM_ADOWNER_ID = "param_adowner_id"// 广告主id
    const val PARAM_DISTRIBUTION_ID = "param_distribution_id"// 分销业务ID （eg: 代金券ID）
    const val PARAM_FROM_NOTIFICATION = "param_from_notification"// 页面来源于推送
    const val PARAM_NOTIFICATION_OPEN_MESSAGE = "param_notification_open_message"// 推送打开主页面tab位置
    const val PARAM_URL = "param_url"
    const val PARAM_HANDLE_BACK = "param_handle_back" // 处理返回键
    const val PARAM_WEB_REFRESH = "param_web_refresh" // 是否可刷新
    const val PARAM_WEB_CHANGETITLE = "param_web_changetitle" // 自动改变标题
    const val PARAM_IS_OTHER_TYPE = "IS_OTHER_TYPE" // 点击评价的查看更多
    const val PARAM_SORT_TYPE = "param_sort_type"
    const val PARAM_FEED_TYPE = "param_feed_type"

    const val PARAM_CITY_CODE = "param_city_code"
    const val PARAM_ZONE_CODE = "param_zone_code"
    const val PARAM_SQUARE_CODE = "param_square_code"
    const val PARAM_TYPE_CODE = "param_type_code"


    /** 同步数据 */
    const val ACTION_SYNC = 1
    const val ACTION_START_LOCATION = 2
    const val ACTION_CHECK_NOVICE = 3

    /** 更新红点 */
    const val ACTION_SYNC_REMIND = 4
    const val ACTION_CHECK_ORALTOKEN = 5
    const val ACTION_UPGRADE = 6
    const val ACTION_NEWAPPRENTICE = 7
    const val ACTION_LINKTASK_START = 8
    const val ACTION_REQUEST_EXCONDTION = 9

    /** RxBus Event */
    const val EVENT_STARTUP_DONE = 0x59//新手接口
    const val EVENT_FINISH_ALL = 0x60
    const val EVENT_LOCAL_CHANGED = 0x61 // 用户选择城市
    const val EVENT_NEED_REFRESH = 0x62//登录登出后刷新界面
    const val EVENT_HOME_REFRESH = 0x63// 选择城市后，刷新首页数据
    const val EVENT_MY_REFRESH = 0x64
    const val EVENT_NEED_FINISH = 0x65
    const val EVENT_GIFT_SUCCESS = 0x66 // 代金券转赠成功
    const val EVENT_ORDER_UPDATE = 0x67 // 订单更新
    const val EVENT_POINT_UPDATE = 0x68 // 红点更新
    const val EVENT_ORDER_DELETE = 0x69 // 红点更新
    const val EVENT_FINISH_BRANCH = 0x70 // 分店结束
    const val EVENT_FAB_CASH_VISIBLE_REFRESH = 0x71 // 首页发帖按钮，是否显示发帖赚现金
    const val EVENT_SKU_REFRESH = 0x72//优惠界面刷新
    const val EVENT_SET_PASSWORD = 0x73// 密码设置成功
    const val EVENT_SEARCH_POI_LIST = 0x74// poi列表刷新 搜索
    const val EVENT_MOVE_POI_LIST = 0x75// poi列表刷新 移动
    const val EVENT_LOGOUT = 0x76//登录登出后刷新界面
    const val EVENT_LOGIN = 0x78//登录后刷新界面
    const val EVENT_FOLLOW_USER = 0x77//关注用户
    const val EVENT_EVALUATION_DONE = 0x80// 消费后评价完成


    const val EVENT_AUTH_SUCCESS = 0x80 // 微信授权成功
    const val EVENT_AUTH_CANCEL = 0x81 // 微信授权取消
    const val EVENT_AUTH_FAIL = 0x82 // 微信授权取消
    const val EVENT_WECHAT_REGISTER = 0x83 // 微信注册成功
    const val EVENT_CHECK_WECHAT_MINI_DRAINAGE = 0x84 // 检查未完成订单(小程序引流)
    const val EVENT_ORDER_RETURN_SUCCESS = 0x85 // 退款成功
    const val EVENT_BIND_NEW_PHONE_SUCCESS = 0x86 // 手机号换绑成功
    const val EVENT_RELOAD = 0x87 // 刷新
    const val EVENT_REFRESH_TAB_MAIN_TOPIC = 0x88 // 刷新 tab 0 页内容
    const val EVENT_REFRESH_TAB_MAIN_STORE = 0x89 // ...  tab 1
    const val EVENT_FOLD_APPBAR = 0x92
    const val EVENT_EXPANDED_APPBAR = 0x93
    const val EVENT_MORE_TRANSPARENT = 0x94
    const val EVENT_MORE_SATURATION = 0x95

    /** 帖子 */
    const val ITEM_POST = 1
    /** 广告 */
    const val ITEM_AD = 2
    /** 推荐关注 */
    const val ITEM_SUGGEST = 3

    /**
     * 二维码大小
     */
    const val QR_IMAGE_SIZE = 300

    /**
     * 佣金最高
     */
    const val TYPE_DISTRIBUTION_MONEY_MAX = 1

    /**
     * 最新上新
     */
    const val TYPE_DISTRIBUTION_NEWEST = 2

    /**
     * 销量最高
     */
    const val TYPE_DISTRIBUTION_SELLING = 3

    /**
     * 消息唯一标识
     */
    const val EXTRA_KEY_MESSAGE_NUM = "messageNum"

    /**
     * order id
     * 订单id
     */
    const val EXTRA_KEY_ORDER_ID = "orderId"


    const val KEY_MESSAGE_INFO_DTO_LIST = "messageInfoDtoList"

    /**
     * 话题id
     */
    const val EXTRA_KEY_TOPIC_ID = "topicId"

    /**
     * 话题名称
     */
    const val EXTRA_KEY_TOPIC_NAME = "topicName"

    /**
     * 是否是益友体验记话题
     */
    const val EXTRA_KEY_TRY_TOPIC = "tryTopic"

    /**
     * 艾特实体
     */
    const val EXTRA_KEY_AT_ENTITY = "atEntity"

    const val EXTRA_POI_NAME = "poiName"
    const val EXTRA_POI_ID = "poiId"
    const val EXTRA_LONGITUDE = "longitude"
    const val EXTRA_LATITUDE = "latitude"

    /**
     *  手机号转赠
     */
    const val TYPE_GIFT_PHONE: Int = 0

    /**
     *  微信转赠
     */
    const val TYPE_GIFT_WECHAT: Int = 1

    /**
     * PGC 详情
     */
    const val TYPE_AD_PGC: Int = 0

    /**
     * 广告详情
     */
    const val TYPE_AD_SINGLE: Int = 1


    const val VIDEO_IMAGE = 0
    const val ORIGINAL_IMAGE = 1
    const val GIF_IMAGE = 2
    const val VIDEO = 3

    const val SMALL_WIDTH_120 = "-smallWidth120.jpg"
    const val SMALL_WIDTH_360 = "-smallWidth360.jpg"
    const val SMALL_WIDTH_720 = "-smallWidth720.jpg"


    // ## 登录相关接口，请求类型
    /** 使用验证码登录或注册 */
    const val TYPE_CAPTCHA_LOGIN_REGISTER = 0

    /** 密码登录 */
    const val TYPE_PASSWORD_LOGIN = 1

    /** 微信登录 */
    const val TYPE_WECHAT_LOGIN = 2

    /** 微信登录绑定手机号 */
    const val TYPE_WECHAT_LOGIN_BIND_PHONE = 3

    /** 微信小程序登录 */
    const val TYPE_WECHAT_MINI_LOGIN = 4

    /** 头条小程序登录 */
    const val TYPE_BYTE_DANCE_MINI_LOGIN = 5
    // #############################

    /** 线性优惠列表类型 */
    const val TYPE_DISCOUNT_LINEAR = 0

    /** 瀑布流式优惠列表类型 */
    const val TYPE_DISCOUNT_STAGGERED_GRID = 1



}