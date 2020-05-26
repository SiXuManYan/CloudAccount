package com.account.app

import android.app.Activity
import com.account.base.common.BaseView

interface CloudAccountView : BaseView {

    fun getLastActivity(): Activity?

    fun hideLoading()
    fun showLoading()




}
