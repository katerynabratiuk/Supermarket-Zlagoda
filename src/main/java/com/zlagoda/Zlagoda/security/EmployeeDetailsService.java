package com.zlagoda.Zlagoda.security;
import com.zlagoda.Zlagoda.entity.Employee;
import com.zlagoda.Zlagoda.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public EmployeeDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findById(username);
        if (employee == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new EmployeeDetails(employee);
    }
}
