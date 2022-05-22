package com.meta.ticket;

import com.meta.ticket.service.NaverSmsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller는 기본적으로 .jsp이나 .html파일로 연결됨
@RestController
@SpringBootApplication
@PropertySource("classpath:application-sms.properties")
public class TicketApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketApplication.class, args);
    }
    @Autowired
    private NaverSmsApi naverSmsApi;

    @Value("${to}")
    public String to;

    @GetMapping(value="/")
    public String serviceIdTest(){
        int result = naverSmsApi.sendSms(this.to);
        if(result == 1){
            return "인증번호 전송 완료";
        }
        return "인증번호 전송 실패";
    }

}
