package com.fatcloud.account.feature.forms.market

import com.fatcloud.account.base.common.BaseView
import com.fatcloud.account.entity.form.MarketData

/**
 * Created by Wangsw on 2020/8/5 0005 11:34.
 * </br>
 * 电子化平台
 */
interface FormMarketView : BaseView {
    fun bindPageInfo(it: MarketData)

}