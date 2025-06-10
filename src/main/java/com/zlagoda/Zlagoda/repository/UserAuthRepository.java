package com.zlagoda.Zlagoda.repository;

import com.zlagoda.Zlagoda.entity.UserAuth;
import org.springframework.security.core.GrantedAuthority;
import java.util.List;

public interface UserAuthRepository {
    UserAuth findByUsername(String username);
    List<GrantedAuthority> findAuthoritiesByUsername(String username);
}
