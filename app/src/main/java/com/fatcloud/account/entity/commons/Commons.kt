package com.fatcloud.account.entity.commons

data class Commons (

    /**
     * 银行账户性质类型
     */
    val accountNatues: List<AccountNatue>,

    /**
     * 经营范围
     */
    val businessScope: List<BusinessScope>,


    val businessScopeInds: List<BusinessScopeInd>,

    /**
     * 组成形式
     */
    val forms: List<Form>
)