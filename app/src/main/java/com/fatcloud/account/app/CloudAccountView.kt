package com.fatcloud.account.app

import android.app.Activity
import com.fatcloud.account.base.common.BaseView

interface CloudAccountView : BaseView {

    fun getLastActivity(): Activity?

    fun hideLoading()
    fun showLoading()




}
