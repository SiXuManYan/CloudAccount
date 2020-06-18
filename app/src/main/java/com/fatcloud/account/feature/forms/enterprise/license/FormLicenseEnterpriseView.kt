package com.fatcloud.account.feature.forms.enterprise.license

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.defray.prepare.PreparePay

/**
 * Created by Wangsw on 2020/6/10 0010 18:30.
 * </br>
 *
 */
interface FormLicenseEnterpriseView : BaseTaskView {

    /**
     * 企业套餐添加成功
     */
    fun addEnterpriseSuccess(preparePay: PreparePay)
}