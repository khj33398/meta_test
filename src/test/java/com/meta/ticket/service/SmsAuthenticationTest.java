package com.meta.ticket.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-sms.properties")
class SmsAuthenticationTest {
    SmsAuthentication smsAuthentication = new SmsAuthentication();

    //@Test
    public void getSmsAuthentication() {
        smsAuthentication.sendShortMessage();
    }

    @Value("${serviceId}")
    private String serviceId;

    @Test
    public void propertiesTest(){
        System.out.println(serviceId);
    }
}