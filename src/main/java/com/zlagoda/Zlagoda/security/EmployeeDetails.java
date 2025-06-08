package com.zlagoda.Zlagoda.security;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.zlagoda.Zlagoda.entity.Employee;

public class EmployeeDetails implements UserDetails {

    private final Employee employee;
    public EmployeeDetails(Employee employee) {
        this.employee = employee;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + employee.getRole().toUpperCase())
        );
    }

    @Override
    public String getPassword() {
        return employee.getSurname();
    }

    @Override
    public String getUsername() {
        return employee.getPhoneNumber();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
