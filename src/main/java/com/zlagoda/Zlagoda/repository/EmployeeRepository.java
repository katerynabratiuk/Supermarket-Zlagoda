package com.zlagoda.Zlagoda.repository;

import com.zlagoda.Zlagoda.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends GenericRepository<Employee, String>{

    List<Employee> filter(Boolean manager, Boolean cashier);
}
