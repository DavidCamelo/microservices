package com.example.auth.config;

import com.example.auth.enums.RoleEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        var passwordEncoded = passwordEncoder().encode("Password#");
        return username ->
            switch (username) {
                case "admin@mail.com" ->
                        new User(username, passwordEncoded, true, true, true, true, List.of(new SimpleGrantedAuthority(RoleEnum.ADMIN.name())));
                case "user@mail.com" ->
                        new User(username, passwordEncoded, true, true, true, true, List.of(new SimpleGrantedAuthority(RoleEnum.USER.name())));
                default ->
                        throw new UsernameNotFoundException("user not found");
            };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
