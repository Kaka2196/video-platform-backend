package com.yl.videoplatformbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.yl.videoplatformbackend.mapper")
@EnableAsync
public class VideoPlatformBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoPlatformBackendApplication.class, args);
    }

}
