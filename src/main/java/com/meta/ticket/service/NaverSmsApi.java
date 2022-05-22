package com.meta.ticket.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

//기본 프로퍼티 파일이 아닌 다른 프로퍼티 파일에서 데이터를 가져오는 경우 property파일 경로를 지정해줘야 함
@PropertySource("classpath:application-sms.properties")
@Service
public class NaverSmsApi {

    @Value("${serviceId}")
    public String serviceId;

    @Value("${accessKey}")
    public String accessKey;

    @Value("${secretKey}")
    public String secretKey;

    @Value("${from}")
    public String from;

    public String getSignature(String time) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String serviceId = this.serviceId;
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/" + serviceId + "/messages";

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(time)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);

        return encodeBase64String;
    }

    public int sendSms(String userNumber){
        int result = 0;
        String time = String.valueOf(System.currentTimeMillis());
        String accessKey = this.accessKey;

        int random = (int)(Math.random()*10000)+1;
        System.out.println("인증 번호 : "+random);
        String serviceId = this.serviceId;
        String from = this.from; //등록한 번호만 사용 가능
        String to = userNumber;
        String subject = "[meta_ticket 인증]"; //기본 메시지 제목
        String apiUrl = "https://sens.apigw.ntruss.com/sms/v2/services/"+serviceId+"/messages";

        JSONObject bodyJson = new JSONObject();
        JSONObject toJson = new JSONObject();
        JSONArray toArr = new JSONArray();

        toJson.put("to", to);
        toJson.put("content", "인증번호("+random+") 입력시 인증완료");
        toArr.add(toJson);

        bodyJson.put("type", "SMS");
        bodyJson.put("contentType", "COMM");
        bodyJson.put("countryCode", "82");
        bodyJson.put("from", from);
        bodyJson.put("subject", subject);
        bodyJson.put("content", "인증번호 전송"); //둘 중 하나 제거해도 될 듯 -> to가 우선순위가 높음
        bodyJson.put("messages", toArr);

        String body = bodyJson.toJSONString();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn =  (HttpURLConnection)url.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "application/json");
            conn.setRequestProperty("x-ncp-apigw-timestamp", time);
            conn.setRequestProperty("x-ncp-iam-access-key", accessKey);
            conn.setRequestProperty("x-ncp-apigw-signature-v2", getSignature(time));

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.write(body.getBytes());
            dos.flush();
            dos.close();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            BufferedReader br;
            if(responseCode==202){
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                result = 1; //성공
            } else{
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                result = 0; //실패
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = br.readLine()) != null){
                response.append(inputLine);
            }
            br.close();

            System.out.println(response.toString());

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException ie){

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
