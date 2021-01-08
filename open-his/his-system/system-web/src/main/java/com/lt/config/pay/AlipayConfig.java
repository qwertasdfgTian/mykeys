package com.lt.config.pay;

/**
 * @Author: Mr.Tian
 */
public class AlipayConfig {

    //应用的ID，申请时的APPID
    public static String app_id = "2021000116687596";

    //SHA256withRsa对应支付宝公钥
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvMvyPfzuvWsBathtruFiEWBA22lQn8SHKXcHSICbtocv2fbwWwIyMvfaEKn0d6MF/0jVTpuQugdbfKdwm9r51wvqCFOdKaH2SYDShuvSjaS/SKvQtKWJG6T6Wb74p7fVJ2EmjxzkWLehVXCch2AuwAwmYS5aKUWS5VWuH8dKdHhwzVtjtNzA+IBhOgwBtnzfZHc/aWkJY6y36SyP075f2zEWJ1L/Vc1yhu/jxZNOt4CLjBsabPOD+eUd8M6+e6jA04coGyN5C3PR+VNRVDlOa18jFiizNzTkhyPZj92W8UKAhQxdFSj9zJtfzr90zgB+qr740O+tjw0NlGE+wYQhWQIDAQAB";
    //签名方式
    public static String sign_type = "RSA2";
    //字码编码格式
    public static String charset = "utf-8";
    //回调地址
    public static String notifyUrl="http://3323756pb1.zicp.vip/pay/callback/";
}
