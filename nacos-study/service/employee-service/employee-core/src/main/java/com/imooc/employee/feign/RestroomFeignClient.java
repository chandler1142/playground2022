package com.imooc.employee.feign;

import com.imooc.restroom.api.IRestroomService;
import com.imooc.restroom.pojo.Toilet;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 1. 直接定义Feign接口
 * 2. 继承接口 (使用这个)
 * 3. 接口定义注解
 */
@FeignClient(value = "restroom-service")
public interface RestroomFeignClient extends IRestroomService {

    @GetMapping("/toilet-service/checkAvailable")
    public List<Toilet> getAvailableToilet();

    @PostMapping("/toilet-service/occupy")
    public Toilet occupy(@RequestParam("id") Long id);

    @PostMapping("/toilet-service/release")
    public Toilet release(@RequestParam("id") Long id);

    @GetMapping("/toilet-service/checkAvailability")
    public boolean checkAvailability(@RequestParam("id") Long id);

}
