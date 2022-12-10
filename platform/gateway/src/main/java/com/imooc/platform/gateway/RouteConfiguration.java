package com.imooc.platform.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RouteConfiguration {

    //两种方式定义网关
    //1. resource文件创建
    //2. 代码配置
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(route -> route
                        .path("/toilet-service/**")
                        .uri("lb://restroom-service")

                )
//                .route( route -> route
//                        .path("/employee/**")
//                        .uri("lb://employee-service")
//                )
                .build();
    }

}
