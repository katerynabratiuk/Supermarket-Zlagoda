package com.zlagoda.Zlagoda.config;

import com.zlagoda.Zlagoda.security.EmployeeDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

     private final EmployeeDetailsService employeeDetailsService;

     public SecurityConfig(EmployeeDetailsService employeeDetailsService) {
         this.employeeDetailsService = employeeDetailsService;
     }

//     @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http.csrf(AbstractHttpConfigurer::disable)
//                 .authorizeHttpRequests(auth -> auth
//                         .requestMatchers(HttpMethod.GET, "/product/**").hasAnyRole("USER", "CASHIER", "MANAGER")
//                         .requestMatchers(HttpMethod.GET, "/category").hasAnyRole("USER")
//                         .requestMatchers(HttpMethod.GET, "/category/**").hasAnyRole("CASHIER", "MANAGER")
//                         .requestMatchers(HttpMethod.GET, "/customer/**").hasAnyRole("CASHIER", "MANAGER")
//                         .requestMatchers(HttpMethod.GET, "/check/**").hasAnyRole("CASHIER", "MANAGER")
//                         .requestMatchers(HttpMethod.GET, "/employee/**").hasAnyRole("CASHIER", "MANAGER")
//
//                         .requestMatchers(HttpMethod.POST, "/product/**").hasAnyRole("MANAGER")
//                         .requestMatchers(HttpMethod.POST, "/category/**").hasAnyRole("MANAGER")
//                         .requestMatchers(HttpMethod.POST, "/customer/**").hasAnyRole("MANAGER")
//                         .requestMatchers(HttpMethod.POST, "/employee/**").hasAnyRole("MANAGER")
//                         .requestMatchers(HttpMethod.POST, "/check/**").hasAnyRole("CASHIER")
//
//                         .requestMatchers(HttpMethod.PUT, "/product/**").hasAnyRole("MANAGER")
//                         .requestMatchers(HttpMethod.PUT, "/category/**").hasAnyRole("MANAGER")
//                         .requestMatchers(HttpMethod.PUT, "/customer/**").hasAnyRole("CASHIER", "MANAGER")
//                         .requestMatchers(HttpMethod.PUT, "/employee/**").hasAnyRole("MANAGER")
//
//                         .requestMatchers(HttpMethod.DELETE, "/product/**").hasAnyRole("MANAGER")
//                         .requestMatchers(HttpMethod.DELETE, "/category/**").hasAnyRole("MANAGER")
//                         .requestMatchers(HttpMethod.DELETE, "/customer/**").hasAnyRole("MANAGER")
//                         .requestMatchers(HttpMethod.DELETE, "/employee/**").hasAnyRole("MANAGER")
//                         .anyRequest().authenticated()
//                 )
//                 .formLogin(withDefaults());
//         return http.build();
//     }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) throws Exception {
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        return authBuilder.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public EmployeeDetailsService getEmployeeDetailsService() {
        return employeeDetailsService;
    }
}
