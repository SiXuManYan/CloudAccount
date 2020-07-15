package com.fatcloud.account.feature.forms.personal.logout

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.fatcloud.account.BuildConfig
import com.fatcloud.account.R
import com.fatcloud.account.app.CloudAccountApplication
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.common.PermissionUtils
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.order.persional.PersonalLicenseLogout
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/7/13 0013 16:38.
 * </br>
 *
 */
class FormLicenseLogoutPresenter @Inject constructor(private var view: FormLicenseLogoutView) : BasePresenter(view) {
    private val gson = Gson()

    /**
     * 添加个体营业执照注销
     */
    fun addLicenseChangePersonal(lifecycle: LifecycleOwner, enterpriseInfo: PersonalLicenseLogout) {

        val bodyJsonStr = gson.toJson(enterpriseInfo)

        val jsonObject: JsonObject = gson.fromJson(bodyJsonStr, JsonObject::class.java)

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.addLicensePersonalLogout(jsonObject),
            object : BaseHttpSubscriber<PreparePay>(view) {
                override fun onSuccess(data: PreparePay?) {

                    data?.let {
                        view.commitSuccess(it)
                    }

                }

            }
        )
    }

    fun downLoadImage(activity: Activity) {
        // 下载授权书
        val application = activity.application as CloudAccountApplication
        val commitmentUrl = application.commonData?.commitmentUrl
        if (commitmentUrl.isNullOrBlank()) {
            return
        }

        PermissionUtils.permissionAny(activity, PermissionUtils.OnPermissionCallBack { granted ->

                if (!granted) {
                    ProductUtils.showPermissionFailure(activity, "下载需要授权存储权限，是否打开应用设置授权？")
                    return@OnPermissionCallBack
                }

                // http://192.168.1.191:8881/product/p6/commitment.png
                val savePath = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath, "Cloud")
                val destFile = File(savePath, "commitment.jpg")
                FileUtils.createOrExistsFile(destFile)
                Observable.just(destFile)
                    .map { dest ->
                        val future =
                            Glide.with(activity).asFile().load(BuildConfig.SERVER_HOST + commitmentUrl).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        future.get().copyTo(dest, true)
                        return@map dest
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ dest ->
                        activity.sendBroadcast(
                            Intent(
                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.fromFile(dest)
                            )
                        )
                        ToastUtils.showShort(R.string.image_save_success)
                    }, {
                        LogUtils.e(it.message)
                    })


            }, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }


}