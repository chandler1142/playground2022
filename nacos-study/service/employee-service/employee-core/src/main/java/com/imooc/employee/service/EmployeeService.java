package com.imooc.employee.service;

import com.imooc.employee.api.IEmployeeActivityService;
import com.imooc.employee.dao.EmployeeActivityDao;
import com.imooc.employee.entity.EmployeeActivityEntity;
import com.imooc.employee.pojo.ActivityType;
import com.imooc.employee.pojo.EmployeeActivity;
import com.imooc.restroom.api.IRestroomService;
import com.imooc.restroom.pojo.Toilet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("employee-service")
public class EmployeeService implements IEmployeeActivityService {

    @Autowired
    private EmployeeActivityDao employeeActivityDao;

    @Autowired
    private IRestroomService restroomService;

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
        List<Toilet> toilets = restroomService.getAvailableToilet();
        if (CollectionUtils.isEmpty(toilets)) {
            throw new RuntimeException("shit in urinal");
        }

        //抢坑
        Toilet toilet = restroomService.occupy(toilets.get(0).getId());

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

        //释放坑
        restroomService.release(record.getResourceId());
        record.setActive(false);
        record.setEndTime(new Date());

        employeeActivityDao.save(record);

        EmployeeActivity result = new EmployeeActivity();
        BeanUtils.copyProperties(record, result);

        return result;
    }

    @GetMapping("getAvailableIds")
    public List<Long> getAvailableIds() {
        List<Toilet> availableToilet = restroomService.getAvailableToilet();
        return availableToilet.stream().map(element -> {
            return element.getId();
        }).collect(Collectors.toList());
    }

}
