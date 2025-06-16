package com.zlagoda.Zlagoda.service;

import com.zlagoda.Zlagoda.entity.Employee;

import java.util.List;

public interface EmployeeService{

    List<Employee> findAll();
    List<Employee> findByName(String name);
    Employee findById(String id);
    void create(Employee employee);
    void update(Employee employee);
    void delete(String id);
}
