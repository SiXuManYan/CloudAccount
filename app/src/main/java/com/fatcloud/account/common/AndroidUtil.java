package com.fatcloud.account.common;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fatcloud.account.BuildConfig;
import com.fatcloud.account.R;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Administrator on 2016/5/31.
 */
public class AndroidUtil {


    private static final int CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isWifiConnected(Context context) {

        if (context == null) {
            return false;
        }
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager == null) {
            return false;
        }

        NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWiFiNetworkInfo != null) {
            return mWiFiNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * make true current connect service is wifi
     *
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {

        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }


    public static String saveThumb(String video_name, String video_path, Context context, int width, int height) {
        String thumb_image_path = "";
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(video_path);
            bitmap = retriever.getFrameAtTime(-1);
        } catch (IllegalArgumentException ex) {
        } catch (RuntimeException ex) {
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
            }
        }
        if (bitmap == null) {
            return thumb_image_path;
        }
        bitmap = ThumbnailUtils.extractThumbnail(bitmap,
                width,
                height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        String name = AndroidUtil.md5(video_name) + ".png";
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Common.IMAGE_SAVE_PATH;
        try {
            File saveFile = AndroidUtil.saveFile4Bitmap(context, path, bitmap, name, Bitmap.CompressFormat.PNG);
            if (saveFile != null) {
                thumb_image_path = saveFile.getPath();
            }
        } catch (Exception e) {

        }
        //回收
        bitmap.recycle();
        return thumb_image_path;
    }

    public static String getFormatVideoTime(int duration) {
        int second = duration / 1000;
        if (second < 60) {
            return "0:" + getDoubleDecimal(second);
        }

        int minute = second / 60;
        int surplus_second = second % 60;
        if (minute < 60) {
            return minute + ":" + getDoubleDecimal(surplus_second);
        }
        int hour = minute / 60;
        int surplus_minute = minute % 60;
        return hour + ":" + getDoubleDecimal(surplus_minute) + ":" + getDoubleDecimal(surplus_second);

    }

    public static String getFormatSize(long size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            // return size + "Byte";
            return "0KB";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            String result = result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
            if (result.equals("10.00MB")) {
                result = "10MB+";
            }
            return result;
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    public static String getNumTenThousand(int num, String tenThousand) {
        String followCountStr = "";
        if (num >= 0 && num < 10000) {
            followCountStr = num + "";
        } else {
            followCountStr = (num / 10000) + tenThousand + "+";
        }
        return followCountStr;
    }


    private static Random random;

    //双重校验锁获取一个Random单例
    public static Random getRandom() {
        if (random == null) {
            synchronized (AndroidUtil.class) {
                if (random == null) {
                    random = new Random();
                }
            }
        }

        return random;
    }

    /**
     * 获得一个[0,max)之间的整数。
     *
     * @param max
     * @return
     */
    public static int getRandomInt(int max) {
        return Math.abs(getRandom().nextInt()) % max;
    }

    /**
     * 从set中随机取得一个元素
     *
     * @param set
     * @return
     */
    public static String getRandomElement(Set<String> set) {
        int rn = getRandomInt(set.size());
        int i = 0;
        for (String e : set) {
            if (i == rn) {
                return e;
            }
            i++;
        }
        return "";
    }


    public static boolean isNumeric(String number) {
        boolean flag;
        try {
            Integer.valueOf(number);
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public final static String getDoubleDecimal2(Object object) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(object);
    }

    public final static String getDoubleDecimal(Object object) {
        DecimalFormat df = new DecimalFormat("00");
        return df.format(object);
    }

    public final static String getDoubleNoDecimal(Object object) {
        DecimalFormat df = new DecimalFormat("0");
        return df.format(object);
    }

    public static void startIEOrTaobao(String url, Context context) {
        if (url.startsWith("https://") || url.startsWith("http://")) {
            if (url.contains("detail.tmall") || url.contains("item.taobao")) {
                if (AndroidUtil.checkApkExist(context, "com.taobao.taobao")) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    // Uri content_url =
                    // Uri.parse("http://redirect.simba.taobao.com/rd?w=unionnojs&f=http%3A%2F%2Fai.taobao.com%2Fauction%2Fedetail.htm%3Fe%3DJSEvFFnbdrXuDAZjWhpTWL5POJyfkRtg6qc51yDwBatBWJVBnwmj7tnO073KpEUuesayvrQ7hvkEwiwEAUVRm6Zm694AGsktsnwvzENOFXBulQwmqTgIXaDw6CrXJwrW1Zj0FgaU3ng3O8LYOx3ofA%253D%253D%26ptype%3D100010%26from%3Dbasic&k=5ccfdb950740ca16&c=un&b=alimm_0&p=mm_111082503_10544802_34776111");
                    Uri content_url = Uri.parse(url); // 淘宝商品的地址
                    intent.setData(content_url);
                    intent.setClassName("com.taobao.taobao", "com.taobao.tao.detail.activity.DetailActivity");
                    context.startActivity(intent);
                } else {
                    Uri uri = Uri.parse(url);
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(it);
                }
            } else {
                Uri uri = Uri.parse(url);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(it);

            }

        } else {
            Toast.makeText(context, "跳转失败", Toast.LENGTH_SHORT).show();
        }
    }

    public static final void propertyAnim(View view, long millisend, float transX, float transY, float alphaStart, float alphaEnd, Animator.AnimatorListener animationListener, Interpolator value) {
        PropertyValuesHolder pv_transX = PropertyValuesHolder.ofFloat("translationX", transX);
        PropertyValuesHolder pv_transY = PropertyValuesHolder.ofFloat("translationY", transY);
        PropertyValuesHolder pv_alpha = PropertyValuesHolder.ofFloat("alpha", alphaStart, alphaEnd);
        ObjectAnimator obj_animator = ObjectAnimator.ofPropertyValuesHolder(view, pv_transX, pv_transY, pv_alpha);
        if (animationListener != null) {
            obj_animator.addListener(animationListener);
        }
        obj_animator.setInterpolator(value);
        obj_animator.setDuration(millisend);
        obj_animator.start();
    }

    public static final void propertyAnim(View view, long millisend, float rotationStart, float rotationEnd, Animator.AnimatorListener animationListener, Interpolator value) {
        PropertyValuesHolder pv_rotate = PropertyValuesHolder.ofFloat("rotation", rotationStart, rotationEnd);
        ObjectAnimator obj_animator = ObjectAnimator.ofPropertyValuesHolder(view, pv_rotate);
        if (animationListener != null) {
            obj_animator.addListener(animationListener);
        }
        obj_animator.setInterpolator(value);
        obj_animator.setDuration(millisend);
        obj_animator.start();
    }

    // url必须以http、https、ftp或者www开头
    private static Pattern sPattern = Pattern.compile("(http://|ftp://|https://|www){1}[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr)[^\u4e00-\u9fa5\\s]*");

    /**
     * 识别URL
     *
     * @param feedText
     * @return
     */
    public static List<String> recognizeUrls(String feedText) {
        List<String> urls = new ArrayList<String>();
        Matcher matcher = sPattern.matcher(feedText);
        while (matcher.find()) {
            urls.add(matcher.group(0));
        }
        return urls;
    }

    public static String isEmpty(String str) {
        if (str != null && !str.isEmpty() && !str.equals("null")) {
            return str;
        } else {
            return "";
        }
    }

    public static String formateDatetime(long timestamp, String formate) {
        SimpleDateFormat sdf = new SimpleDateFormat(formate, Locale.getDefault());
        return sdf.format(timestamp);
    }

    /**
     * * 防止连续点击类,一共有两个防止连续点击处理
     *
     * @param view
     * @return true 连点 false 没有
     */
    public static boolean isDoubleClick(View view) {
        if (view == null) return false;
        long nowTime = System.currentTimeMillis();
        Object value = view.getTag(R.id.tag_click_time);
        if (value != null) {
            long lastTime = (long) value;
            LogUtils.e(view.getId() + "lastTime", lastTime);
            if (nowTime - lastTime >= 1500) {
                view.setTag(R.id.tag_click_time, nowTime);
                LogUtils.e(view.getId() + "set_old_nowTime", nowTime);
                return false;
            } else {
                return true;
            }
        } else {
            LogUtils.e(view.getId() + "set_new_nowTime", nowTime);
            view.setTag(R.id.tag_click_time, nowTime);
            return false;
        }
    }


    /**
     * 防止button快速连点
     *
     * @return
     */
    public static boolean isNotFastClick() {
        boolean flag = false;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = currentClickTime;
        return flag;
    }


    /**
     * 验证手机格式
     * 2018-01-17 增加 166 号段
     */
    public static boolean isMobileNumber(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186
         * 电信：133、153、180、189、（1349卫通）、170、177
         * 总结起来就是第一位必定为1，第二位必定为3、5、7、8，其他位置的可以为0-9
         *
         */
        String telRegex = "[1][3456789]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }

    }

    /**
     * 保存文件，写入内容
     *
     * @param filePath
     * @param fileName
     * @param fileContext
     * @return
     */
    public static boolean saveFile(String filePath, String fileName, String fileContext) {
        FileWriter fw = null;
        PrintWriter pw = null;
        try {

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String fileStr = filePath + fileName;
                File parentF = new File(filePath);
                if (!parentF.exists()) {
                    parentF.mkdirs();
                }
                File f = new File(fileStr);
                if (!f.exists()) {
                    f.createNewFile();
                }
                fw = new FileWriter(f, true);
                pw = new PrintWriter(fw);
                pw.println(fileContext);
                pw.flush();
                fw.flush();
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 获取应用崩溃日志
     *
     * @param context
     * @param ex
     * @return
     */
    public static String getCrashLog(Context context, Throwable ex) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            StringBuffer exceptionStr = new StringBuffer();
            exceptionStr.append("Version:" + info.versionName + "(" + info.versionCode + ")");
            exceptionStr.append("Android:" + Build.VERSION.RELEASE + "(" + Build.MODEL + ")\n");
            exceptionStr.append("Exception:" + ex.getMessage() + "\n");
            StackTraceElement[] elements = ex.getStackTrace();
            for (int i = 0; i < elements.length; i++) {
                exceptionStr.append(elements[i].toString() + "\n");
            }
            return exceptionStr.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取设备唯一ID 该方法是通过 IMEI 值，Build类的信息值，官方 Android 唯一ID，wifi的mac地址，
     * bt的mac地址拼接之后再MD5加密计算出来的唯一ID
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        //由于权限问题，READ_PHONE 权限暂时不要.
        //2016-08-04
//       TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        String m_szImei = TelephonyMgr.getDeviceId() == null ? "" : TelephonyMgr.getDeviceId();
        String m_szImei = "";
        String m_szDevIDShort = "35" + // we make this look like a valid IMEI
                Build.BOARD.length() % 10//
                + Build.BRAND.length() % 10//
                + Build.CPU_ABI.length() % 10//
                + Build.DEVICE.length() % 10//
                + Build.DISPLAY.length() % 10//
                + Build.HOST.length() % 10//
                + Build.ID.length() % 10//
                + Build.MANUFACTURER.length() % 10//
                + Build.MODEL.length() % 10//
                + Build.PRODUCT.length() % 10 //
                + Build.TAGS.length() % 10//
                + Build.TYPE.length() % 10//
                + Build.USER.length() % 10; // 13
        String m_szAndroidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) == null//
                ? "" //
                : Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        BluetoothAdapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String m_szBTMAC = m_BluetoothAdapter.getAddress();
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        String m_szLongID = m_szImei + m_szDevIDShort + m_szAndroidID + m_szWLANMAC + m_szBTMAC;
        // compute md5
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        // get md5 bytes
        byte p_md5Data[] = m.digest();
        // create a hex string
        String m_szUniqueID = new String();
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
            // if it is a single digit, make sure it have 0 in front (proper
            // padding)
            if (b <= 0xF)
                m_szUniqueID += "0";
            // add number to string
            m_szUniqueID += Integer.toHexString(b);
        } // hex string to uppercase
        m_szUniqueID = m_szUniqueID.toUpperCase();

        return m_szUniqueID;
    }


    /**
     * 判断网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 获取屏幕密度
     *
     * @param activity
     * @return
     */
    public static int DensityDpi(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.densityDpi;

    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
//        DisplayMetrics dm = new DisplayMetrics();
//        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
//        return dm.widthPixels;
        return ScreenUtils.getScreenWidth();
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
//        DisplayMetrics dm = new DisplayMetrics();
//        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
//        return dm.heightPixels;
        return ScreenUtils.getScreenHeight();
    }

    /**
     * 判断应用是否在前台运行
     *
     * @param context
     * @return
     */
    public static boolean appIsRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.processName.equals(context.getPackageName())) {
                    if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param mContext
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(1000);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    // 只能判断部分CJK字符（CJK统一汉字）
    public static boolean isChineseByREG(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
        return pattern.matcher(str.trim()).find();
    }

    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (//
                ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS //
                        || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS //
                        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A //
                        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B //
                        || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION //
                        || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS//
                        || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION//
        ) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否包含中文
     *
     * @param strName
     * @return
     */
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 0 一个数字
     * # 一个数字，不包括 0
     * . 小数的分隔符的占位符
     * , 分组分隔符的占位符
     * ; 分隔格式。
     * - 缺省负数前缀。
     * % 乘以 100 和作为百分比显示
     * ? 乘以 1000 和作为千进制货币符显示；用货币符号代替；如果双写，用
     * 国际货币符号代替。如果出现在一个模式中，用货币十进制分隔符代
     * 替十进制分隔符。
     * X 前缀或后缀中使用的任何其它字符，用来引用前缀或后缀中的特殊字符。
     * 例子：
     * DecimalFormat df1 = new DecimalFormat("0.0");
     * DecimalFormat df2 = new DecimalFormat("#.#");
     * DecimalFormat df3 = new DecimalFormat("000.000");
     * DecimalFormat df4 = new DecimalFormat("###.###");
     * System.out.println(df1.format(12.34));
     * System.out.println(df2.format(12.34));
     * System.out.println(df3.format(12.34));
     * System.out.println(df4.format(12.34));
     * 结果：
     * 12.3
     * 12.3
     * 012.340
     * 12.34
     *
     * @param object
     * @return
     */
    public final static String getDoubleLastDecimal(Object object) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(object);
    }

    /**
     * 32位小写md5加密
     *
     * @return md5(value) or ""
     */
    public final static String md5(String val) {
        try {
            String result = null;
            if (val != null && val.length() > 0) {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(val.getBytes(), 0, val.length());
                result = String.format("%032x", new BigInteger(1, md5.digest()));
            }
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * px 转为 dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * dip转为 px
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 判断是否为邮箱格式
     *
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (!str.contains("@")) {
            return false;
        }
        Pattern pattern = Pattern.compile(check);
        Matcher match = pattern.matcher(str);
        if (match.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检验email合格性
     *
     * @param email
     * @return
     */
    public static boolean isEmail2(String email) {

        if (TextUtils.isEmpty(email)) {
            return false;
        }

        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(email);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 判断特殊字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean StringFilter(String str) {
        // 只允许字母和数字
        // String regEx = "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        String cache = m.replaceAll("").trim();
        return !cache.equals(str);
    }

    /**
     * 去除特殊字符
     *
     * @return
     */
    public static String stringFilter(String str) {

        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 判读应用是否被安装
     *
     * @param context
     * @param packageName 应用包名
     * @return
     */
    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || packageName.isEmpty()) {
            return false;
        }

        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            if (info != null) {
                return true;
            } else {
                return false;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getEncode(String str) {
        String str_ = "";
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            str_ = URLEncoder.encode(str, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            str_ = "";
        }

        return str_;
    }


    public static String getDecode(String str) {
        String str_ = "";
        try {
            str_ = URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            str_ = "";
        }
        return str_;
    }

    public static boolean softInputStateHidden(Activity activity) {

        if (activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            return imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);// toggleSoftInput(0,
            // InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            return false;
        }
    }

    public static void softInputShowForced(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static boolean softInputShowImplicit(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 将bitmap存储file,如果文件存在 直接替换文件 jpg格式
     *
     * @param path 路径
     * @param bt   要存储的bitmap
     * @param name 文件名
     * @return
     * @throws IOException
     */
    public static File saveFile4Bitmap(Context context, String path, Bitmap bt, String name, Bitmap.CompressFormat format) throws Exception {
        if (context == null) {
            return null;
        }
        Bitmap bitmap = bt;
        // String suffix = "";
        // if (format == Bitmap.CompressFormat.PNG) {
        // suffix = ".png";
        // }
        // if (format == Bitmap.CompressFormat.JPEG) {
        // suffix = ".jpg";
        // }
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            throw new Exception("sdcard disable");
        }
        FileOutputStream b = null;


        // 创建文件路径
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 创建文件
        File fileName = new File(path + name);
        if (fileName.exists()) {
            fileName.delete();
        }
        fileName.createNewFile();
        try {
            b = new FileOutputStream(fileName);
            bitmap.compress(format, 100, b);// 把数据写入文件,并且不压缩
            exportToGallery(path + name, context);
        } catch (FileNotFoundException e) {
            throw e;
        } finally {
            b.flush();
            b.close();
        }
        return fileName;
    }

    public static Uri exportToGallery(String filename, Context context) {
        // Save the name and description of a video in a ContentValues map.
        final ContentValues values = new ContentValues(2);
        values.put(MediaStore.Video.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Video.Media.DATA, filename);
        // Add a new record (identified by uri)
        final Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + filename)));
        return uri;
    }

    public static File saveLogo(Context context) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Common.YH_LOGO_PATH;
        File f_cache_logo_path = new File(path);
        if (!f_cache_logo_path.exists()) {
            f_cache_logo_path.mkdirs();
        }
        File f_cache_logo = new File(path + "yihua_logo.png");
        if (f_cache_logo.exists()) {
            return f_cache_logo;
        } else {
            try {
                AndroidUtil.saveFile4Bitmap(context, path, BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher), "cloud_logo.png", Bitmap.CompressFormat.PNG);
                return f_cache_logo;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获得当前联网类型wifi or mobile
     *
     * @return -1: 无连接类型;
     * ConnectivityManager.TYPE_WIFI: wifi;
     * ConnectivityManager.TYPE_MOBILE:mobile。
     * 判断网络连接状态时mobile最好增加判断是net还是wap网络
     */
    public static int getNetWorkType(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            if (networkInfo != null)
                return networkInfo.getType();
            return -1;
        } else {
            return -1;
        }
    }

    /**
     * UTF-8 参数转码
     *
     * @param userInputStr
     * @return
     */
    public static String stringEncoder(String userInputStr, String TAG) {

        try {
            return URLEncoder.encode(userInputStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            LogUtils.e(TAG + "messageEncoder()>>>消息转码失败...", e);
            return "";
        }
    }


    public static String getJson(Context mContext, String fileName) {

        StringBuilder sb = new StringBuilder();
        AssetManager am = mContext.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(am.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {

            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
    }


    /**
     * 用户定位cityCode
     *
     * @param context
     * @return
     */
    public static String getCityCode(Context context) {
        return CommonUtils.INSTANCE.getLocationInfo()[0];

    }

    /**
     * 获取用户定位cityCode
     *
     * @param cityName
     * @param context
     * @return
     */
    public static String getCityCode(String cityName, Context context) {

        String cityCode = "";
//        if (!TextUtils.isEmpty(cityName)) {
//            SupportCitiesHelper helper = SupportCitiesHelper_.getInstance_(context);
//            SupportCitiesBean citiesBean = helper.getSpportCitiesBean(cityName);
//            cityCode = citiesBean.getCode();
//        }
        return TextUtils.isEmpty(cityCode) ? CommonUtils.INSTANCE.getLocationInfo()[2] : cityCode;
    }


    public static void doJson(Context context) {
      /*  SupportCitiesDao dao = new SupportCitiesDao(context);
        InputStream inputStream = null;

        try {
            inputStream = context.getAssets().open("supportCities.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = "";
        try {
            s = readTextFromSDcard(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<SupportCitiesBean> supportCitiesBeen = dao.addObjectToList(s);
        dao.insertModelToDb(supportCitiesBeen, "大连市");
*/

    }

    /**
     * 将传入的is逐行解析读取
     *
     * @param is InputStream
     * @return
     * @throws Exception
     */
    private static String readTextFromSDcard(InputStream is) throws Exception {

        InputStreamReader reader = new InputStreamReader(is, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();
    }

    /**
     * 格式化时间 yyyy-MM-dd
     *
     * @param time
     * @return
     */
    public static String formatTime(String time) {

        String timeFormat;
        if (!TextUtils.isEmpty(time)) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = format.parse(time);
                timeFormat = format.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                timeFormat = "";
            }
        } else {
            return "";
        }
        return timeFormat;
    }






    /*
     * @param dateSec
     * @param praiseCount 好评数
     * @param commetCount
     * @return
     *
    public static int readCircleFun(long dateSec, int praiseCount, int commetCount) {
        int ret = 0;
        //当前时间毫秒数
        long currSec = Calendar.getInstance().getTimeInMillis();
        long middleMin = (currSec - dateSec) / 60000;
        if (middleMin > 0) {
            if (middleMin > 1440) {
                middleMin = 1440;
            }
            BigDecimal tempType = new BigDecimal(middleMin * 7.3);
            tempType = tempType.setScale(0, BigDecimal.ROUND_HALF_UP);
            int retTwo = Integer.valueOf(tempType.toString());
            int retOne = (praiseCount + commetCount) * 107 + 1;
            ret = retOne + retTwo;
        } else {
            ret = 0;
        }
        return ret;
    }*/

    /**
     * 反射修改tabLayout下划线长度，android4.2以后
     *
     * @param context context
     * @param tabs    TabLayout
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void changeUnderLineLength(Context context, TabLayout tabs) {
        Class<?> tablayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tablayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (tabStrip != null) {
            tabStrip.setAccessible(true);
        }
        LinearLayout ll_tab = null;
        try {
            if (tabStrip != null) {
                ll_tab = (LinearLayout) tabStrip.get(tabs);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (ll_tab != null) {
            for (int i = 0; i < ll_tab.getChildCount(); i++) {
                View child = ll_tab.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                params.setMarginStart(dip2px(context, 30f));
                params.setMarginEnd(dip2px(context, 30f));
                child.setLayoutParams(params);
                child.invalidate();
            }
        }
    }

    /**
     * 修改下划线长度
     *
     * @param context
     * @param tabLayout
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void changeUnderLineLength(Context context, TabLayout tabLayout, float margin) {
        Class<?> tablayout = tabLayout.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tablayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (tabStrip != null) {
            tabStrip.setAccessible(true);
        }
        LinearLayout ll_tab = null;
        try {
            if (tabStrip != null) {
                ll_tab = (LinearLayout) tabStrip.get(tabLayout);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (ll_tab != null) {
            for (int i = 0; i < ll_tab.getChildCount(); i++) {
                View child = ll_tab.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                params.setMarginStart(dip2px(context, margin));
                params.setMarginEnd(dip2px(context, margin));
                child.setLayoutParams(params);
                child.invalidate();
            }
        }
    }

    /**
     * String日期转换为Long
     *
     * @param formatDate("MM/dd/yyyy HH:mm:ss")
     * @param date("12/31/2013 21:08:00")
     * @return * @throws ParseException
     */
    public static Long dataStringToLong(String formatDate, String date) throws ParseException {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
        Date dt = sdf.parse(date);
        return dt.getTime();
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String longToString(long currentTime, String formatType) throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }


    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }


    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }


    public static String getVersionCode(Context context) {
        if (context == null) {
            return "63";
        } else {
            try {
                PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                return String.valueOf(info.versionCode);
            } catch (PackageManager.NameNotFoundException e) {
                return "";
            }
        }
    }


    /**
     * List集合去除重复数据
     *
     * @param arr 集合内model 必须重写hashCode equals
     * @return
     */
    public static ArrayList getList(List arr) {

        List list = new ArrayList();
        Iterator it = arr.iterator();

        while (it.hasNext()) {
            Object obj = (Object) it.next();

            if (!list.contains(obj)) { //不包含就添加
                list.add(obj);
            }
        }
        return (ArrayList) list;
    }

    /**
     * 两个Double数相加
     *
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double add(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.add(b2).doubleValue();
    }

    /**
     * 两个Double数相减
     *
     * @param v1
     * @param v2
     * @return Double
     * 12345.6789
     */
    public static Double sub(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.subtract(b2).doubleValue();
    }


    /**
     * 两个Double数相乘
     *
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double mul(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.multiply(b2).doubleValue();
    }


    /**
     * 两个Double数相除，并保留scale位小数
     *
     * @param v1
     * @param v2
     * @param scale
     * @return Double
     */
    public static Double div(Double v1, Double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 两个Double数相除，并保留scale位小数，不采取四舍五入
     *
     * @param v1
     * @param v2
     * @param scale
     * @return Double
     */
    public static Double div2(Double v1, Double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2, scale, BigDecimal.ROUND_UNNECESSARY).doubleValue();
    }


    /**
     * 格式化金额
     * 四舍五入保留两位
     *
     * @param money
     * @return
     */
    public static String formatDouble2(double money) {
        BigDecimal bigDec = new BigDecimal(money);
        double total = bigDec.setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(total);
    }

    /**
     * 强制隐藏键盘
     *
     * @param view
     * @param activity
     */
    public static void hideKeyboard(View view, Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    public static double string2Double(String doubleString) {

        double money = 0;
        if (!TextUtils.isEmpty(doubleString)) {
            try {
                money = Double.valueOf(doubleString);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return money;
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, BuildConfig.WECHAT_APPID, false);
        if (iwxapi.isWXAppInstalled()) {
            return true;
        } else {
            ToastUtils.showShort("检测到手机未安装微信");
            return false;
        }
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWXAppInstalled(Context context) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, BuildConfig.WECHAT_APPID, false);
        if (iwxapi.isWXAppInstalled()) {
            return true;
        } else {
            return false;
        }
    }


    public static String encryptPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return "";
        } else {
            return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
    }

    /**
     * 获取指定字符串出现的次数
     *
     * @param srcText  源字符串
     * @param findText 要查找的字符串
     * @return
     */
    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        Pattern p = Pattern.compile(findText);
        Matcher m = p.matcher(srcText);
        while (m.find()) {
            count++;
        }
        return count;
    }

    public static boolean isHuaWei() {
        return Build.MANUFACTURER.equals("HUAWEI");
    }


    /**
     * 过滤空字符串
     *
     * @param oldString
     * @return
     */
    public static String filterTextEmpty(String oldString) {
        if (TextUtils.isEmpty(oldString)) {
            return "";
        } else {
            return oldString;
        }
    }

    /**
     * 过滤空字符串
     *
     * @param oldString
     * @return
     */
    public static String filterMoneyTextEmpty(String oldString) {
        if (TextUtils.isEmpty(oldString) || oldString.equals("null")) {
            return "0";
        } else {
            return oldString;
        }
    }


    public static boolean judgeUrlStr(String urlStr) {

        if (TextUtils.isEmpty(urlStr)) {
            return false;
        } else if (urlStr.startsWith("http://") || urlStr.startsWith("https://") || urlStr.startsWith("www.")) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * Rgb 565 压缩
     * @return
     *
    private Bitmap compressRGB565(Bitmap bitmap,Co) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.de

        return  BitmapFactory.decodeResource(getResources(), bitmap, options);
    }
    */

    /**
     * 得到bitmap的大小
     */
    public static int getBitmapSizeKb(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount() / 1024;
        }
        return bitmap.getByteCount() / 1024;
    }

    /**
     * 质量压缩
     *
     * @param bitmap
     * @return
     */
    public static Bitmap compressBitmap(Bitmap bitmap) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        int options = 80;
        // 循环判断如果压缩后图片是否大于128kb,大于继续压缩
        while (outputStream.toByteArray().length / 1024 > 128) {
            // 重置 outputStream 即清空 outputStream
            outputStream.reset();
            // 这里压缩options%，把压缩后的数据存放到 outputStream 中
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
            options -= 5;// 每次都减少5
        }

        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(outputStream.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);
    }


    /**
     * 传入图片地址进入保存
     */
    public static void saveBitmapImage(Bitmap bitmap, Activity activityContext) {

        if (bitmap == null || activityContext == null) {
            Toast.makeText(activityContext, "图片保存失败", Toast.LENGTH_SHORT).show();
            return;
        }
        String pathFile = Environment.getExternalStorageDirectory().getAbsolutePath() + Common.IMAGE_SAVE_PATH;
        String fileName = AndroidUtil.md5(pathFile) + System.currentTimeMillis() + ".png";
        try {
            File aveFile = AndroidUtil.saveFile4Bitmap(activityContext, pathFile, bitmap, fileName, Bitmap.CompressFormat.PNG);
            // 更新图库
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(aveFile);
            intent.setData(uri);
            //这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你
            activityContext.sendBroadcast(intent);
            Toast.makeText(activityContext, "操作成功，图片保存路径：" + pathFile + fileName, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(activityContext, "图片保存失败", Toast.LENGTH_SHORT).show();
        }
    }


  /*  public static void saveWebViewImage(Context context, WebView webView) {

        WebView.HitTestResult result = webView.getHitTestResult();
        if (result == null) {
            Toast.makeText(context, context.getString(R.string.save_photo_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (result.getType() != WebView.HitTestResult.IMAGE_TYPE) {
            return;
        }
        String imgurl = result.getExtra();

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.common_dialog);
        builder.setMessage(context.getString(R.string.save_photo_message));

        builder.setPositiveButton(context.getString(R.string.confirm), (dialog, which) -> {
            Bitmap bit = ImageLoader.getInstance().getMemoryCache().get(imgurl);
            if (bit != null) {
                String pathFile = Environment.getExternalStorageDirectory().getAbsolutePath() + Common.YH_IMAGE_SAVE_PATH;
                String fileName = AndroidUtil.md5(imgurl) + ".png";
                try {
                    AndroidUtil.saveFile4Bitmap(context, pathFile, bit, fileName, Bitmap.CompressFormat.PNG);
                    Toast.makeText(context, context.getString(R.string.save_photo_success), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(context, context.getString(R.string.save_photo_error), Toast.LENGTH_SHORT).show();
                }
            } else {
                String fileName = AndroidUtil.md5(imgurl);
                SaveImageUtils saveImageUtils = new SaveImageUtils(0, imgurl, fileName, (result1, type1, code, filePath, smallFilePath) -> {
                    if (code == SaveImageUtils.SUCCESS_10003 || code == SaveImageUtils.SUCCESS_10004) {
                        Toast.makeText(context, context.getString(R.string.save_photo_success), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, context.getString(R.string.save_photo_error), Toast.LENGTH_SHORT).show();
                    }
                });
                saveImageUtils.execute();
            }
            dialog.dismiss();
        });
        builder.setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> {
            dialog.dismiss();
        });
        builder.create().show();
    }

    public static void saveWebViewImage(Context context, com.tencent.smtt.sdk.WebView webView) {


        com.tencent.smtt.sdk.WebView.HitTestResult result = webView.getHitTestResult();
        if (result == null) {
            Toast.makeText(context, context.getString(R.string.save_photo_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (result.getType() != WebView.HitTestResult.IMAGE_TYPE) {
            return;
        }
        String imgurl = result.getExtra();

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.common_dialog);
        builder.setMessage(context.getString(R.string.save_photo_message));

        builder.setPositiveButton(context.getString(R.string.confirm), (dialog, which) -> {
            Bitmap bit = ImageLoader.getInstance().getMemoryCache().get(imgurl);
            if (bit != null) {
                String pathFile = Environment.getExternalStorageDirectory().getAbsolutePath() + Common.YH_IMAGE_SAVE_PATH;
                String fileName = AndroidUtil.md5(imgurl) + ".png";
                try {
                    AndroidUtil.saveFile4Bitmap(context, pathFile, bit, fileName, Bitmap.CompressFormat.PNG);
                    Toast.makeText(context, context.getString(R.string.save_photo_success), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(context, context.getString(R.string.save_photo_error), Toast.LENGTH_SHORT).show();
                }
            } else {
                String fileName = AndroidUtil.md5(imgurl);
                SaveImageUtils saveImageUtils = new SaveImageUtils(0, imgurl, fileName, (result1, type1, code, filePath, smallFilePath) -> {
                    if (code == SaveImageUtils.SUCCESS_10003 || code == SaveImageUtils.SUCCESS_10004) {
                        Toast.makeText(context, context.getString(R.string.save_photo_success), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, context.getString(R.string.save_photo_error), Toast.LENGTH_SHORT).show();
                    }
                });
                saveImageUtils.execute();
            }
            dialog.dismiss();
        });
        builder.setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> {
            dialog.dismiss();
        });
        builder.create().show();
    }
*/

    /**
     * 去除字符串首尾出现的某个字符.
     *
     * @param source  源字符串.
     * @param element 需要去除的字符.
     * @return String.
     */
    public static String trimFirstAndLastChar(String source, char element) {
        boolean beginIndexFlag = true;
        boolean endIndexFlag = true;
        do {
            int beginIndex = source.indexOf(element) == 0 ? 1 : 0;
            int endIndex = source.lastIndexOf(element) + 1 == source.length() ? source.lastIndexOf(element) : source.length();
            source = source.substring(beginIndex, endIndex);
            beginIndexFlag = (source.indexOf(element) == 0);
            endIndexFlag = (source.lastIndexOf(element) + 1 == source.length());
        } while (beginIndexFlag || endIndexFlag);
        return source;
    }

    /**
     * 将Map转化为Json
     *
     * @param map
     * @return String
     */
    public static String mapToJson(Map<String, String> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }


    /**
     * 匹配Luhn算法：可用于检测银行卡卡号
     *
     * @param cardNo
     * @return
     */
    public static boolean checkBankCardNumber(String cardNo) {

        if (TextUtils.isEmpty(cardNo)) {
            return false;
        }

        int[] cardNoArr = new int[cardNo.length()];
        for (int i = 0; i < cardNo.length(); i++) {
            cardNoArr[i] = Integer.valueOf(String.valueOf(cardNo.charAt(i)));
        }
        for (int i = cardNoArr.length - 2; i >= 0; i -= 2) {
            cardNoArr[i] <<= 1;
            cardNoArr[i] = cardNoArr[i] / 10 + cardNoArr[i] % 10;
        }
        int sum = 0;
        for (int i = 0; i < cardNoArr.length; i++) {
            sum += cardNoArr[i];
        }
        return sum % 10 == 0;
    }


}
