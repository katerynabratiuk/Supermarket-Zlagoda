package com.zlagoda.Zlagoda.controller;

import com.zlagoda.Zlagoda.entity.Employee;
import com.zlagoda.Zlagoda.service.implementation.EmployeeServiceImpl;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employee")
@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500"
})
public class EmployeeController {

    private final EmployeeServiceImpl employeeService;

    public EmployeeController(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @GetMapping("/{employeeId}")
    public Employee getEmployee(@PathVariable String employeeId) {
        return employeeService.findById(employeeId);
    }

//    @GetMapping("/by-role/{role}")
//    public List<Employee> findByRole(@PathVariable String role) {
//        return employeeService.findByRole(role);
//    }

    @GetMapping("/filter")
    public List<Employee> filterProducts(
            @RequestParam(required = false) Boolean manager,
            @RequestParam(required = false) Boolean cashier,
            @RequestParam(required = false) String sortBy
    ) {
        List<String> sortParams = new ArrayList<>();
        if (sortBy != null && !sortBy.isBlank()) {
            sortParams.add(sortBy);
        }
        return employeeService.filterEmployee(manager, cashier, sortParams);
    }

    @PostMapping()
    public void create(@RequestBody @Valid Employee employee) {
        employeeService.create(employee);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody @Valid Employee employee) {
        employeeService.update(employee);
    }

    @DeleteMapping("/{employeeId}")
    public void delete(@PathVariable String employeeId) {
        employeeService.delete(employeeId);
    }
}

