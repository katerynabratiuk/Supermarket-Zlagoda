package com.zlagoda.Zlagoda.service.implementation;

import com.zlagoda.Zlagoda.entity.Employee;
import com.zlagoda.Zlagoda.util.IdGenerator;
import com.zlagoda.Zlagoda.repository.EmployeeRepository;
import com.zlagoda.Zlagoda.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    @Autowired
    private IdGenerator idGenerator;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    public List<Employee> filterEmployee(Boolean manager, Boolean cashier, List<String> sortBy) {
        return employeeRepository.filter(manager, cashier, sortBy);
    }


    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> findByName(String name) {
        return employeeRepository.findByName(name);
    }

    @Override
    public Employee findById(String id) {
        return employeeRepository.findById(id);
    }

    @Override
    public void create(Employee employee) {
        if (employee.getId() == null) {
            employee.setId(idGenerator.generate(IdGenerator.Option.Employee));
        }
        employeeRepository.create(employee);
    }

    @Override
    public void update(Employee employee) {
        if (employee.getPatronymic() != null && employee.getPatronymic().isBlank()) {
            employee.setPatronymic(null);
        }
        employeeRepository.update(employee);
    }

    @Override
    public void delete(String id) {
        employeeRepository.delete(id);
    }

    @Override
    public List<Employee> search(String query) {
        return employeeRepository.search(query);
    }

    public Employee getEmployeeByUsername(String username) {
        Employee employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            throw new UsernameNotFoundException("Employee not found for username: " + username);
        }
        return employee;
    }
}