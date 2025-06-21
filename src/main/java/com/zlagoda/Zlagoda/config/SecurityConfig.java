package com.zlagoda.Zlagoda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.zlagoda.Zlagoda.security.UserAuthDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;
import com.zlagoda.Zlagoda.security.JwtAuthFilter;
import com.zlagoda.Zlagoda.security.JwtUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserAuthDetailsService userAuthDetailsService;
    private final JwtUtil jwtUtil;

    public SecurityConfig(UserAuthDetailsService userAuthDetailsService, JwtUtil jwtUtil) {
        this.userAuthDetailsService = userAuthDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder
    ) throws Exception {
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
                .userDetailsService(userAuthDetailsService)
                .passwordEncoder(passwordEncoder);

        return authBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/product/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/category/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/").permitAll()

                        .requestMatchers("/auth/me").authenticated()

                        .requestMatchers(HttpMethod.GET, "/customer/").hasAnyRole("CASHIER", "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/check/").hasAnyRole("CASHIER", "MANAGER")
//                        .requestMatchers(HttpMethod.GET, "/employee/").hasAnyRole( "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/stats/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/employee/me").authenticated()

                      // Modification endpoints
                        .requestMatchers(HttpMethod.POST, "/product/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/category/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/customer/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/employee/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/check/").hasRole("CASHIER")

                        .requestMatchers(HttpMethod.PUT, "/product/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/category/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/customer/").hasAnyRole("CASHIER", "MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/employee/").hasRole("MANAGER")

                        .requestMatchers(HttpMethod.DELETE, "/product/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/category/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/customer/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/employee/").hasRole("MANAGER")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);
        return http.build();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()
//                )
//                .formLogin(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable);
//
//        return http.build();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
//                .formLogin(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .build();
//    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtUtil, userAuthDetailsService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://127.0.0.1:5500", "http://localhost:8090"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}