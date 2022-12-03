package com.imooc.employee.api;

import com.imooc.employee.pojo.EmployeeActivity;

import java.util.List;

public interface IEmployeeActivityService {

    EmployeeActivity useToilet(Long employeeId);

    EmployeeActivity restoreToilet(Long employeeId);

    List<Long> getAvailableIds();

}
