package com.bosc.txx.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhoulei
 * @date 2025/8/21
 */

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello(){
        return "Hello World";
    }

}
