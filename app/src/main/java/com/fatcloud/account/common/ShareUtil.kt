package com.fatcloud.account.common

import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.Platform.ShareParams
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.sina.weibo.SinaWeibo
import cn.sharesdk.tencent.qq.QQ
import cn.sharesdk.tencent.qzone.QZone
import cn.sharesdk.wechat.friends.Wechat
import cn.sharesdk.wechat.moments.WechatMoments
import cn.sharesdk.wechat.utils.WXMiniProgramObject
import com.fatcloud.account.BuildConfig
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * Created by Wasngsw on 2019/6/3 14:01.
 *
 *
 * @see [ShareSdk官网文档](http://wiki.mob.com/%E4%B8%8D%E5%90%8C%E5%B9%B3%E5%8F%B0%E5%88%86%E4%BA%AB%E5%86%85%E5%AE%B9%E7%9A%84%E8%AF%A6%E7%BB%86%E8%AF%B4%E6%98%8E/)
 */
object ShareUtil {
    // # wechat
    /**
     * 微信分享文本
     *
     * @param title                  512Bytes以内
     * @param text                   1KB以内
     * @param platformActionListener
     */
    fun shareTextWechat(title: String?, text: String?, platformActionListener: PlatformActionListener?) {
        ShareSDK.deleteCache()
        val platform = ShareSDK.getPlatform(Wechat.NAME)
        val shareParams = ShareParams()
        shareParams.title = title
        shareParams.text = text
        shareParams.shareType = Platform.SHARE_TEXT
        platform.platformActionListener = platformActionListener
        platform.share(shareParams)
    }

    /**
     * 微信分享图片
     *
     * @param title                  512Bytes以内
     * @param imageUrl               url路径10KB以内
     * @param imageFilePath          path路径不能超过10KB
     * @param imageBitmapData        10MB以内
     * @param platformActionListener
     */
    fun shareImageWechat(
        title: String?,
        imageUrl: String?,
        imageFilePath: String?,
        imageBitmapData: Bitmap?,
        platformActionListener: PlatformActionListener?
    ) {
        ShareSDK.deleteCache()
        val platform = ShareSDK.getPlatform(Wechat.NAME)
        val shareParams = ShareParams()
        shareParams.title = title
        if (!TextUtils.isEmpty(imageUrl)) {
            shareParams.imageUrl = imageUrl
        }
        if (!TextUtils.isEmpty(imageFilePath)) {
            shareParams.imagePath = imageFilePath
        }
        if (imageBitmapData != null) {
            shareParams.imageData = imageBitmapData
        }
        shareParams.shareType = Platform.SHARE_IMAGE
        platform.platformActionListener = platformActionListener
        platform.share(shareParams)
    }

    /**
     * 微信分享网页
     *
     * @param title
     * @param text
     * @param imageUrl
     * @param imageFilePath
     * @param imageBitmapData
     * @param webUrl
     * @param platformActionListener
     */
    fun shareWebpagerWechat(
        title: String?,
        text: String?,
        imageUrl: String?,
        imageFilePath: String?,
        imageBitmapData: Bitmap?,
        webUrl: String?,
        platformActionListener: PlatformActionListener?
    ) {
        ShareSDK.deleteCache()
        val platform = ShareSDK.getPlatform(Wechat.NAME)
        val shareParams = ShareParams()
        shareParams.title = title
        shareParams.text = text
        shareParams.url = webUrl
        if (!TextUtils.isEmpty(imageUrl)) {
            shareParams.imageUrl = imageUrl
        }
        if (!TextUtils.isEmpty(imageFilePath)) {
            shareParams.imagePath = imageFilePath
        }
        if (imageBitmapData != null) {
            shareParams.imageData = imageBitmapData
        }
        shareParams.shareType = Platform.SHARE_WEBPAGE
        platform.platformActionListener = platformActionListener
        platform.share(shareParams)
    }

    /**
     * 微信分享小程序
     *
     * @param title
     * @param lowVersionUrl          低版本微信将打开该url：
     * @param imageUrl
     * @param imageFilePath
     * @param imageBitmapData
     * @param wxPath                 path
     * @param platformActionListener
     */
    fun shareMiniWechat(
        title: String?,
        lowVersionUrl: String?,
        imageUrl: String?,
        imageFilePath: String?,
        imageBitmapData: Bitmap?,
        wxPath: String?,
        platformActionListener: PlatformActionListener?
    ) {
        ShareSDK.deleteCache()
        val platform = ShareSDK.getPlatform(Wechat.NAME)
        val shareParams = ShareParams()
        shareParams.title = title
        if (!TextUtils.isEmpty(lowVersionUrl)) {
            shareParams.url = lowVersionUrl
        } else {
            shareParams.url = "http://www.yihuaapp.com/share.html"
        }
        if (!TextUtils.isEmpty(imageUrl)) {
            shareParams.imageUrl = imageUrl
        }
        if (!TextUtils.isEmpty(imageFilePath)) {
            shareParams.imagePath = imageFilePath
        }
        if (imageBitmapData != null) {
            shareParams.imageData = imageBitmapData
        }
        shareParams.wxUserName = "gh_3bf9554636b4"
        shareParams.wxPath = wxPath
        shareParams.shareType = Platform.SHARE_WXMINIPROGRAM
        shareParams.wxMiniProgramType =
            if (BuildConfig.FLAVOR == "dev") WXMiniProgramObject.MINIPROGRAM_TYPE_TEST else WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE
        platform.platformActionListener = platformActionListener
        platform.share(shareParams)
    }
    // # wechat Moment
    /**
     * 朋友圈分享文本
     *
     * @param title
     * @param text
     * @param platformActionListener
     */
    fun shareTextMoment(title: String?, text: String?, platformActionListener: PlatformActionListener?) {
        ShareSDK.deleteCache()
        val platform = ShareSDK.getPlatform(WechatMoments.NAME)
        val shareParams = ShareParams()
        shareParams.title = title
        shareParams.text = text
        shareParams.shareType = Platform.SHARE_TEXT
        platform.platformActionListener = platformActionListener
        platform.share(shareParams)
    }

    fun shareImageMoment(
        title: String?,
        imageUrl: String?,
        imageFilePath: String?,
        imageBitmapData: Bitmap?,
        platformActionListener: PlatformActionListener?
    ) {
        val platform = ShareSDK.getPlatform(WechatMoments.NAME)
        val shareParams = ShareParams()
        shareParams.title = title
        if (!TextUtils.isEmpty(imageUrl)) {
            shareParams.imageUrl = imageUrl
        }
        if (!TextUtils.isEmpty(imageFilePath)) {
            shareParams.imagePath = imageFilePath
        }
        if (imageBitmapData != null) {
            shareParams.imageData = imageBitmapData
        }
        shareParams.shareType = Platform.SHARE_IMAGE
        platform.platformActionListener = platformActionListener
        platform.share(shareParams)
    }

    /**
     * 分享链接-微信
     *
     * @param url       分享链接地址
     * @param title     上方标题
     * @param text      中间内容区域
     * @param imageUrl  底部小Logo链接
     * @param imageData
     * @param listener
     */
    fun shareWechat4WebPage(
        url: String?,
        title: String?,
        text: String?,
        imageUrl: String?,
        imageData: Bitmap?,
        listener: PlatformActionListener?
    ) {
        ShareSDK.deleteCache()
        val share = ShareParams()
        share.title = title
        if (!TextUtils.isEmpty(text)) {
            share.text = text
        }
        share.url = url
        if (imageUrl != null && !imageUrl.isEmpty()) {
            share.imageUrl = imageUrl
        }
        if (imageData != null) {
            share.imageData = imageData
        }
        share.shareType = Platform.SHARE_WEBPAGE
        val share_p = ShareSDK.getPlatform(Wechat.NAME)
        share_p.platformActionListener = listener
        share_p.SSOSetting(true)
        share_p.share(share)
    }

    /**
     * 朋友圈网页分享
     *
     * @param url       链接
     * @param title     内容
     * @param text      传了也不显示
     * @param imageUrl  正文中的图片链接
     * @param imageData 图片data
     * @param listener  分享结果状态回调
     */
    fun shareWechatMoments4WebPage(
        url: String?,
        title: String?,
        text: String?,
        imageUrl: String?,
        imageData: Bitmap?,
        listener: PlatformActionListener?
    ) {
        ShareSDK.deleteCache()
        val share = ShareParams()
        share.title = title
        if (!TextUtils.isEmpty(text)) {
            share.text = text
        }
        share.url = url
        if (imageUrl != null && !imageUrl.isEmpty()) {
            share.imageUrl = imageUrl
        }
        if (imageData != null) {
            share.imageData = imageData
        }
        share.shareType = Platform.SHARE_WEBPAGE
        val share_p = ShareSDK.getPlatform(WechatMoments.NAME)
        share_p.platformActionListener = listener
        share_p.SSOSetting(true)
        share_p.share(share)
    }

    fun shareSinaWeibo(
        content: String,
        imagePath: String?,
        url: String,
        listener: PlatformActionListener?
    ) {
        ShareSDK.deleteCache()
        val share = ShareParams()
        share.imagePath = imagePath
        share.text = content + url
        val share_p = ShareSDK.getPlatform(SinaWeibo.NAME)
        share_p.platformActionListener = listener
        share_p.SSOSetting(true)
        share_p.share(share)
    }

    fun shareWebpagerMoment(
        title: String?,
        imageUrl: String?,
        imageFilePath: String?,
        imageBitmapData: Bitmap?,
        webUrl: String?,
        platformActionListener: PlatformActionListener?
    ) {
        ShareSDK.deleteCache()
        val platform = ShareSDK.getPlatform(WechatMoments.NAME)
        val shareParams = ShareParams()
        shareParams.title = title
        if (!TextUtils.isEmpty(imageUrl)) {
            shareParams.imageUrl = imageUrl
        }
        if (!TextUtils.isEmpty(imageFilePath)) {
            shareParams.imagePath = imageFilePath
        }
        if (imageBitmapData != null) {
            shareParams.imageData = imageBitmapData
        }
        shareParams.url = webUrl
        shareParams.shareType = Platform.SHARE_WEBPAGE
        platform.platformActionListener = platformActionListener
        platform.share(shareParams)
    }
    // # QQ
    /**
     * QQ 网页分享爱那个
     *
     * @param title                  最多30个字符
     * @param text                   最多40个字符
     * @param imageUrl
     * @param imageFilePath
     * @param platformActionListener 注：QQ分享图文和音乐，在PC版本的QQ上可能只看到一条连接，因为PC版本的QQ只会对其白名单的连接作截图，如果不在此名单中，则只是显示连接而已. 如果只分享图片在PC端看不到图片的，只会显示null，在手机端会显示图片和null字段。
     */
    fun shareWebPagerQq(
        title: String?,
        text: String?,
        imageUrl: String?,
        imageFilePath: String?,
        titleUrl: String?,
        platformActionListener: PlatformActionListener?
    ) {
        ShareSDK.deleteCache()
        val platform = ShareSDK.getPlatform(QQ.NAME)
        val shareParams = ShareParams()
        shareParams.title = title
        shareParams.text = text
        shareParams.titleUrl = titleUrl
        if (!TextUtils.isEmpty(imageUrl)) {
            shareParams.imageUrl = imageUrl
        }
        if (!TextUtils.isEmpty(imageFilePath)) {
            shareParams.imagePath = imageFilePath
        }
        shareParams.shareType = Platform.SHARE_WEBPAGE
        platform.platformActionListener = platformActionListener
        platform.share(shareParams)
    }

    /**
     * QQ 分享图片
     *
     * @param imageUrl
     * @param imageFilePath
     * @param platformActionListener
     */
    fun shareImageQq(
        imageUrl: String?,
        imageFilePath: String?,
        platformActionListener: PlatformActionListener?
    ) {
        ShareSDK.deleteCache()
        val platform = ShareSDK.getPlatform(QQ.NAME)
        val shareParams = ShareParams()
        if (!TextUtils.isEmpty(imageUrl)) {
            shareParams.imageUrl = imageUrl
        }
        if (!TextUtils.isEmpty(imageFilePath)) {
            shareParams.imagePath = imageFilePath
        }
        shareParams.shareType = Platform.SHARE_IMAGE
        platform.platformActionListener = platformActionListener
        platform.share(shareParams)
    }
    // # QZone 提示：必须需要QQ客户端才可以分享
    // QQ空间支持分享文字和图文 参数说明
    // title：最多200个字符
    // text：最多600个字符
    // 分享时一定要携带title、titleUrl、site、siteUrl。
    // site ：分享此内容的网站名称，仅在QQ空间使用
    // siteUrl：分享此内容的网站地址，仅在QQ空间使用
    /**
     * QQ控件分享文本
     *
     * @param title
     * @param text
     * @param titleUrl
     * @param mListener
     */
    fun shareTextQzone(
        title: String?,
        text: String?,
        titleUrl: String?,
        mListener: PlatformActionListener?
    ) {
        ShareSDK.deleteCache()
        val platform = ShareSDK.getPlatform(QZone.NAME)
        val shareParams = ShareParams()
        shareParams.title = title
        shareParams.text = text
        shareParams.titleUrl = titleUrl
        //        shareParams.setSite("account");
//        shareParams.setSiteUrl("http://www.yihuaapp.com/share.html");
        shareParams.shareType = Platform.SHARE_TEXT
        platform.platformActionListener = mListener
        platform.share(shareParams)
    }

    /**
     * QQ空间分享网页
     *
     * @param title
     * @param text
     * @param webUrl
     * @param mListener
     */
    fun shareWebPagerQzone(
        title: String?,
        text: String?,
        webUrl: String?,
        mListener: PlatformActionListener?
    ) {
        ShareSDK.deleteCache()
        val platform = ShareSDK.getPlatform(QZone.NAME)
        val shareParams = ShareParams()
        shareParams.title = title
        shareParams.text = text
        shareParams.url = webUrl
        //        shareParams.setSite("account");
//        shareParams.setSiteUrl("http://www.yihuaapp.com/share.html");
        shareParams.shareType = Platform.SHARE_WEBPAGE
        platform.platformActionListener = mListener
        platform.share(shareParams)
    }

    /**
     * QQ空间分享图片
     *
     * @param title
     * @param text
     * @param imageUrl
     * @param mListener
     */
    fun shareImageQzone(
        title: String?,
        text: String?,
        imageUrl: String?,
        mListener: PlatformActionListener?
    ) {
        ShareSDK.deleteCache()
        val platform = ShareSDK.getPlatform(QZone.NAME)
        val shareParams = ShareParams()
        shareParams.imageUrl = imageUrl
        shareParams.shareType = Platform.SHARE_IMAGE
        platform.platformActionListener = mListener
        platform.share(shareParams)
    }

    // # Sina
    //
    // 注：微博分享链接是将链接写到setText内：eg：setText(“分享文本 http://mob.com”);

    /**
     * 微博分享文本+网页
     * 图片不能大于2M，仅支持JPEG、GIF、PNG格式
     * @param text
     * @param webPageUrl
     * @param mListener
     */
    fun shareTextSinaWeibo(text: String, webPageUrl: String, mListener: PlatformActionListener?) {

        ShareSDK.deleteCache()
        val shareParams = ShareParams().apply {
            this.text = text + webPageUrl
        }
        val platform = ShareSDK.getPlatform(SinaWeibo.NAME).apply {
            platformActionListener = mListener
        }
        platform.share(shareParams)
    }

    /**
     * 微博分享爱图文
     *
     * @param text
     * @param webPageUrl
     * @param imageUrl
     * @param imageFilePath
     * @param mListener
     */
    fun shareImageSinaWeibo(text: String, webPageUrl: String, imageUrl: String?, imageFilePath: String?, mListener: PlatformActionListener?) {
        ShareSDK.deleteCache()

        val shareParams = ShareParams().apply {
            this.text = text + webPageUrl

            if (!TextUtils.isEmpty(imageUrl)) {
                this.imageUrl = imageUrl
            }
            if (!TextUtils.isEmpty(imageFilePath)) {
                this.imagePath = imageFilePath
            }
        }
        val platform = ShareSDK.getPlatform(SinaWeibo.NAME).apply {
            platformActionListener = mListener
        }
        platform.share(shareParams)
    }

    /**
     * 微信登录授权，会拿回用户相关的微信信息，
     *
     * @param platformActionListener
     */
    fun wechatLogin(platformActionListener: PlatformActionListener?) {
        val wechat = ShareSDK.getPlatform(Wechat.NAME) ?: return
        wechat.platformActionListener = platformActionListener
        if (wechat.isAuthValid) {
            wechat.removeAccount(true)
            return
        }
        wechat.authorize()
    }

    /**
     * 微信授权
     */
    fun wechatAuth(context: Context?) {
        val mApi = WXAPIFactory.createWXAPI(context, BuildConfig.WECHAT_APPID, false)
        mApi.registerApp(BuildConfig.WECHAT_APPID)
        if (!AndroidUtil.isWeixinAvilible(context)) {
            return
        }
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo" // 用户个人信息
        mApi.sendReq(req)
    }
}