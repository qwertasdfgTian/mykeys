package com.lt.pay;

import java.util.Map;

/**
 * @Author: Mr.Tian
 */
public class TestPay {
    public static void main(String[] args) {
        String outTradeNo="OD123124124ABCDEFG";
        String subject="LT-医疗管理系统支付平台";
        String totalAmount="100";
        String undiscountableAmount=null;
        String body="买药用的";
        //回调的接口
        String notifyUrl="http://3323756pb1.zicp.vip/pay/callback"+outTradeNo;
        //String notifyUrl="http://127.0.0.1";
        Map<String, Object> pay = PayService.pay(outTradeNo, subject, totalAmount, undiscountableAmount, body, notifyUrl);
        System.out.println(pay);
    }
}
