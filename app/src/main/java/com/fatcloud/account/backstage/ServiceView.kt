package com.fatcloud.account.backstage

import com.fatcloud.account.base.common.BaseView
import com.fatcloud.account.entity.commons.Commons
import com.fatcloud.account.entity.upgrade.Upgrade


interface ServiceView : BaseView {

    /**
     * 需要处理app 版本升级
     */
    fun doAppUpgrade(data: Upgrade)

}