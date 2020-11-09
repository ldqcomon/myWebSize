package com.it.sf.common;

import com.it.sf.common.HttpClientUtils;

/**
 * @Auther: ldq
 * @Date: 2020/8/26
 * @Description:
 * @Version: 1.0
 */
public class SmsTest {
    //用户名
    private static String Uid = "hear";

    //接口安全秘钥
    private static String Key = "d41d8cd98f00b204e980"; //me

    //手机号码，多个号码如13800000000,13800000001,13800000002
    private static String smsMob = "15220062636";

    //短信内容
    private static String smsText = "验证码：88";

    public static void main(String[] args) {

        HttpClientUtils client = HttpClientUtils.getInstance();

        //UTF发送
        int result = client.sendMsgUtf8(Uid, Key, smsText, smsMob);
        if(result>0){
            System.out.println("UTF8成功发送条数=="+result);
        }else{
            System.out.println(client.getErrorMsg(result));
        }
    }
}
