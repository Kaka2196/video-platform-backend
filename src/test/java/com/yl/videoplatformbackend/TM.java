package com.yl.videoplatformbackend;

import com.yl.videoplatformbackend.service.impl.SendMailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TM {

    @Autowired
    SendMailService sendMailService;
    @Test
    void t(){
        sendMailService.sendSimpleMail("young.leon@qq.com","young.leon@qq.com","test","testttt");
    }
}
