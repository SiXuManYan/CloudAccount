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
     * 重设密码
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
