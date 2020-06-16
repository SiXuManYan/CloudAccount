package com.fatcloud.account.entity.defray;

/**
 * Created by Admin on 2018/1/8.
 * <p>
 * https://docs.open.alipay.com/204/105301
 */
public class AlipayResultStatus {

    /**
     * 订单支付成功
     */
    public static final String Status_9000 = "9000";


    /**
     * 订单支付失败
     */
    public static final String Status_4000 = "4000";

    /**
     * 正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     */
    public static final String Status_8000 = "8000";


    /**
     * 重复请求
     */
    public static final String Status_5000 = "5000";

    /**
     * 用户中途取消
     */
    public static final String Status_6001 = "6001";

    /**
     * 网络连接出错
     */
    public static final String Status_6002 = "6002";

    /**
     * 支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     */
    public static final String Status_6004 = "6004";

}
