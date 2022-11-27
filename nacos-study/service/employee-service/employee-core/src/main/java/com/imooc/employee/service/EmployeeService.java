package com.imooc.employee.service;

import com.imooc.employee.api.IEmployeeActivityService;
import com.imooc.employee.dao.EmployeeActivityDao;
import com.imooc.employee.entity.EmployeeActivityEntity;
import com.imooc.employee.pojo.ActivityType;
import com.imooc.employee.pojo.EmployeeActivity;
import com.imooc.restroom.pojo.Toilet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("employee-service")
public class EmployeeService implements IEmployeeActivityService {

    @Autowired
    private EmployeeActivityDao employeeActivityDao;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    @Transactional
    @PostMapping("toilet-break")
    public EmployeeActivity useToilet(Long employeeId) {

        List<ServiceInstance> instances = discoveryClient.getInstances("restroom-service");

        int count = employeeActivityDao.countByEmployeeIdAndActivityTypeAndActive(employeeId, ActivityType.TOILET_BREAK, true);
        if (count > 0) {
            throw new RuntimeException("快拉");
        }


        //发起源成调用
        Toilet[] toilets = restTemplate.getForObject("http://restroom-service/toilet-service/checkAvailable/", Toilet[].class);
        if (ArrayUtils.isEmpty(toilets)) {
            throw new RuntimeException("shit in urinal");
        }

        //抢坑
        MultiValueMap<String, Object> args = new LinkedMultiValueMap<>();
        args.add("id", toilets[0].getId());
        Toilet toilet = restTemplate.postForObject("http://restroom-service/toilet-service/occupy",
                args,
                Toilet.class);

        //保存如厕记录
        EmployeeActivityEntity toiletBreak = EmployeeActivityEntity.builder()
                .employeeId(employeeId)
                .active(true)
                .activityType(ActivityType.TOILET_BREAK)
                .resourceId(toilet.getId())
                .build();

        employeeActivityDao.save(toiletBreak);

        EmployeeActivity result = new EmployeeActivity();
        BeanUtils.copyProperties(toiletBreak, result);

        return result;
    }

    @Override
    @Transactional
    @PostMapping("done")
    public EmployeeActivity restoreToilet(Long activityId) {
        EmployeeActivityEntity record = employeeActivityDao.findById(activityId)
                .orElseThrow(() -> new RuntimeException("record not found"));
        if (!record.isActive()) {
            throw new RuntimeException("activity is no longer active");
        }

        MultiValueMap<String, Object> args = new LinkedMultiValueMap<>();
        args.add("id", record.getResourceId());
        Toilet toilet = restTemplate.postForObject("http://restroom-service/toilet-service/release",
                args,
                Toilet.class);
        record.setActive(false);
        record.setEndTime(new Date());

        employeeActivityDao.save(record);

        EmployeeActivity result = new EmployeeActivity();
        BeanUtils.copyProperties(record, result);

        return result;
    }

}
