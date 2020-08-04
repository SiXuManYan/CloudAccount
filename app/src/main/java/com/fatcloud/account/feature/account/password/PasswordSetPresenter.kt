package com.fatcloud.account.feature.account.password

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.backstage.DataServiceFaker
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

/**
 * Created by Wangsw on 2020/6/2 0002 15:55.
 * </br>
 *
 */
class PasswordSetPresenter @Inject constructor(private var passwordSetView: PasswordSetView) : BasePresenter(passwordSetView) {


    lateinit var database: CloudDataBase @Inject set

/*
    {
        "code": "200",
        "msg": "成功",
        "data": {
        "token": "2ecd511fb1f143ad922679e73e08fc18",
        "username": "17640339671",
        "nickName": "Fta:笔张"
            }
    }
*/

    /**
     * 注册
     */
    fun register(
        lifecycleOwner: LifecycleOwner,
        passWord: String,
        account: String,
        captcha: String
    ) {

        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.register(
                account,
                passWord,
                captcha,
                "Android",
                CommonUtils.getLocationInfo()[2],
                CommonUtils.getLocationInfo()[4],
                CommonUtils.getLocationInfo()[5],
                CommonUtils.getShareDefault().getString(Constants.SP_PUSH_DEVICE_ID)
            ),
            object : BaseHttpSubscriber<User>(passwordSetView) {
                override fun onSuccess(data: User?) {
                    data?.let {

                        loginSuccess(it, account)

                        passwordSetView.registerSuccess()
                    }

                }
            })
    }


    private fun loginSuccess(it: User, account: String) {
        // 更新用户
        database.userDao().addUser(it)
        User.update()

        // 更新登录状态
        CommonUtils.getShareDefault().put(Constants.SP_LOGIN, true)
        CommonUtils.getShareDefault().put(Constants.SP_TOKEN, it.token)
        CommonUtils.getShareDefault().put(Constants.SP_LAST_LOGIN_USER, account)

        // 刷新页面登录状态
        RxBus.post(Event(Constants.EVENT_LOGIN))
        RxBus.post(Event(Constants.EVENT_NEED_REFRESH))

        // 更新应用数据
        DataServiceFaker.startService(appContext, Constants.ACTION_SYNC_OTHER)
    }

    /**
     * 重设密码
     */
    fun resetPassword(lifecycleOwner: LifecycleOwner, passWord: String, account: String,    captcha: String) {
        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.resetPassword(
                account,
                passWord,
                captcha
            ),
            object : BaseHttpSubscriber<JsonElement>(passwordSetView) {
                override fun onSuccess(data: JsonElement?) {
                    RxBus.post(Event(Constants.EVENT_PASSWORD_RESET_SUCCESS))
                    passwordSetView.passwordResetSuccess()

                }
            })
    }


}