package com.fatcloud.account.app

import android.app.Activity
import com.fatcloud.account.base.common.BaseView
import com.fatcloud.account.entity.commons.Commons

interface CloudAccountView : BaseView {

    fun getLastActivity(): Activity?

    fun hideLoading()
    fun showLoading()
    fun receiveCommonData(data: Commons)


}
