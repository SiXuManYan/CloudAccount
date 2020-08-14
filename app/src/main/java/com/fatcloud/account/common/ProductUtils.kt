package com.fatcloud.account.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.IDCardParams
import com.baidu.ocr.sdk.model.IDCardResult
import com.blankj.utilcode.util.*
import com.fatcloud.account.BuildConfig
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.entity.form.p9p10.NativeFormPersonalPackageP9P10Draft
import com.fatcloud.account.entity.local.form.*
import com.fatcloud.account.extend.LimitInputTextWatcher
import com.fatcloud.account.feature.gallery.GalleryActivity
import com.fatcloud.account.feature.matisse.Glide4Engine
import com.fatcloud.account.feature.matisse.Matisse
import com.fatcloud.account.feature.ocr.RecognizeIDCardResultCallBack
import com.fatcloud.account.view.EditView
import com.fatcloud.account.view.dialog.AlertDialog
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.style.cityjd.JDCityConfig
import com.lljjcoder.style.cityjd.JDCityPicker
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhihu.matisse.MimeType
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

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
                    showPermissionFailure(activity, StringUtils.getString(R.string.album_need_permission))
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
                    showPermissionFailure(fragment.context, StringUtils.getString(R.string.album_need_permission))
                }
            }, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    }


    fun showPermissionFailure(context: Context?, message: String) {
        AlertDialog.Builder(context).setTitle(R.string.hint)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(R.string.yes, AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
                AppUtils.launchAppDetailsSettings()
            })
            .setNegativeButton(R.string.no, AlertDialog.STANDARD, DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
            })
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
    fun getRealOssUrl(context: Context?, url: String, ossCallBack: CloudAccountApplication.OssSignCallBack) {

        val activity = context as Activity
        val application = activity.application as CloudAccountApplication


        var newUrl = ""
        val isStart = url.startsWith("/", true)
        newUrl = if (isStart) {
            url.replaceFirst("/", "")
        } else {
            url
        }
        application.getOssSecurityTokenForSignUrl(getOssSignUrlObjectKey(newUrl), ossCallBack)

    }

    /**
     * 验证 银行卡号
     */
    fun isBankCardNumber(cardString: String, typeString: String? = ""): Boolean {


        // 16,17, 19 位数
        val match = AndroidUtil.checkBankCardNumber(cardString)


        if (!match) {
            ToastUtils.showShort(StringUtils.getString(R.string.bank_number_wrong_format, typeString))
        }

        return match
    }


    /**
     * 验证 手机号
     */
    fun isPhoneNumber(string: String, typeString: String? = ""): Boolean {

        val match = AndroidUtil.isMobileNumber(string)
        if (!match) {
            ToastUtils.showShort(StringUtils.getString(R.string.phone_number_wrong_format, typeString))
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
            ToastUtils.showShort(StringUtils.getString(R.string.id_card_number_wrong_format, typeString))
        }
        return match
    }

    /**
     * 大于三个字的中文
     */
    fun isThreeChineseName(string: String, typeString: String? = ""): Boolean {

        if (string.isBlank()) {
            ToastUtils.showShort(StringUtils.getString(R.string.input_format, typeString))
            return false
        }

        if (RegexUtils.isMatch("^[a-zA-Z]+\$", string) || string.length < 2 || string.length > 30) {
            ToastUtils.showShort(typeString + "请输入3~30个中文")
            return false
        }
        return true
    }


    /**
     * 大于三个字的中文
     */
    fun is18TaxNumber(string: String): Boolean {

        if (string.length != 18) {
            ToastUtils.showShort("请输入18位纳税人识别号")
            return false
        }
        return true
    }


    /**
     * 正确的从业人数
     */
    fun isRightEmployeesNumber(string: String): Boolean {

        if (string.isBlank()) {
            ToastUtils.showShort(StringUtils.getString(R.string.employees_number))
            return false
        }

        if (string.length < 0) {

            return false
        }
        return true
    }


    /**
     * 校验身份证 url是否为空
     */
    fun hasIdCardUrl(idCardUrl: String, isFaceUp: Boolean, typeString: String? = ""): Boolean {
        val nullOrEmpty = idCardUrl.isNotEmpty()
        if (!nullOrEmpty) {

            if (isFaceUp) {
                ToastUtils.showShort(StringUtils.getString(R.string.id_card_front_empty_format, typeString))
            } else {
                ToastUtils.showShort(StringUtils.getString(R.string.id_card_back_empty_format, typeString))
            }


        }
        return nullOrEmpty

    }

    /**
     * @param idCardSide 身份证正反面
     * @param filePath 存储路径
     *  根据 imageStatus 判断识别结果合法性
     * @see <a href="https://cloud.baidu.com/doc/OCR/s/rk3h7xzck">OCR 身份证识别</a>
     */
    fun recIDCard(context: Context, idCardSide: String, filePath: String, callBack: RecognizeIDCardResultCallBack) {

        val param = IDCardParams().apply {

            imageFile = File(filePath)
            setIdCardSide(idCardSide)    // 设置身份证正反面
            isDetectDirection = true // 设置方向检测
            imageQuality = 9   // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        }

        OCR.getInstance(context).recognizeIDCard(param, object : OnResultListener<IDCardResult?> {
            override fun onResult(result: IDCardResult?) {

                if (result == null) {
                    return
                }
                if (result.imageStatus == "normal") {
                    callBack.onResult(result)
                } else {
                    ToastUtils.showShort("扫描错误，请重新扫描！")
                }

//                result.imageStatus
//                normal-识别正常
//                reversed_side-身份证正反面颠倒
//                non_idcard-上传的图片中不包含身份证
//                blurred-身份证模糊
//                other_type_card-其他类型证照
//                over_exposure-身份证关键字段反光或过曝
//                over_dark-身份证欠曝（亮度过低）
//                unknown-未知状态
            }

            override fun onError(error: OCRError) {
                ToastUtils.showShort("扫描错误，请重新扫描！")
            }
        })


    }


    fun handleDoubleClick(view: View) {
        view.isClickable = false
        view.postDelayed({
            view.isClickable = true
        }, 300)
    }


    fun lookGallery(context: Context, url: String) {
        if (url.isBlank()) {
            return
        }
        val imageList = ArrayList<String>()
        imageList.add(url)
        val bundle = Bundle().apply {
            putStringArrayList(Constants.PARAM_LIST, imageList)
            putInt(Constants.PARAM_INDEX, 0)
        }
        context.startActivity(Intent(context, GalleryActivity::class.java).putExtras(bundle))
    }

    /**
     * edit text  仅支持中文输入
     */
    fun onlySupportChineseInput(vararg editTextList: EditText) {
        editTextList.forEach {
            it.addTextChangedListener(LimitInputTextWatcher(it))
        }
    }


    fun hasDuplicateName(nameArrays: List<String>? = ArrayList(), vararg names: String): Boolean {


        val stringList = ArrayList<String>(names.asList())
        nameArrays?.let {
            stringList.addAll(it)
        }
        val stringSet: Set<String> = HashSet(stringList)
        return if (stringList.size == stringSet.size) {
            false
        } else {
            ToastUtils.showShort("姓名不能重复")
            true
        }

    }


    /**
     * 检查重复姓名
     */
    fun hasDuplicateName(vararg names: String): Boolean {

        val stringList = ArrayList<String>(names.asList())
        val stringSet: Set<String> = HashSet(stringList)

        return if (stringList.size == stringSet.size) {
            false
        } else {
            ToastUtils.showShort("姓名不能重复")
            true
        }
    }


    /**
     * 删除草稿
     */
    fun deleteDraft(productMold: String) {

        when (productMold) {
            Constants.P1 -> {
                PersonalLicenseDraft.clearAll()
            }
            Constants.P2 -> {
                EnterprisePackageDraft.clearAll()
            }
            Constants.P3 -> {
                PersonalBookkeepingDraft.clearAll()
            }
            Constants.P4 -> {
                PersonalTaxDraft.clearAll()
            }
            Constants.P5 -> {
                // 不需要保存功能
            }
            Constants.P6 -> {
                // 不需要保存功能
            }
            Constants.P7 -> {
                // 不需要保存功能
            }
            Constants.P8 -> {
                BankPersonalDraft.clearAll()
            }
            Constants.P9, Constants.P10 -> {
                NativeFormPersonalPackageP9P10Draft.clearAll()
            }
            Constants.P11 -> {
                // 无表单
            }
            else -> {
            }
        }
    }


    /**
     * 订单支付状态
     */
    fun setPaymentStatus(payState: String?, payStateText: String?,statusImage: ImageView, statusTextView: TextView) {
        statusTextView.text = payStateText

        when (payState) {
            Constants.OS1 -> {
                statusImage.setImageResource(R.drawable.ic_status_daizhifu)
            }
            Constants.OS2 -> {
                statusImage.setImageResource(R.drawable.ic_status_dingdanshixiao)
            }
            Constants.OS3 -> {
                statusImage.setImageResource(R.drawable.ic_status_dingdanshixiao)
            }
            Constants.OS4 -> {
                statusImage.setImageResource(R.drawable.ic_status_zhifuzhong)
            }
            Constants.OS5 -> {
                statusImage.setImageResource(R.drawable.ic_status_yibanjie)
            }

            Constants.OS6 -> {
                statusImage.setImageResource(R.drawable.ic_status_banlizhong)
            }
            Constants.OS7 -> {
                statusImage.setImageResource(R.drawable.ic_status_banlizhong)
            }
            Constants.OS8 -> {
                statusImage.setImageResource(R.drawable.ic_status_yibanjie)
            }
            Constants.OW1 -> {
                statusImage.setImageResource(R.drawable.ic_status_banlizhong)
            }
            Constants.OW2 -> {
                statusImage.setImageResource(R.drawable.ic_status_banlizhong)
            }
            Constants.OW3 -> {
                statusImage.setImageResource(R.drawable.ic_status_yibanjie)
            }
            Constants.OW4 -> {
                statusImage.setImageResource(R.drawable.ic_status_weijihuo)
            }
            else -> {
                statusImage.visibility = View.INVISIBLE
            }
        }
    }


}