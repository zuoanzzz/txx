package com.bosc.txx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bosc.txx.dao")
public class TxxApplication {

    public static void main(String[] args) {
        SpringApplication.run(TxxApplication.class, args);
    }

}
