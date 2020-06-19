package com.fatcloud.account.feature.account.login.wechat

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/18 0018 20:43.
 * </br>
 *
 */
class WechatLoginRegisterPresenter @Inject constructor(private var wechatLoginRegisterView: WechatLoginRegisterView) : BasePresenter(wechatLoginRegisterView) {


    /**
     * 检查用户是否存在
     */
    fun checkAccountIsExisted(lifecycleOwner: LifecycleOwner, account: String) {

        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.checkAccountIsExisted(account), object : BaseHttpSubscriber<JsonObject>(wechatLoginRegisterView) {
                override fun onSuccess(data: JsonObject?) {

                    data?.let {
                        if (it.has("existed")) {
                            wechatLoginRegisterView.accountExistedTag(it.get("existed").asBoolean, account)
                        }
                    }
                }
            }
        )
    }



}