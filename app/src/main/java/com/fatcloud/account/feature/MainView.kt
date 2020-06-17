package com.fatcloud.account.feature

import com.fatcloud.account.base.common.BaseView
import com.fatcloud.account.entity.upgrade.Upgrade

interface MainView :BaseView{
    abstract fun doAppUpgrade(data: Upgrade)

}
