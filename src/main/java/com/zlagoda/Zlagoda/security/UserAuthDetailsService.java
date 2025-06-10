package com.zlagoda.Zlagoda.security;

import com.zlagoda.Zlagoda.entity.UserAuth;
import com.zlagoda.Zlagoda.repository.UserAuthRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserAuthDetailsService implements UserDetailsService {

    private final UserAuthRepository userAuthRepository;

    public UserAuthDetailsService(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuth userAuth = userAuthRepository.findByUsername(username);
        if (userAuth == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        List<GrantedAuthority> authorities = userAuthRepository.findAuthoritiesByUsername(username);
        return new UserAuthDetails(userAuth, authorities);
    }
}