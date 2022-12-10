package com.imooc.restroom.service;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@RequestMapping("test")
public class TestService {

    @GetMapping("ping")
    public String getAvailableToilet() {
        return "pong";
    }

}
