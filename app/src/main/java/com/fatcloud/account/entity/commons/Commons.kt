package com.fatcloud.account.entity.commons

data class Commons(

    /**
     * 银行账户性质类型
     */
    val accountNatues: List<AccountNature> = ArrayList(),

    /**
     * 经营范围
     */
    val businessScope: ArrayList<BusinessScope> = ArrayList(),


    val businessScopeInds: List<BusinessScopeInd> = ArrayList(),


    /**
     * 组成形式
     */
    val forms: List<Form> = ArrayList(),

    val ocrCount: Int = 0,

    /**
     * 营业执照注销承诺书
     */
    var commitmentUrl: String = "",

    val accountNatuesPerson: List<BusinessScopeInd> = ArrayList()



)