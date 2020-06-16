package com.fatcloud.account.feature.defray

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.defray.WechatPayInfo

/**
 * Created by Wangsw on 2020/6/16 0016 16:02.
 * </br>
 *
 */
interface PayView : BaseTaskView {

    /**
     * 进行微信支付
     */
    fun doWechatPay(it: WechatPayInfo)


}