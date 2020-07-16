package com.fatcloud.account.entity.order.persional

import com.fatcloud.account.entity.commons.BusinessScope
import com.fatcloud.account.entity.order.IdentityImg

/**
 * Created by Wangsw on 2020/7/13 0013 8:53.
 * </br>
 * 个体户营业执照变更
 */
class PersonalBank {

    /**
     * 产品id
     */
    var productId: String? = null

    /**
     * 选中的产品价格id
     */
    var productPriceId: String? = null


    /**
     * 经营范围 id
     * @see BusinessScope.id
     */
    var businessScope: ArrayList<String>? = null


    /**
     * 身份证号
     */
    var idno: String? = null


    /**
     * 身份证正反面
     */
    var imgsIdno: List<IdentityImg>? = null

    /**
     * 营业执照正反面
     */
    var imgsLicense: List<IdentityImg>? = null


    /**
     * 法人姓名
     */
    var legalPersonName: String? = null


    /**
     * 客户端计算出的最终金额
     */
    var money: String? = null

    /**
     * 联系电话
     */
    var phone: String? = null


/*
    {
        "accountType": "string",
        "addressDetailed": "string",
        "addressPost": "string",
        "addressRegistered": "string",
        "bank": "string",
        "currency": "string",
        "depositorName": "string",
        "enterpriseCode": "string",
        "imgsDepositAccount": [
        {
            "imgUrl": "string",
            "mold": "string"
        }
        ],
        "imgsIdno": [
        {
            "imgUrl": "string",
            "mold": "string"
        }
        ],
        "imgsLicense": [
        {
            "imgUrl": "string",
            "mold": "string"
        }
        ],
        "money": 0,
        "personFinance": {
        "name": "string",
        "phone": "string"
    },
        "personLegal": {
        "name": "string",
        "phone": "string"
    },
        "personReconciliation": {
        "name": "string",
        "phone": "string"
    },
        "personVerification1": {
        "name": "string",
        "phone": "string"
    },
        "personVerification2": {
        "name": "string",
        "phone": "string"
    },
        "productId": 0,
        "productPriceId": 0
    }
    */
}