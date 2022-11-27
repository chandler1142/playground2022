package com.imooc.restroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@ComponentScan(basePackages = {"com.imooc"})
@EnableDiscoveryClient
@EnableJpaAuditing
@SpringBootApplication
public class RestroomApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestroomApplication.class, args);
    }

}
