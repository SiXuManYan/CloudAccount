package com.fatcloud.account.feature.order.details.enterprise.company

import com.fatcloud.account.base.common.BaseTaskView
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo
import com.fatcloud.account.entity.order.persional.PersonalInfo

/**
 * Created by Wangsw on 2020/6/4 0004 14:01.
 * </br>
 *  公司信息
 */
interface CompanyRegisterInfoView : BaseTaskView {
    fun bindDetailInfo(data: EnterpriseInfo)

    /**
     * 当为 产品流程类型为：PW3银行账户办理时，tOrderWork/detail 接口不返回相关法人信息
     * 需要使用订单id调用  tOrder/detail 接口请求法人股东信息
     * 绑定法人信息
     */
    fun bindShareholdersInfo(data: PersonalInfo)
}