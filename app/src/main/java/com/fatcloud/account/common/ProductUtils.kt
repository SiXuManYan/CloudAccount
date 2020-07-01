package com.fatcloud.account.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.*
import com.fatcloud.account.BuildConfig
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.feature.matisse.Glide4Engine
import com.fatcloud.account.feature.matisse.Matisse
import com.fatcloud.account.view.EditView
import com.fatcloud.account.view.dialog.AlertDialog
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.style.cityjd.JDCityConfig
import com.lljjcoder.style.cityjd.JDCityPicker
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhihu.matisse.MimeType
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.math.BigDecimal

/**
 * Created by Wangsw on 2020/6/12 0012 17:29.
 * </br>
 * 产品相关逻辑处理
 */
object ProductUtils {


    //Reactive收集
    private var compositeDisposable: CompositeDisposable? = null


    /**
     * 处理经营范围pid
     *  ArrayList<String> ->  ArrayList<Int>
     */
    fun stringList2IntList(selectPid: ArrayList<String>): ArrayList<Int> {
        val scope: ArrayList<Int> = ArrayList()
        selectPid.forEach {
            scope.add(it.toInt())
        }
        return scope
    }


    /**
     * 非空校验，提供震动和抖动反馈
     */
    fun checkEditEmptyWithVibrate(vararg args: EditView): Boolean {

        args.forEach {
            if (it.value().isEmpty()) {
                VibrateUtils.vibrate(10)
                it.startAnimation(CommonUtils.getShakeAnimation(2))
                return false
            }
        }
        return true
    }

    /**
     * 非空校验，提供震动和抖动反馈
     */
    fun checkViewValueEmpty(value: String, view: View): Boolean {

        if (TextUtils.isEmpty(value)) {
            VibrateUtils.vibrate(10)
            view.startAnimation(CommonUtils.getShakeAnimation(2))
            return false
        }
        return true
    }


    /**
     * 添加订阅
     * @param subscription 订阅
     */
    fun addSubscribe(subscription: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(subscription)
    }

    /**
     * 相册权限申请
     */
    fun requestAlbumPermissions(activity: Activity?): Boolean {
        var isGranted = false

        activity?.let {
            addSubscribe(
                RxPermissions(it)
                    .request(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .subscribe {
                        isGranted = true
                    })
        }

        return isGranted
    }


    /**
     * 相册选择
     * @param mediaType
     * @see Matisse.IMG
     * @see Matisse.GIF
     * @see Matisse.VIDEO
     * @see Constants.I1
     * @see Constants.I2
     */
    fun handleMediaSelect(activity: Activity, mediaType: Int, @IdRes fromViewId: Int) {

        //
        PermissionUtils.permissionAny(
            activity, PermissionUtils.OnPermissionCallBack { granted ->
                if (granted) {
                    Matisse.from(activity)
                        .choose(if (mediaType == 0) MimeType.ofAll() else with(MimeType.ofImage()) {
                            remove(MimeType.GIF)
                            this
                        }, true)
                        .countable(true)
//                .originalEnable(false)
                        .maxSelectable(1)
                        .theme(R.style.Matisse_Dracula)
                        .thumbnailScale(0.87f)
                        .imageEngine(Glide4Engine())
                        .forResult(Constants.REQUEST_MEDIA, mediaType, fromViewId)

                } else {
                    showPermissionFailure(activity)
                }
            }, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )


    }

    fun handleMediaSelectForFragment(fragment: Fragment, mediaType: Int, @IdRes fromViewId: Int) {

        PermissionUtils.permissionAny(
            fragment.activity, PermissionUtils.OnPermissionCallBack { granted ->
                if (granted) {
                    Matisse.from(fragment)
                        .choose(if (mediaType == 0) MimeType.ofAll() else with(MimeType.ofImage()) {
                            remove(MimeType.GIF)
                            this
                        }, true)
                        .countable(true)
                        //                .originalEnable(false)
                        .maxSelectable(1)
                        .theme(R.style.Matisse_Dracula)
                        .thumbnailScale(0.87f)
                        .imageEngine(Glide4Engine())
                        .forResult(Constants.REQUEST_MEDIA, mediaType, fromViewId)

                } else {
                    showPermissionFailure(fragment.context)
                }
            }, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    }


    private fun showPermissionFailure(context: Context?) {
        AlertDialog.Builder(context).setTitle(R.string.hint)
            .setMessage(R.string.album_need_permission)
            .setCancelable(false)
            .setPositiveButton(
                R.string.yes,
                AlertDialog.STANDARD,
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                    AppUtils.launchAppDetailsSettings()
                })
            .setNegativeButton(
                R.string.no,
                AlertDialog.STANDARD,
                DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
            .create()
            .show()
    }

    fun getEditValueToBigDecimal(editValue: String): BigDecimal {
        return if (editValue.isNullOrBlank()) {
            BigDecimal.ZERO
        } else {
            try {
                BigDecimal(BigDecimal(editValue).stripTrailingZeros().toPlainString())
            } catch (e: Exception) {
                BigDecimal.ZERO
            }
        }
    }


    /**
     * 城市信息选择
     */
    fun showLocationPicker(context: Context?, listener: OnCityItemClickListener) {
        val jdCityConfig = JDCityConfig.Builder().build().apply {
            showType = JDCityConfig.ShowType.PRO_CITY_DIS
        }

        val cityPicker = JDCityPicker().apply {
            init(context)
            setConfig(jdCityConfig)
            setOnCityItemClickListener(listener)
            showCityPicker()
        }
    }


    /**
     * 是否为加密url
     */
    fun isOssSignUrl(url: String): Boolean {
        if (url.contains(BuildConfig.OSS_PRIVATE_BUCKET_NAME) || !RegexUtils.isURL(url)) {
            return true
        }
        return false
    }

    /**
     * https://ftacloud-bucket-private.oss-cn-qingdao.aliyuncs.com/android/dev/image/encryption/image_1592462141234.jpg
     * oss-cn-qingdao.aliyuncs.com
     * 获取加密图片的 Oss object key
     */
    fun getOssSignUrlObjectKey(url: String): String {

        if (url.contains(BuildConfig.OSS_PRIVATE_BUCKET_NAME)) {
            return url.replace(BuildConfig.OSS_PRIVATE_BUCKET_NAME, "")
        } else {
            return url
        }


    }


    /**
     * 获取OSS 签名
     */
    fun getRealOssUrl(
        context: Context?,
        url: String,
        ossCallBack: CloudAccountApplication.OssSignCallBack
    ) {

        val activity = context as Activity
        val application = activity.application as CloudAccountApplication

        application.getOssSecurityTokenForSignUrl(getOssSignUrlObjectKey(url), ossCallBack)

    }

    /**
     * 验证 银行卡号
     */
    fun isBankCardNumber(cardString: String, typeString: String? = ""): Boolean {
        val match = RegexUtils.isMatch("^[1-9]\\d{9,29}\$", cardString)
        if (!match) {
            ToastUtils.showShort(
                StringUtils.getString(
                    R.string.bank_number_wrong_format,
                    typeString
                )
            )
        }
        return match
    }

    /**
     * 验证 手机号
     */
    fun isPhoneNumber(string: String, typeString: String? = ""): Boolean {
        val match = AndroidUtil.isMobileNumber(string)
        if (!match) {
            ToastUtils.showShort(
                StringUtils.getString(
                    R.string.phone_number_wrong_format,
                    typeString
                )
            )
        }
        return match
    }

    /**
     * 验证 身份证号
     * 支持1/2代(15位/18位数字)
     */
    fun isIdCardNumber(string: String, typeString: String? = ""): Boolean {
        val match = RegexUtils.isMatch(
            "(^\\d{8}(0\\d|10|11|12)([0-2]\\d|30|31)\\d{3}\$)|(^\\d{6}(18|19|20)\\d{2}(0[1-9]|10|11|12)([0-2]\\d|30|31)\\d{3}(\\d|X|x)\$)",
            string
        )

        if (!match) {

            ToastUtils.showShort(
                StringUtils.getString(
                    R.string.id_card_number_wrong_format,
                    typeString
                )
            )
        }
        return match
    }

    /**
     * 校验身份证 url是否为空
     */
    fun hasIdCardUrl(idCardUrl: String, isFaceUp: Boolean, typeString: String? = ""): Boolean {
        val nullOrEmpty = idCardUrl.isNotEmpty()
        if (!nullOrEmpty) {

            if (isFaceUp) {
                ToastUtils.showShort(
                    StringUtils.getString(R.string.id_card_front_empty_format, typeString)
                )
            } else {
                ToastUtils.showShort(
                    StringUtils.getString(R.string.id_card_back_empty_format, typeString)
                )
            }


        }
        return nullOrEmpty

    }


}