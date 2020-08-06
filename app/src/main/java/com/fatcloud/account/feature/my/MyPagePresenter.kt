package com.fatcloud.account.feature.my

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.data.CloudDataBase
import com.fatcloud.account.entity.users.User
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.RxBus
import com.google.gson.JsonElement
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


}
