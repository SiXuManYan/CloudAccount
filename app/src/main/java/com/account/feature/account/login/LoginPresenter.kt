package com.account.feature.account.login

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.account.base.common.BasePresenter
import com.account.base.net.BaseHttpSubscriber
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/1 0001 17:10.
 * </br>
 *
 */
class LoginPresenter @Inject constructor(private var loginView: LoginView) : BasePresenter(loginView) {


    /**
     * 检查用户是否存在
     */
    fun checkAccountIsExisted(lifecycleOwner: LifecycleOwner, account: String) {

        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.checkAccountIsExisted(account), object : BaseHttpSubscriber<JsonObject>(loginView) {
                override fun onSuccess(data: JsonObject?) {


                    data?.let {
                        if (it.has("existed")) {
                            loginView.accountExistedTag(it.get("existed").asBoolean,account)
                        }
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    loginView.accountExistedTag(false, account)
                }

            }
        )


    }


}