package com.fatcloud.account.entity.order.enterprise

import java.math.BigDecimal

/**
 * 企业信息
 */
data class EnterpriseInfo(
    val accountId: String,
    val addr: String,
    val area: String,
    val bankNo: String,
    val bankPhone: String,
    val businessScope: List<Int>,
    val businessScopeNames: String,
    val createDt: String,
    val delFlag: Int,
    val enterpriseName0: String,
    val enterpriseName1: String,
    val enterpriseName2: String,
    val id: String,
    val income: Int,

    /**
     * 出资数额
     */
    val investMoney:BigDecimal = BigDecimal.ZERO,
    val investYearNum: Int = 0,
    val mold: String,
    val moldText: String,
    val money: BigDecimal = BigDecimal.ZERO,
    val nickName: String,
    val no: String,
    val payState: String,
    val payStateText: String,
    val productId: String,
    val productName: String,
    val productPriceId: Int,
    val productPriceName: String,
    val shareholders: List<Shareholder>,
    val state: String,
    val stateText: String,
    val updateDt: String,
    val username: String,
    val version: Int,
    val weixinPayMap: String,


    // 银行相关信息
    /** 营业执照图网址 */
    val businessLicenseImgUrl: String,

    /** 首都 */
    val capital: String,

    /** 电子图章网址 */
    val electronicSealImgUrl: String,

    /** 企业地址 */
    val enterpriseAddr: String,

    /** 企业性质 */
    val enterpriseMold: String,

    /** 企业名称 */
    val enterpriseName: String,

    /** 金融Idno */
    val financeIdno: String,

    /** 财务ID编号网址A */
    val financeIdnoImgUrlA: String,

    /** 金融Idno Img Url B */
    val financeIdnoImgUrlB: String,

    /** 财务名称 */
    val financeName: String,

    /** 金融电话 */
    val financePhone: String,

    /** 法人认股权证网址 */
    val legalPersonWarrantImgUrl: String,

    /** 订单工作编号 */
    val orderWorkId: String,

    /** 调和地址 */
    val reconciliatAddr: String,

    /** 调解区 */
    val reconciliatArea: String,

    /** 协调联系 */
    val reconciliatContact: String,

    /** reconciliatPhone */
    val reconciliatPhone: String,

    /** 设置状态文本 */
    val settingStateText: String

)

/*


{
    "code": "200",
    "msg": "成功",
    "data":
    {
        "id": "1268422429030481920",
        "delFlag": 0,
        "updateDt": "2020-06-08 10:15:25",
        "createDt": "2020-06-04 14:02:31",
        "version": 2,
        "no": "2020060400002062",
        "accountId": "1264788939516936192",
        "productId": "1262568059554496512",
        "productPriceId": "3",
        "money": 0.01,
        "mold": "P2",
        "state": "OW3",
        "payState": "PS4",
        "weixinPayMap": "{\"sign\": \"9A6BC3770FC9E80E2CECB9D9D284B82B\", \"appid\": \"wxd47f6921c3df9750\", \"package\": \"Sign=WXPay\", \"noncestr\": \"HBfVKJBbrgLA2f8c\", \"prepayid\": \"wx04140246612264f55c994ce51792146000\", \"partnerid\": \"1528759041\", \"timestamp\": \"1591250462\"}",
        "area": "内蒙古自治区赤峰市阿鲁科尔沁旗坤都镇",
        "income": 2000,
        "payStateText": "微信已支付",
        "productId": "1262568059554496512",
        "shareholders": [
        {
            "idno": "210102191203072090",
            "imgs": [
            {
                "mold": "I1",
                "imgUrl": "ios_20200604140230577_202003272.png"
            },
            {
                "mold": "I2",
                "imgUrl": "ios_20200604140231136_202008896.png"
            }
            ],
            "mold": "SH1",
            "name": "佛举行",
            "phone": "13200010001",
            "idnoAddr": "佛裤子",
            "shareProportion": 8
        },
        {
            "idno": "210102191203072090",
            "imgs": [
            {
                "mold": "I1",
                "imgUrl": "ios_20200604140231237_202002218.png"
            },
            {
                "mold": "I2",
                "imgUrl": "ios_20200604140231327_202004707.png"
            }
            ],
            "mold": "SH2",
            "name": "逗我",
            "phone": "13500010001",
            "idnoAddr": "阿惊喜",
            "shareProportion": 8
        },
        {
            "idno": "210102191203072090",
            "imgs": [
            {
                "mold": "I1",
                "imgUrl": "ios_20200604140231418_202002082.png"
            },
            {
                "mold": "I2",
                "imgUrl": "ios_20200604140231515_202003644.png"
            }
            ],
            "mold": "SH3",
            "name": "明我哦",
            "phone": "13500010001",
            "idnoAddr": "俄就可以咯",
            "shareProportion": 5
        }
        ],
        "productPriceName": "无业务,零申报",
        "nickName": "你好123",
        "investMoney": 2000,
        "businessScope": [
        392
        ],
        "businessScopeNames": "通讯科技",
        "productPriceId": 3,
        "productName": "企业套餐办理",
        "moldText": "企业套餐",
        "bankPhone": "13200010001",
        "money": 3176,
        "stateText": "已办结",
        "bankNo": "6222222222222222222",
        "investYearNum": 20,
        "enterpriseName2": "佛子岭",
        "enterpriseName1": "Doyon",
        "enterpriseName0": "俄墨鱼",
        "addr": "哦图咯行",
        "username": "13200010001"
    }
}








  "data": {
    "capital": "2000",
    "financeIdno": "210102191203072090",
    "financeName": "佛学院",
    "orderWorkId": "1268422429160505344",
    "financePhone": "13200010001",
    "enterpriseAddr": "公司公司",
    "enterpriseMold": "专用户",
    "enterpriseName": "俄墨鱼",
    "reconciliatAddr": "佛学院我",
    "reconciliatArea": "河北省邯郸市复兴区化林路街道",
    "reconciliatPhone": "13200010001",
    "financeIdnoImgUrlA": "ios_20200604144001110_202005954.png",
    "financeIdnoImgUrlB": "ios_20200604144001602_202006111.png",
    "reconciliatContact": "恩泽我",
    "electronicSealImgUrl": "ios_20200604144001885_202001740.png",
    "businessLicenseImgUrl": "ios_20200604144001767_202008348.png",
    "legalPersonWarrantImgUrl": "ios_20200604144002362_202002618.png",
    "stateText": "已办结",
    "state": "OW3",
    "settingStateText": "已激活"
  }




*/
