package com.fatcloud.account.common

import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Environment
import android.provider.ContactsContract
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.view.View
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.Interpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import com.fatcloud.account.R
//import com.amap.api.maps2d.AMapUtils
//import com.amap.api.maps2d.model.LatLng
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.*

//import org.jetbrains.anko.startActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import kotlin.collections.HashMap


object CommonUtils {

    private const val ALGORITHM = "PBKDF2WithHmacSHA1"
    private const val TRANSFORMATION = "AES/CBC/PKCS7Padding"
    private val AES_IV = byteArrayOf(0xA, 1, 0xB, 5, 4, 0xF, 7, 9, 0x17, 3, 1, 6, 8, 0xC, 0xD, 91)
    private val PASSWORD =
        charArrayOf('P', '$', 'r', ' ', 'v', '9', 'l', 'K', 'j', 'm', '2', 'D', 'u', 'c', '@', 's', ' ', 'L', 'a', 'b', 'F', 'n')
    private val SALT = byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF)
    private val keyHashMap = HashMap<String, Pair<String, String>>()


    fun getKeyParameter(key: String): Pair<String, String>? {
        return keyHashMap[key]
    }

    fun removeKeyParameter(key: String): Pair<String, String>? {
        return keyHashMap.remove(key)
    }


    /**
     * 加密字符生成秘钥
     * @param timeInterval 时间差
     * @param account 账号（电话）
     * @param actionType 操作类型
     *
     */
    fun encrypt(timeInterval: String, account: String, actionType: String): String {
        // 上一次登录成功时间
        val aesLoginTime = getShareDefault().getLong(Constants.SP_AES_LOGIN_TIME, 0)

        // 上一次登陆成功服务器返回时间
        val aesLoginServiceTime = getShareDefault().getLong(Constants.SP_AES_LOGIN_SERVICE_TIME, 0)

        // 时间戳
        val correctTime = aesLoginServiceTime + System.currentTimeMillis() - aesLoginTime

        val temp = StringBuilder()
        temp.append(correctTime)
        temp.append(Constants.DEVICE_ANDROID)
        temp.append(account)
        temp.append(",")
        temp.append(actionType)
        temp.append(",")
        temp.append(AppUtils.getAppVersionName())

        val secretKey = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(
            PBEKeySpec(
                PASSWORD, SALT,
                10000, 128
            )
        )

        val aes = EncryptUtils.encryptAES2Base64(
            temp.toString().toByteArray(),
            secretKey.encoded, TRANSFORMATION, AES_IV
        )
        return String(aes).apply {
            keyHashMap[this] = Pair(account, actionType)
        }
    }

    /**
     * 获取设备ID
     * @param context 上下文
     */
//    @SuppressLint("HardwareIds")
//    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
//    fun deviceId(context: Context): String {
//        val shortDevId = ("35" +
//                Build.BOARD.length % 10
//                + Build.BRAND.length % 10
//                + Build.CPU_ABI.length % 10
//                + Build.DEVICE.length % 10
//                + Build.DISPLAY.length % 10
//                + Build.HOST.length % 10
//                + Build.ID.length % 10
//                + Build.MANUFACTURER.length % 10
//                + Build.MODEL.length % 10
//                + Build.PRODUCT.length % 10
//                + Build.TAGS.length % 10 +
//                +Build.TYPE.length % 10
//                + Build.USER.length % 10)
//        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
//                ?: ""
//        val longId = shortDevId.plus(androidId).plus(
//                (context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager)
//                        .connectionInfo.macAddress) + BluetoothAdapter.getDefaultAdapter()?.address
//        val uniqueId = StringBuilder()
//        val mdBytes = EncryptUtils.encryptMD5(longId.toByteArray())
//        for (i in mdBytes.indices) {
//            val b = 0xFF and mdBytes[i].toInt()
//            // if it is a single digit, make sure it have 0 in front (proper  padding)
//            if (b <= 0xF) {
//                uniqueId.append("0")
//            }
//            // add number to string
//            uniqueId.append(Integer.toHexString(b))
//        } // hex string to uppercase
//
//        return uniqueId.toString().toUpperCase()
//    }

    /**
     * 获取默认的SharePreference
     */
    fun getShareDefault() = SPUtils.getInstance(Constants.PR_DEFAULT)!!

    /**
     * 获取定位的SharePreference
     */
    fun getShareLocation() = SPUtils.getInstance(Constants.PR_LOCATION)!!

    /**
     * 获取定位信息
     * @return 0 选择城市代码, 1 选择城市名，2 定位城市代码, 3 定位城市名，4 经度，5 纬度，6 地址
     */
    fun getLocationInfo(): Array<String> {
        val location = getShareLocation()
        return arrayOf(
            location.getString(Constants.SP_SELECT_LOCAL_CODE),
            location.getString(Constants.SP_SELECT_LOCAL_NAME),
            location.getString(Constants.SP_LOCAL_CODE),
            location.getString(Constants.SP_LOCAL_NAME),
            location.getString(Constants.SP_LONGITUDE, "0"),
            location.getString(Constants.SP_LATITUDE, "0"),
            location.getString(Constants.SP_ADDRESS)
        )
    }

    fun getAutoPlayVideoSetting() = getShareDefault().getBoolean(Constants.SP_AUTO_PLAY_VIDEO)

    fun setAutoPlayVideoSetting(auto: Boolean) {
        getShareDefault().put(Constants.SP_AUTO_PLAY_VIDEO, auto)
    }


    /**
     * 根据经纬度算距离
     *
     * @param startPoint 起点
     * @param endPoint   终点
     * @return 距离
     */

//    fun calculateDistance(startPoint: LatLng, endPoint: LatLng): String {
//        val distance = AMapUtils.calculateLineDistance(startPoint, endPoint)
//
//        return if (distance >= 1000) {
//            val decimal = BigDecimal.valueOf(distance.toDouble() / 1000f).setScale(2, RoundingMode.HALF_UP)
//            var km = decimal.toString()
//            if (km.endsWith(".00")) {
//                km = km.substring(0, km.indexOf("."))
//            } else if (km.endsWith("0")) {
//                km = km.substring(0, km.length - 1)
//            }
//            "距${km}km"
//        } else {// [ 100,1000)
//            "距${distance.toInt()}m"
//        } /*else {
//            "100米以内"
//        }*/
//    }

    /**
     * 毫秒转换成时间戳
     * @param millis 毫秒
     */
    fun millisecondsToTimeSpan(millis: Long): String? {
//        val units = arrayOf("天", "小时", "分钟", "秒", "毫秒")
        val units = arrayOf("天", ":", ":", "")
        if (millis == 0L) return "0${units[units.size - 1]}"
        val sb = StringBuilder()
        var m = millis
        if (millis < 0) {
            sb.append("-")
            m = -millis
        }
        val unitLen = intArrayOf(TimeConstants.DAY, TimeConstants.HOUR, TimeConstants.MIN, TimeConstants.SEC)
        for (i in 0 until 4) {
            if (m >= unitLen[i]) {
                val mode = m / unitLen[i]
                m -= mode * unitLen[i]
                if (i != 0 && mode < 10) {
                    sb.append("0")
                }
                sb.append(mode).append(units[i])
            } else {
                if (i != 0) {
                    sb.append("00").append(units[i])
                }
            }
        }
        return sb.toString()
    }

    /**
     * 毫秒转换为时间格式
     * @param millis 毫秒
     */
    fun millisecondsToDuration(millis: Long): String {
        if (millis == 0L) return "0"
        val sb = StringBuilder()
        val unitLen = intArrayOf(TimeConstants.HOUR, TimeConstants.MIN, TimeConstants.SEC)
        var m = millis
        for (i in unitLen) {
            if (m >= i) {
                var mode = m / i
                m -= mode * i
                if (m > 500) {
                    mode = mode.inc()
                }
                sb.append(
                    if (mode < 10) {
                        "0".plus(mode)
                    } else {
                        mode
                    }
                )
            } else {
                sb.append("00")
            }
            sb.append(":")
        }
        return sb.deleteCharAt(sb.length - 1).toString()
    }

    fun millisToDay(millis: Long): Long {
        return ((System.currentTimeMillis() - millis) / 86400000)
    }

    fun dateToTimeSpan(dateTime: String?): String {
        if (dateTime.isNullOrEmpty()) {
            return ""
        }
        val currentMillisecond = System.currentTimeMillis()
        val dateMillis = TimeUtils.string2Millis(dateTime)
        //间隔秒
        val millisLeft = currentMillisecond - dateMillis

        //一分钟之内
        return when {
            millisLeft < TimeConstants.MIN -> "刚刚"
            millisLeft < TimeConstants.HOUR -> "${millisLeft / TimeConstants.MIN}分钟前"
            millisLeft < TimeConstants.DAY -> "${millisLeft / TimeConstants.HOUR}小时前"
            millisLeft < TimeConstants.DAY * 30L -> "${millisLeft / TimeConstants.DAY}天前"
            millisLeft < TimeConstants.DAY * 365L -> "${millisLeft / (TimeConstants.DAY * 30L)}个月前"
            else -> TimeUtils.millis2String(dateMillis, SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()))
        }
    }

    /**
     * 获取缩略图压缩比
     */
    fun getThumbnailSizeUri(): String {
        return when {
            ScreenUtils.getScreenWidth() <= 720 -> "?imageMogr2/thumbnail/!50p"
            ScreenUtils.getScreenWidth() <= 1080 -> "?imageMogr2/thumbnail/!75p"
            else -> "?imageMogr2/thumbnail/!95p"
        }
    }

    /**
     * 获取缩略图压缩比
     */
    fun getThumbnailUriBySize(size: Int): String {
        return when (size) {
            120 -> "-smallWidth120.jpg"
            360 -> "-smallWidth360.jpg"
            720 -> "-smallWidth720.jpg"
            else -> "-smallWidth360.jpg"
        }
    }

    fun getThumbnail(): String {
        return "?imageMogr2/thumbnail/!10p"
    }


    /**
     * 压缩图片
     */
    fun compressImage(bitmap: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var options = 100
        while (baos.toByteArray().size / 1024 > 3072) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset()//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos)//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10//每次都减少10
        }
        //把压缩后的数据baos存放到ByteArrayInputStream中
        return baos.toByteArray()
    }

    /**
     * 获取缓存目录
     */
    fun getFileCachedPath() = (if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
        Utils.getApp().externalCacheDir!!.path
    } else {
        Utils.getApp().cacheDir.path
    })!!

    private const val EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE"

    fun getOwnCacheDirectory(context: Context, cacheDir: String): String? {
        var appCacheDir: File? = null
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() && hasExternalStoragePermission(context)) {
            appCacheDir = File(Environment.getExternalStorageDirectory(), cacheDir)
        }
        if (appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
            appCacheDir = context.cacheDir
        }
        return appCacheDir?.path
    }

    private fun hasExternalStoragePermission(context: Context): Boolean {
        val perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION)
        return perm == PackageManager.PERMISSION_GRANTED
    }

    fun bytesToMBSize(byteNum: Long): String {
        return if (byteNum <= 0) {
            "0M"
        } else {
            String.format(Locale.getDefault(), "%.1fM", byteNum.toDouble() / 1048576)
        }
    }

    /**
     * 把两个位图覆盖合成为一个位图，以底层位图的长宽为基准
     * @param backBitmap 在底部的位图
     * @param frontBitmap 盖在上面的位图
     * @return
     */
    fun mergeBitmap(backBitmap: Bitmap, frontBitmap: Bitmap, x: Int, y: Int): Bitmap {
        val bitmap = backBitmap.copy(Bitmap.Config.RGB_565, true)
        val canvas = Canvas(bitmap)
        val baseRect = Rect(0, 0, backBitmap.width, backBitmap.height)
        val frontRect = Rect(x, y, frontBitmap.width, frontBitmap.height)
        canvas.drawBitmap(frontBitmap, frontRect, baseRect, null)
        return bitmap
    }

    /**
     * 设置状态栏透明
     * @param activity 不解释
     */
    fun setStatusBarTransparent(activity: Activity) {
        BarUtils.setStatusBarColor(activity, Color.TRANSPARENT, true)
        BarUtils.setStatusBarLightMode(activity, false)
        BarUtils.subtractMarginTopEqualStatusBarHeight(activity.findViewById(android.R.id.content))
    }

    /**
     * 设置状态栏透明
     * @param activity 不解释
     */
    fun setStatusBarTransparentWithLightMode(activity: Activity) {
        BarUtils.setStatusBarColor(activity, Color.TRANSPARENT, true)
        BarUtils.setStatusBarLightMode(activity, true)
        BarUtils.subtractMarginTopEqualStatusBarHeight(activity.findViewById(android.R.id.content))
    }

    /**
     * 通讯录翻译
     *
     * @param phoneNum 手机号
     * @return 手机号对应的姓名
     */
    fun translatePhone(context: Context, phoneNum: String): String {

        var displayName = ""
        val projection =
            arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER)
        val phone1 = phoneNum.subSequence(0, 3).toString() + " " + phoneNum.substring(3, 7) + " " + phoneNum.substring(
            7,
            11
        )// 136 6666 6666
        val phone2 = phoneNum.subSequence(0, 3).toString() + "-" + phoneNum.substring(3, 7) + "-" + phoneNum.substring(
            7,
            11
        )// 136-6666-6666
        val phone3 = "+86$phoneNum"// +8613666666666
        val phone4 = "+86 " + phoneNum.subSequence(0, 3).toString() + " " + phoneNum.substring(3, 7) + " " + phoneNum.substring(
            7,
            11
        )// +86 136 6666 6666

        val resolver = context.contentResolver
        val cursor = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            ContactsContract.CommonDataKinds.Phone.NUMBER + " in(?,?,?,?,?)",
            arrayOf(phoneNum, phone1, phone2, phone3, phone4), null
        )
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                cursor.close()
            }
        }
        return displayName
    }

    /**
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param time
     * @return
     */
    fun getTimeLag(time: String): String {
        var spaceTime = ""
        if (!TextUtils.isEmpty(time)) {
            val aLong = timeStrToSecond(time)
            spaceTime = getSpaceTime(aLong)
        }
        return spaceTime
    }

    /**
     * 将时间转化成毫秒
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param time
     * @return
     */
    fun timeStrToSecond(time: String): Long? {
        try {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return format.parse(time).time
        } catch (e: Exception) {
            e.printStackTrace()
            return -1L
        }

    }


    /**
     * 获取时间间隔
     *
     * @param millisecond
     * @return
     */
    fun getSpaceTime(millisecond: Long?): String {

        val currentMillisecond = System.currentTimeMillis()

        //间隔秒
        val spaceSecond = (currentMillisecond - millisecond!!) / 1000

        //一分钟之内
        return if (spaceSecond < 60) {
            "刚刚"
        } else if (spaceSecond / 60 > 0 && spaceSecond / 60 < 60) {
            (spaceSecond / 60).toString() + "分钟前"
        } else if (spaceSecond / (60 * 60) > 0 && spaceSecond / (60 * 60) < 24) {
            (spaceSecond / (60 * 60)).toString() + "小时前"
        } else if (spaceSecond / (60 * 60 * 24) > 0 && spaceSecond / (60 * 60 * 24) < 30) {
            (spaceSecond / (60 * 60 * 24)).toString() + "天前"
        } else if (spaceSecond / (60 * 60 * 24 * 30) > 0 && spaceSecond / (60 * 60 * 24 * 30) < 12) {
            (spaceSecond / (60 * 60 * 24 * 30)).toString() + "月前"
        } else {
            getDateTimeFromMillisecond(millisecond)
        }
    }

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param millisecond
     * @return
     */
    fun getDateTimeFromMillisecond(millisecond: Long?): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(millisecond!!)
        return simpleDateFormat.format(date)
    }


    /**
     * 获取联系人
     *
     * @param context
     * @return xingming
     */
    fun getContact(context: Context): Map<String, String> {
        val map = HashMap<String, String>()
        try {
            val PHONES_PROJECTION =
                arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER)
            val resolver = context.contentResolver
            val cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    var phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    phone = phone.replace(" ", "")
                    phone = phone.replace("-", "")

                    if (phone.length >= 11) {
                        if (phone.substring(0, 3) == "+86") {
                            phone = phone.substring(3, phone.length)
                        }
                        if (phone.substring(0, 2) == "86") {
                            phone = phone.substring(2, phone.length)
                        }
                        if (phone.length != 11 || phone.substring(0, 1) != "1") {
                            continue
                        }
                        map[phone] = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    }
                }
                cursor.close()
            }
        } catch (e: Exception) {

        }

        return map
    }

    /**
     * 通讯录 手机号转换
     *
     * @param contactPhone
     * @return xxx, xxxx, xxxx, xxx
     */
    fun handleConnect(contactPhone: Set<String>): String {
        var result = ""
        if (contactPhone.isEmpty()) {
            return result
        }
        val builder = StringBuilder()
        for (phone in contactPhone) {
            builder.append(phone)
            builder.append(",")
        }
        result = builder.substring(0, builder.length - 1)
        return result
    }

    fun call(context: Context, phone: String) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort("暂无联系方式")
            return
        }
        val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        context.startActivity(phoneIntent)
    }

    fun propertyAnim(
        view: View,
        millis: Long,
        rotationStart: Float,
        rotationEnd: Float,
        animationListener: AnimatorListener?,
        value: Interpolator
    ) {
        val rotate = PropertyValuesHolder.ofFloat("rotation", rotationStart, rotationEnd)!!
        val animator = ObjectAnimator.ofPropertyValuesHolder(view, rotate)
        if (animationListener != null) {
            animator.addListener(animationListener)
        }
        animator.interpolator = value
        animator.duration = millis
        animator.start()
    }


    fun checkSearchWords(keyword: String) = RegexUtils.isMatch("[a-zA-Z0-9\\u4e00-\\u9fa5]+", keyword)

    fun removeInvalidate(keyword: String): String {
        val matcher =
            Pattern.compile("\"[`~!@#\$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]\"").matcher(keyword)
        return matcher.replaceAll("").trim()
    }

    /**
     * 输出合适的价格标签
     */
    fun getPrettyNumber(number: BigDecimal?) = (if (number == null) {
        "0"
    } else {
        number.stripTrailingZeros().toPlainString()
    })!!

    /**
     * 对应价格的数字处理
     *
     * @param number 数
     * @return
     */
    fun getPriceFormat(number: BigDecimal?): String {
        if (number == null || number.compareTo(BigDecimal.ZERO) == 0) {
            return "0"
        }
        return if (BigDecimal(number.toInt()).compareTo(number) == 0) {
            number.toInt().toString() + ".0"
        } else {
            number.stripTrailingZeros().toPlainString()
        }
    }

    /**
     * 如果是整数取整数不是返回toPlainString
     *
     * @param number 数
     * @return
     */
    fun getIntFormat(number: BigDecimal?): String {
        if (number == null || number.compareTo(BigDecimal.ZERO) == 0) {
            return "0"
        }
        return if (BigDecimal(number.toInt()).compareTo(number) == 0) {
            number.toInt().toString()
        } else {
            number.stripTrailingZeros().toPlainString()
        }
    }

    /**
     * 如果是整数取整数不是返回toPlainString
     *
     * @param number 数
     * @return
     */
    fun getIntFormat(stringNumber: String?): String {
        var number: BigDecimal? = null
        try {
            number = BigDecimal(stringNumber)
        } catch (e: NullPointerException) {

        } catch (e: NumberFormatException) {

        }
        if (number == null || number.compareTo(BigDecimal.ZERO) == 0) {
            return "0"
        }
        return if (BigDecimal(number.toInt()).compareTo(number) == 0) {
            number.toInt().toString()
        } else {
            number.stripTrailingZeros().toPlainString()
        }
    }

    /**
     * 返回格式 1.2W
     *
     * @param number 数
     * @return
     */
    fun getIntTOTenThousand(number: Int?): String {
        if (number == null) {
            return "0"
        }
        val format = number / 10000f
        if (format < 1) {
            return number.toString()
        }
        return ((format * 10).toInt() / 10f).toString() + "w"
    }

    /**
     * 返回格式 1.2K
     *
     * @param number 数
     * @return
     */
    fun getIntTOThousand(number: Int?): String {
        if (number == null) {
            return "0"
        }
        val format = number / 1000f
        if (format < 1) {
            return number.toString()
        }
        return ((format * 10).toInt() / 10f).toString() + "k"
    }

    /**
     * 折扣
     *
     * @param number 折扣
     * @return
     */
    fun getDiscountFormat(number: String?): String {
        if (number.isNullOrEmpty()) {
            return "0"
        }
        return if (!number.contains(".")) {
            "$number.0"
        } else {
            val value = number.toFloat() * 10
            val result = BigDecimal(value.toDouble()).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()
            if (result.contains(".")) result else "$result.0"
        }
    }


    fun isDoubleClick(view: View): Boolean {
        val nowTime = System.currentTimeMillis()
        val value = view.getTag(R.id.tag_click_time)
        if (value != null) {
            val lastTime = value as Long
            if (nowTime - lastTime >= Constants.CLICK_INTERVAL) {
                view.setTag(R.id.tag_click_time, nowTime)
                return false
            } else {
                return true
            }
        } else {
            view.setTag(R.id.tag_click_time, nowTime)
            return false
        }
    }

    /**
     * 设置格式化金额
     *
     * @param textView
     * @param text1       单位
     * @param text2       金额
     * @param text1DpSize 单位字体大小 dp
     * @param text2DpSize 金额字体大小 dp
     */
    fun setFormatText(textView: TextView, text1: String, text2: String, text1DpSize: Int, text2DpSize: Int) {

        val stringBuilder = SpannableStringBuilder(text1 + text2)
        stringBuilder.setSpan(AbsoluteSizeSpan(text1DpSize, true), 0, text1.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        stringBuilder.setSpan(
            AbsoluteSizeSpan(text2DpSize, true),
            text1.length,
            (text1 + text2).length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = stringBuilder
    }


    fun getFriendlyTime(tv: TextView) {

        val hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        when {
            hours < 11 -> {
                tv.text = "嗨，早上好"
            }
            hours < 13 -> {
                tv.text = "嗨，中午好"
            }
            hours < 18 -> {
                tv.text = "嗨，下午好"
            }
            else -> {
                tv.text = "嗨，晚上好"
            }
        }
    }


    public fun getTestUrl(): Int {

        val randoms = (1..14).random()
        val id: Int

        when (randoms) {
            1 -> {
                id = R.mipmap.test1
            }
            2 -> {
                id = R.mipmap.test2
            }
            3 -> {
                id = R.mipmap.test3
            }
            4 -> {
                id = R.mipmap.test4
            }
            5 -> {
                id = R.mipmap.test5
            }
            6 -> {
                id = R.mipmap.test6
            }
            7 -> {
                id = R.mipmap.test7
            }
            8 -> {
                id = R.mipmap.test8
            }
            9 -> {
                id = R.mipmap.test9
            }
            10 -> {
                id = R.mipmap.test10
            }
            11 -> {
                id = R.mipmap.test11
            }
            12 -> {
                id = R.mipmap.test12
            }
            13 -> {
                id = R.mipmap.test13
            }
            else -> {
                id = R.mipmap.test14
            }
        }
        return id
    }


    /**
     * 晃动动画
     *
     * @param counts 1秒钟晃动多少下
     * @return 动画
     */
    fun getShakeAnimation(counts: Int): Animation? {
        val translateAnimation: Animation = TranslateAnimation(0f, 10f, 0f, 0f)
        translateAnimation.interpolator = CycleInterpolator(counts.toFloat())
        translateAnimation.duration = 500
        return translateAnimation
    }


    /**
     * 订单支付状态
     */
    fun setPaymentStatus(payState: String, statusImage: ImageView, statusTextView: TextView) {
        when (payState) {
            "OS1" -> {
                statusImage.setImageResource(R.drawable.ic_status_daizhifu)
                statusTextView.text = "待支付"
            }
            "OS2" -> {
                statusImage.setImageResource(R.drawable.ic_status_dingdanshixiao)
                statusTextView.text = "取消订单"
            }
            "OS3" -> {
                statusImage.setImageResource(R.drawable.ic_status_dingdanshixiao)
                statusTextView.text = "订单实效"
            }
            "OS4" -> {
                statusImage.setImageResource(R.drawable.ic_status_zhifuzhong)
                statusTextView.text = "支付中"
            }
            "OS5" -> {
                statusImage.setImageResource(R.drawable.ic_status_yibanjie)
                statusTextView.text = "已支付"
            }

            "OS6" -> {
                statusImage.setImageResource(R.drawable.ic_status_banlizhong)
                statusTextView.text = "已受理"
            }
            "OS7" -> {
                statusImage.setImageResource(R.drawable.ic_status_banlizhong)
                statusTextView.text = "办理中"
            }
            "OS8" -> {
                statusImage.setImageResource(R.drawable.ic_status_yibanjie)
                statusTextView.text = "已办结"
            }
            "OW1" -> {
                statusImage.setImageResource(R.drawable.ic_status_banlizhong)
                statusTextView.text = "已激活"
            }
            "OW2" -> {
                statusImage.setImageResource(R.drawable.ic_status_banlizhong)
                statusTextView.text = "办理中"
            }
            "OW3" -> {
                statusImage.setImageResource(R.drawable.ic_status_yibanjie)
                statusTextView.text = "已办结"
            }
            "OW4" -> {
                statusImage.setImageResource(R.drawable.ic_status_dingdanshixiao)
                statusTextView.text = "未激活"
            }


            else -> {
                statusImage.visibility = View.INVISIBLE
                statusTextView.visibility = View.INVISIBLE
            }
        }
    }


}