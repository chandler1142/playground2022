package com.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/test")
    public String test() {
        return "test";
    }

    @RequestMapping("/test2")
    public String test2() {
        return "test2";
    }

}
