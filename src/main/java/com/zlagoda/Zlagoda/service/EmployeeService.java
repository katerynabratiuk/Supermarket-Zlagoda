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
    List<Employee> search(String query);
    List<Employee> filterEmployee(Boolean manager, Boolean cashier, List<String> sortBy);
    Employee getEmployeeByUsername(String username);
}