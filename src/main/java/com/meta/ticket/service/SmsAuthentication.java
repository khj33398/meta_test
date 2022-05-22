package com.meta.ticket.service;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class SmsAuthentication {
    //단문 문자 발송
    //사용할 거라면 메시지 재전송 시간 설정 필요 -> 유료이기 때문
    public void sendShortMessage() {
        //파일이나 properties 파일에 기입하도록 수정 필요
        String api_key = "";
        String api_secret = "";
        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", "01076303241");
        params.put("from", "01076303241");
        params.put("type", "SMS");
        params.put("text", "[meta_ticket] 인증번호("+1978+") 입력시 정상처리");
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }

    //발송한 메시지 내역 보기
    public void checkMessage() {
        String api_key = "NCSNBDM0ATHTLNIP";
        String api_secret = "BEBVSWH7DYU9UCB9CSVR8Z6EFYKYHPIH";
        Message coolsms = new Message(api_key, api_secret);

        // Optional parameters for your own needs
        HashMap<String, String> params = new HashMap<String, String>();
        // 4 params(to, from, type, text) are mandatory. must be filled
        // params.put("message_id", "M52CB443257C61"); // message id
        // params.put("group_id", "G52CB4432576C8"); // group id
        // params.put("offset", "0"); // default 0
        // params.put("limit", "20"); // default 20
        // params.put("rcpt", "01000000000"); // search sent result by recipient number
        // params.put("start", "201601070915"); // set search start date
        // params.put("end", "201601071230"); // set search end date

        try {
            JSONObject obj = (JSONObject) coolsms.sent(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }
}
