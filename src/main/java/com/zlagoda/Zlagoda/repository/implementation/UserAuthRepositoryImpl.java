package com.zlagoda.Zlagoda.repository.implementation;

import com.zlagoda.Zlagoda.entity.UserAuth;
import com.zlagoda.Zlagoda.repository.UserAuthRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserAuthRepositoryImpl implements UserAuthRepository {

    private final DataSource dataSource;

    public UserAuthRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UserAuth findByUsername(String username) {
        String sql = "SELECT username, password, enabled FROM users WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UserAuth user = new UserAuth();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEnabled(rs.getBoolean("enabled"));
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<GrantedAuthority> findAuthoritiesByUsername(String username) {
        String sql = "SELECT authority FROM authorities WHERE username = ?";
        List<GrantedAuthority> authorities = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String role = rs.getString("authority");
                authorities.add(new SimpleGrantedAuthority(role));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authorities;
    }
}
