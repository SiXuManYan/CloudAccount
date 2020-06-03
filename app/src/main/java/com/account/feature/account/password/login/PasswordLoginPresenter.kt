package com.account.feature.account.password.login

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.account.backstage.DataServiceFaker
import com.account.base.common.BasePresenter
import com.account.base.net.BaseHttpSubscriber
import com.account.common.CommonUtils
import com.account.common.Constants
import com.account.data.CloudDataBase
import com.account.entity.users.User
import com.account.event.Event
import com.account.event.RxBus
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/2 0002 18:09.
 * </br>
 *
 */
class PasswordLoginPresenter @Inject constructor(private var view: PasswordLoginView) : BasePresenter(view) {


    lateinit var database: CloudDataBase @Inject set

    fun passwordLogin(lifecycleOwner: LifecycleOwner, currentAccount: String, password: String) {
        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.passwordLogin(currentAccount, password), object : BaseHttpSubscriber<User>(view) {
                override fun onSuccess(data: User?) {


                    data?.let {
                        loginSuccess(it,currentAccount)
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