package com.zlagoda.Zlagoda.controller;

import com.zlagoda.Zlagoda.entity.Employee;
import com.zlagoda.Zlagoda.service.implementation.EmployeeServiceImpl;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee/")
public class EmployeeController {

    private final EmployeeServiceImpl employeeService;

    public EmployeeController(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @GetMapping("by-id/{employeeId}")
    public Employee getEmployee(@PathVariable String employeeId) {
        return employeeService.findById(employeeId);
    }

    @GetMapping("by-role/{role}")
    public List<Employee> findByRole(@PathVariable String role) {
        return employeeService.findByRole(role);
    }

    @PostMapping("add")
    public void create(@RequestBody @Valid Employee employee) {
        employeeService.create(employee);
    }

    @PutMapping("edit/{id}")
    public void update(@RequestBody @Valid Employee employee) {
        employeeService.update(employee);
    }

    @DeleteMapping("delete/{employeeId}")
    public void delete(@PathVariable String employeeId) {
        employeeService.delete(employeeId);
    }
}

