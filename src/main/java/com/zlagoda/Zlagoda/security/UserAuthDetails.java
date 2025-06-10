package com.zlagoda.Zlagoda.security;

import com.zlagoda.Zlagoda.entity.UserAuth;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class UserAuthDetails implements UserDetails {

    private final UserAuth userAuth;
    private final List<GrantedAuthority> authorities;

    public UserAuthDetails(UserAuth userAuth, List<GrantedAuthority> authorities) {
        this.userAuth = userAuth;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userAuth.getPassword();
    }

    @Override
    public String getUsername() {
        return userAuth.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userAuth.isEnabled();
    }
}

