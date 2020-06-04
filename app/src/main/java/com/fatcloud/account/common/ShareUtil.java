package com.fatcloud.account.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import androidx.annotation.Nullable;


import com.fatcloud.account.BuildConfig;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.wechat.utils.WXMiniProgramObject;

/**
 * Created by Wasngsw on 2019/6/3 14:01.
 * </br>
 *
 * @see <a href="http://wiki.mob.com/%E4%B8%8D%E5%90%8C%E5%B9%B3%E5%8F%B0%E5%88%86%E4%BA%AB%E5%86%85%E5%AE%B9%E7%9A%84%E8%AF%A6%E7%BB%86%E8%AF%B4%E6%98%8E/">ShareSdk官网文档</a>
 */
public class ShareUtil {

    // # wechat

    /**
     * 微信分享文本
     *
     * @param title                  512Bytes以内
     * @param text                   1KB以内
     * @param platformActionListener
     */
    public static void shareTextWechat(String title, String text, PlatformActionListener platformActionListener) {
        ShareSDK.deleteCache();
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setTitle(title);
        shareParams.setText(text);
        shareParams.setShareType(Platform.SHARE_TEXT);
        platform.setPlatformActionListener(platformActionListener);
        platform.share(shareParams);
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
    public static void shareImageWechat(String title,
                                        String imageUrl,
                                        String imageFilePath,
                                        Bitmap imageBitmapData,
                                        PlatformActionListener platformActionListener) {
        ShareSDK.deleteCache();
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setTitle(title);

        if (!TextUtils.isEmpty(imageUrl)) {
            shareParams.setImageUrl(imageUrl);
        }
        if (!TextUtils.isEmpty(imageFilePath)) {
            shareParams.setImagePath(imageFilePath);
        }

        if (imageBitmapData != null) {
            shareParams.setImageData(imageBitmapData);
        }

        shareParams.setShareType(Platform.SHARE_IMAGE);
        platform.setPlatformActionListener(platformActionListener);
        platform.share(shareParams);
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
    public static void shareWebpagerWechat(String title,
                                           String text,
                                           String imageUrl,
                                           String imageFilePath,
                                           Bitmap imageBitmapData,
                                           String webUrl,
                                           PlatformActionListener platformActionListener
    ) {
        ShareSDK.deleteCache();
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setTitle(title);
        shareParams.setText(text);
        shareParams.setUrl(webUrl);
        if (!TextUtils.isEmpty(imageUrl)) {
            shareParams.setImageUrl(imageUrl);
        }
        if (!TextUtils.isEmpty(imageFilePath)) {
            shareParams.setImagePath(imageFilePath);
        }
        if (imageBitmapData != null) {
            shareParams.setImageData(imageBitmapData);
        }
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        platform.setPlatformActionListener(platformActionListener);
        platform.share(shareParams);
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
    public static void shareMiniWechat(@Nullable String title,
                                       @Nullable String lowVersionUrl,
                                       String imageUrl,
                                       String imageFilePath,
                                       Bitmap imageBitmapData,
                                       String wxPath,
                                       PlatformActionListener platformActionListener) {
        ShareSDK.deleteCache();
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setTitle(title);
        if (!TextUtils.isEmpty(lowVersionUrl)) {
            shareParams.setUrl(lowVersionUrl);
        } else {
            shareParams.setUrl("http://www.yihuaapp.com/share.html");
        }
        if (!TextUtils.isEmpty(imageUrl)) {
            shareParams.setImageUrl(imageUrl);
        }
        if (!TextUtils.isEmpty(imageFilePath)) {
            shareParams.setImagePath(imageFilePath);
        }
        if (imageBitmapData != null) {
            shareParams.setImageData(imageBitmapData);
        }
        shareParams.setWxUserName("gh_3bf9554636b4");
        shareParams.setWxPath(wxPath);
        shareParams.setShareType(Platform.SHARE_WXMINIPROGRAM);

        shareParams.setWxMiniProgramType(BuildConfig.FLAVOR.equals("dev") ? WXMiniProgramObject.MINIPROGRAM_TYPE_TEST : WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE);
        platform.setPlatformActionListener(platformActionListener);
        platform.share(shareParams);
    }


    // # wechat Moment

    /**
     * 朋友圈分享文本
     *
     * @param title
     * @param text
     * @param platformActionListener
     */
    public static void shareTextMoment(String title, String text, PlatformActionListener platformActionListener) {
        ShareSDK.deleteCache();
        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setTitle(title);
        shareParams.setText(text);
        shareParams.setShareType(Platform.SHARE_TEXT);
        platform.setPlatformActionListener(platformActionListener);
        platform.share(shareParams);
    }


    public static void shareImageMoment(String title,
                                        String imageUrl,
                                        String imageFilePath,
                                        Bitmap imageBitmapData,
                                        PlatformActionListener platformActionListener) {

        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setTitle(title);
        if (!TextUtils.isEmpty(imageUrl)) {
            shareParams.setImageUrl(imageUrl);
        }
        if (!TextUtils.isEmpty(imageFilePath)) {
            shareParams.setImagePath(imageFilePath);
        }
        if (imageBitmapData != null) {
            shareParams.setImageData(imageBitmapData);
        }
        shareParams.setShareType(Platform.SHARE_IMAGE);
        platform.setPlatformActionListener(platformActionListener);
        platform.share(shareParams);
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
    public static void shareWechat4WebPage(
            String url,
            String title,
            String text,
            String imageUrl,
            Bitmap imageData,
            PlatformActionListener listener
    ) {
//        ShareSDK.initSDK(context.getApplicationContext());
        ShareSDK.deleteCache();
        Wechat.ShareParams share = new Wechat.ShareParams();
        share.setTitle(title);
        if (!TextUtils.isEmpty(text)) {
            share.setText(text);
        }

        share.setUrl(url);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            share.setImageUrl(imageUrl);
        }
        if (imageData != null) {
            share.setImageData(imageData);
        }

        share.setShareType(Platform.SHARE_WEBPAGE);
        Platform share_p = ShareSDK.getPlatform(Wechat.NAME);
        share_p.setPlatformActionListener(listener);
        share_p.SSOSetting(true);
        share_p.share(share);
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
    public static void shareWechatMoments4WebPage(
            String url,
            String title,
            String text,
            String imageUrl,
            Bitmap imageData,
            PlatformActionListener listener
    ) {
        ShareSDK.deleteCache();
        WechatMoments.ShareParams share = new WechatMoments.ShareParams();
        share.setTitle(title);
        if (!TextUtils.isEmpty(text)) {
            share.setText(text);
        }
        share.setUrl(url);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            share.setImageUrl(imageUrl);
        }
        if (imageData != null) {
            share.setImageData(imageData);
        }
        share.setShareType(Platform.SHARE_WEBPAGE);
        Platform share_p = ShareSDK.getPlatform(WechatMoments.NAME);
        share_p.setPlatformActionListener(listener);
        share_p.SSOSetting(true);
        share_p.share(share);

    }

    public static void shareSinaWeibo(String content,
                                      String imagePath,
                                      String url,
                                      PlatformActionListener listener) {
        ShareSDK.deleteCache();
        SinaWeibo.ShareParams share = new SinaWeibo.ShareParams();
        share.setImagePath(imagePath);
        share.setText(content + url);
        Platform share_p = ShareSDK.getPlatform(SinaWeibo.NAME);
         share_p.setPlatformActionListener(listener);
        share_p.SSOSetting(true);
        share_p.share(share);
    }

    public static void shareWebpagerMoment(String title,
                                           String imageUrl,
                                           String imageFilePath,
                                           Bitmap imageBitmapData,
                                           String webUrl,
                                           PlatformActionListener platformActionListener) {
        ShareSDK.deleteCache();
        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setTitle(title);
        if (!TextUtils.isEmpty(imageUrl)) {
            shareParams.setImageUrl(imageUrl);
        }
        if (!TextUtils.isEmpty(imageFilePath)) {
            shareParams.setImagePath(imageFilePath);
        }
        if (imageBitmapData != null) {
            shareParams.setImageData(imageBitmapData);
        }
        shareParams.setUrl(webUrl);
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        platform.setPlatformActionListener(platformActionListener);
        platform.share(shareParams);
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
    public static void shareWebPagerQq(String title,
                                       String text,
                                       String imageUrl,
                                       String imageFilePath,
                                       String titleUrl,
                                       PlatformActionListener platformActionListener) {
        ShareSDK.deleteCache();
        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setTitle(title);
        shareParams.setText(text);
        shareParams.setTitleUrl(titleUrl);
        if (!TextUtils.isEmpty(imageUrl)) {
            shareParams.setImageUrl(imageUrl);
        }
        if (!TextUtils.isEmpty(imageFilePath)) {
            shareParams.setImagePath(imageFilePath);
        }
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        platform.setPlatformActionListener(platformActionListener);
        platform.share(shareParams);
    }

    /**
     * QQ 分享图片
     *
     * @param imageUrl
     * @param imageFilePath
     * @param platformActionListener
     */
    public static void shareImageQq(String imageUrl,
                                    String imageFilePath,
                                    PlatformActionListener platformActionListener) {
        ShareSDK.deleteCache();
        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        Platform.ShareParams shareParams = new Platform.ShareParams();
        if (!TextUtils.isEmpty(imageUrl)) {
            shareParams.setImageUrl(imageUrl);
        }
        if (!TextUtils.isEmpty(imageFilePath)) {
            shareParams.setImagePath(imageFilePath);
        }
        shareParams.setShareType(Platform.SHARE_IMAGE);
        platform.setPlatformActionListener(platformActionListener);
        platform.share(shareParams);
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
    public static void shareTextQzone(String title,
                                      String text,
                                      String titleUrl,
                                      PlatformActionListener mListener) {
        ShareSDK.deleteCache();
        Platform platform = ShareSDK.getPlatform(QZone.NAME);
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setTitle(title);
        shareParams.setText(text);
        shareParams.setTitleUrl(titleUrl);
//        shareParams.setSite("account");
//        shareParams.setSiteUrl("http://www.yihuaapp.com/share.html");
        shareParams.setShareType(Platform.SHARE_TEXT);
        platform.setPlatformActionListener(mListener);
        platform.share(shareParams);
    }


    /**
     * QQ空间分享网页
     *
     * @param title
     * @param text
     * @param webUrl
     * @param mListener
     */
    public static void shareWebPagerQzone(String title,
                                          String text,
                                          String webUrl,
                                          PlatformActionListener mListener) {
        ShareSDK.deleteCache();
        Platform platform = ShareSDK.getPlatform(QZone.NAME);
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setTitle(title);
        shareParams.setText(text);
        shareParams.setUrl(webUrl);
//        shareParams.setSite("account");
//        shareParams.setSiteUrl("http://www.yihuaapp.com/share.html");
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        platform.setPlatformActionListener(mListener);
        platform.share(shareParams);
    }


    /**
     * QQ空间分享图片
     *
     * @param title
     * @param text
     * @param imageUrl
     * @param mListener
     */
    public static void shareImageQzone(String title,
                                       String text,
                                       String imageUrl,
                                       PlatformActionListener mListener) {
        ShareSDK.deleteCache();
        Platform platform = ShareSDK.getPlatform(QZone.NAME);
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setImageUrl(imageUrl);
        shareParams.setShareType(Platform.SHARE_IMAGE);
        platform.setPlatformActionListener(mListener);
        platform.share(shareParams);
    }

    // # Sina
    // 图片不能大于2M，仅支持JPEG、GIF、PNG格式
    // 注：微博分享链接是将链接写到setText内：eg：setText(“分享文本 http://mob.com”);


    /**
     * 微博分享文本+网页
     *
     * @param text
     * @param webPageUrl
     * @param mListener
     */
    public static void shareTextSina(String text,
                                     String webPageUrl,
                                     PlatformActionListener mListener) {
        ShareSDK.deleteCache();
        Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setText(text + webPageUrl);
        platform.setPlatformActionListener(mListener);
        platform.share(shareParams);
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
    public static void shareImage(String text,
                                  String webPageUrl,
                                  String imageUrl,
                                  String imageFilePath,
                                  PlatformActionListener mListener) {

        ShareSDK.deleteCache();
        Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setText(text + webPageUrl);
        if (!TextUtils.isEmpty(imageUrl)) {
            shareParams.setImageUrl(imageUrl);
        }
        if (!TextUtils.isEmpty(imageFilePath)) {
            shareParams.setImagePath(imageFilePath);
        }
        platform.setPlatformActionListener(mListener);
        platform.share(shareParams);
    }


    /**
     * 微信登录授权，会拿回用户相关的微信信息，
     *
     * @param platformActionListener
     */
    public static void wechatLogin(PlatformActionListener platformActionListener) {
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        if (wechat == null) {
            return;
        }
        wechat.setPlatformActionListener(platformActionListener);
        if (wechat.isAuthValid()) {
            wechat.removeAccount(true);
            return;
        }
        wechat.authorize();
    }

    /**
     * 微信授权
     */
    public static void wechatAuth(Context context) {
        IWXAPI mApi = WXAPIFactory.createWXAPI(context, BuildConfig.WECHAT_APPID, false);
        mApi.registerApp(BuildConfig.WECHAT_APPID);

        if (!AndroidUtil.isWeixinAvilible(context)) {
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";// 用户个人信息
        mApi.sendReq(req);
    }


}
