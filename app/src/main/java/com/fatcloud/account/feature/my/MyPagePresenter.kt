package com.fatcloud.account.feature.my

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.ToastUtils
import com.fatcloud.account.BuildConfig
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.common.*
import com.fatcloud.account.data.CloudDataBase
import com.fatcloud.account.entity.users.User
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.RxBus
import com.google.gson.JsonElement
import java.io.File
import javax.inject.Inject

class MyPagePresenter @Inject constructor(private val view: MyPageView) : BasePresenter(view) {


    lateinit var database: CloudDataBase @Inject set


    /**
     * 退出登录
     */
    fun loginOutRequest(lifecycleOwner: LifecycleOwner) {

        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.logout(),
            object : BaseHttpSubscriber<JsonElement>(view) {
                override fun onComplete() {
                    super.onComplete()
                    removeUserInfo()
                }

                override fun onSuccess(data: JsonElement?) = Unit
            })
    }

    /**
     * 更新头像和昵称
     */
    fun updateAvatarAndNickname(lifecycleOwner: LifecycleOwner, avatarUrl: String? = null, nickName: String? = null) {

        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY, apiService.updateAvatarAndNickname(avatarUrl, nickName),
            object : BaseHttpSubscriber<JsonElement>(view) {

                override fun onSuccess(data: JsonElement?) {
                    User.get().apply {
                        avatarUrl?.let {
                            this.headUrl = it
                        }
                        nickName?.let {
                            this.nickName = it
                        }
                        database.userDao().updateUser(this)
                        User.update()
                    }
                    view.updateAvatarAndNicknameSuccess()

                }
            })
    }


    /**
     * 消息未读数量
     */
    fun getNewsUnreadCount(lifecycleOwner: LifecycleOwner) {
        if (!User.isLogon()) {
            return
        }
        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY, apiService.getNewsUnreadCount(), object : BaseHttpSubscriber<JsonElement>(view, false) {
            override fun onSuccess(data: JsonElement?) {
                if (data == null) {
                    return
                }

                if (data.isJsonPrimitive) {
                    val messageUnReadNumber = data.asLong
                    view.updateMessageUnReadNumber(messageUnReadNumber)

                }
            }
        })
    }


    private fun removeUserInfo() {
        // 清空用户信息
        database.userDao().clear()
        User.clearAll()

        // 更新登录状态
        CommonUtils.getShareDefault().put(Constants.SP_LOGIN, false)
        CommonUtils.getShareDefault().put(Constants.SP_TOKEN, "")

        // 刷新页面登录状态
        RxBus.post(Event(Constants.EVENT_NEED_REFRESH))
        RxBus.post(Event(Constants.EVENT_LOGOUT))
    }


    /**
     * 拍摄和录制权限申请
     */
    fun requestShootingPermissions(context: Context,file_path:String) {



        PermissionUtils.permissionAny(
            context, PermissionUtils.OnPermissionCallBack { granted ->
                if (granted) {
                    val sdStatus = Environment.getExternalStorageState()
                    if (sdStatus != Environment.MEDIA_MOUNTED) { // 检测sd是否可用
                        ToastUtils.showShort("SD卡不存在")
                    } else {
                        Common.FILE_NAME = String.format(Common.COMMON_PHOTO_NAME, System.currentTimeMillis())
                        val fileParent = File(file_path)
                        if (!fileParent.exists()) {
                            fileParent.mkdirs()
                        }
                        val file = File(file_path + Common.FILE_NAME)
                        val uri: Uri
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file)// 7.0以后
                        } else {
                            uri = Uri.fromFile(file)
                        }
                        view.onShootingPermissionResult(uri)



                    }

                } else {
                    ProductUtils.showPermissionFailure(context!!, "为了正常使用拍摄功能，请允许存储空间权限和相机权限")
                }
            },
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    }

}
