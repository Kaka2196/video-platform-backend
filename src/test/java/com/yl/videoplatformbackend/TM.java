package com.yl.videoplatformbackend;

import com.yl.videoplatformbackend.service.impl.SendMailService;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class TM {

    @Autowired
    SendMailService sendMailService;
    @Autowired
    RedissonClient redisson;
    @Test
    void t(){
        sendMailService.sendSimpleMail("netgl174@gmail.com","test","testttt");
    }

    @Test
    void testRedission(){
        RBucket<Integer> bucket = redisson.getBucket("user:1742181896@qq.com");
        bucket.trySet(234122, 60, TimeUnit.SECONDS);
    }
    @Test
    void testRedisson2(){
       RBucket<Integer> bucket = redisson.getBucket("user:1742181896@qq.com");
        int s = bucket.get();
        System.out.println(s);
    }
}
