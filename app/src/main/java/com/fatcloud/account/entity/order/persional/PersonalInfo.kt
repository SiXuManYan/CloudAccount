package com.fatcloud.account.entity.order.persional

import com.fatcloud.account.entity.commons.BusinessScope
import com.fatcloud.account.entity.commons.Form
import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.enterprise.Shareholder
import java.math.BigDecimal

/**
 * 个人业务订单详情
 *
 */
class PersonalInfo {

    var accountId: String = ""
    var addr: String= ""
    var area: String= ""
    var bankNo: String= ""

    /**
     * 营业执照地址
     */
    var businessLicenseImgUrl: String= ""
    var createDt: String= ""
    var delFlag: Int = 0

    /**
     * 个人业务id
     */
    var id: String = ""

    /**
     * 身份证号
     */
    var idno: String= ""
    var legalPersonName: String= ""

    /**
     * @see  Order.mold
     */
    var mold: String= ""
    var moldText: String= ""
    var money: BigDecimal = BigDecimal.ZERO
    var nickName: String= ""
    var no: String= ""

    /**
     * PS1 未支付
     * PS2 微信支付中
     * PS3 支付宝支付中
     * PS4 微信已支付
     * PS5 支付宝已支付
     */
    var payState: String= ""
    var payStateText: String= ""
    var phoneOfBank: String= ""
    var productId: String= ""
    var productName: String= ""
    var productPriceId: String= ""
    var productPriceName: String= ""


    /**
     * OS1 待支付
     * OS2 取消订单
     * OS3 订单失效
     * OS4 支付中
     * OS5 已支付
     * OS6 已受理
     * OS7 办理中
     * OS8 已办结
     */
    var state: String= ""
    var stateText: String= ""
    var taxpayerNo: String= ""
    var updateDt: String= ""
    var username: String= ""
    var version: Int = 0
    var weixinPayMap: String= ""


    /**
     * 经营范围 id
     * @see BusinessScope.id
     */
    var businessScope: List<Int> = ArrayList()



    var businessScopeNames: String= ""


    /**
     * 资金数额，注册资本
     */
    var capital: BigDecimal = BigDecimal.ZERO

    /**
     * 从业人数
     */
    var employedNum: String = "0"

    /**
     * 组成形式的 Id
     * @see Form.id
     */
    var form: Int = 0
    var formName: String= ""

    /**
     * 1 男
     * 2 女
     */
    var gender: String= ""
    var imgs: List<IdentityImg> = ArrayList()


    /**
     * 收入
     */
    var income: BigDecimal = BigDecimal.ZERO
    var name0: String= ""
    var name1: String= ""
    var name2: String= ""
    var nation: String= ""

    var realName: String= ""
    var tel: String= ""


    // 公司信息


    var enterpriseName0: String= ""
    var enterpriseName1: String= ""
    var enterpriseName2: String= ""

    /**
     * 注册资本
     */
    var investMoney: BigDecimal = BigDecimal.ZERO

    var investYearNum: Int = 0

    var shareholders: List<Shareholder> = ArrayList()


}

/*

{
    "id": "1268456920973312000",
    "delFlag": 0,
    "updateDt": "2020-06-04 16:19:57",
    "createDt": "2020-06-04 16:19:35",
    "version": 2,
    "no": "2020060400003117",
    "accountId": "1264788939516936192",
    "productId": "1262573783173038080",
    "productPriceId": "1263057059755065344",
    "money": 0.01,
    "mold": "P4",
    "state": "OS7",
    "payState": "PS4",
    "weixinPayMap": "{\"sign\": \"8F530264FB7D1CA2AEB920FE1EB9C24B\", \"appid\": \"wxd47f6921c3df9750\", \"package\": \"Sign=WXPay\", \"noncestr\": \"ia0HbHqoBEAYR1U0\", \"prepayid\": \"wx041619389915239288e1960b1703460000\", \"partnerid\": \"1528759041\", \"timestamp\": \"1591258675\"}",
    "area": "内蒙古自治区通辽市科左后旗常胜镇",
    "payStateText": "微信已支付",
    "productId": "1262573783173038080",
    "productPriceName": "个体户税务登记",
    "nickName": "你好123",
    "productPriceId": "1263057059755065344",
    "idno": "210102191203072090",
    "productName": "个体户税务登记",
    "moldText": "个体户税务登记",
    "legalPersonName": "呵呵哒nd",
    "taxpayerNo": "123456789123456789",
    "phoneOfBank": "13200010001",
    "money": 298,
    "stateText": "办理中",
    "bankNo": "6227003028940029516",
    "businessLicenseImgUrl": "ios_20200604161935153_202009906.png",
    "addr": "营业执照我",
    "username": "13200010001"
}

// 个体户税务登记


// 添加个人营业执照
{
  "addr": "string",
  "area": "string",
  "businessScope": [
    0
  ],
  "capital": 0,
  "employedNum": 0,
  "form": 0,
  "gender": "string",
  "idno": "string",
  "imgs": [
    {
      "imgUrl": "string",
      "mold": "string"
    }
  ],
  "income": 0,
  "money": "string",
  "name0": "string",
  "name1": "string",
  "name2": "string",
  "nation": "string",
  "productId": 0,
  "productPriceId": 0,
  "realName": "string",
  "tel": "string"
}




*/
