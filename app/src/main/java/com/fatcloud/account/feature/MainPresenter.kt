package com.fatcloud.account.feature

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.BuildConfig
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.entity.upgrade.Upgrade
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/5/25 0025 16:22.
 * </br>
 *
 */
class MainPresenter @Inject constructor(private var mainView: MainView) : BasePresenter(mainView) {

    /**
     * 检查app版本号
     */
    fun checkAppVersion(lifecycleOwner: LifecycleOwner) {
        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.checkAppVersion(),
            object : BaseHttpSubscriber<Upgrade>(mainView) {
                override fun onSuccess(data: Upgrade?) {
                    data?.let {
                        val version = BigDecimal(it.appVersion)
                        val localVersionCode = BigDecimal(BuildConfig.VERSION_CODE)
                        if (version.compareTo(localVersionCode) == 1 && !it.appUrl.isNullOrBlank()) {
                            mainView.doAppUpgrade(it)
                        }
                    }


                }

            }
        )

    }
}