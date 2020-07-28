package com.fatcloud.account.feature.product.detail.input

import com.fatcloud.account.base.common.BaseView
import com.fatcloud.account.entity.defray.prepare.PreparePay

/**
 * Created by Wangsw on 2020/7/28 0028 9:37.
 * </br>
 *
 */
interface ProductInputView : BaseView {
    fun commitSuccess(it: PreparePay)
}