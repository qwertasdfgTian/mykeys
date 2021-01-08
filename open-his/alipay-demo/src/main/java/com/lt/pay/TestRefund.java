package com.lt.pay;

import java.util.Map;

/**
 * @Author: Mr.Tian
 * 测试退费
 */
public class TestRefund {
    public static void main(String[] args) {
        String outTradeNo="OD123124124ABCDEFG";
        String tradeNo="2020081522001496340500798263";
        String subject="LT-医疗管理系统支付平台";
        String refudAmount="60";
        String refudReason="不想要了";
        Map<String, Object> map = PayService.payBack(outTradeNo, tradeNo, refudAmount, refudReason, "BK12345681");
        System.out.println(map);
    }
}
