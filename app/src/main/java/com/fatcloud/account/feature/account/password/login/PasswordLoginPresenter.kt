package com.fatcloud.account.feature.account.password.login

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.backstage.DataServiceFaker
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.common.AndroidUtil
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.data.CloudDataBase
import com.fatcloud.account.entity.users.User
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.RxBus
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/2 0002 18:09.
 * </br>
 *
 */
class PasswordLoginPresenter @Inject constructor(private var view: PasswordLoginView) : BasePresenter(view) {


    lateinit var database: CloudDataBase @Inject set

    fun passwordLogin(lifecycleOwner: LifecycleOwner, currentAccount: String, password: String) {
        val deviceId = CommonUtils.getShareDefault().getString(Constants.SP_PUSH_DEVICE_ID)

        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.passwordLogin(currentAccount, AndroidUtil.md5(password), deviceId), object : BaseHttpSubscriber<User>(view) {
                override fun onSuccess(data: User?) {

                    data?.let {
                        loginSuccess(it, currentAccount)
                        view.loginSuccess()
                    }
                }

            }
        )
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




}