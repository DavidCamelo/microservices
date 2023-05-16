package com.example.auth.service;

import com.example.auth.dto.LoginDTO;
import com.example.auth.dto.UserDTO;
import com.example.auth.exception.ServiceException;
import com.example.auth.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthorizationService {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public LoginDTO login(UserDTO userDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
        var userDetails = userDetailsService.loadUserByUsername(userDTO.getEmail());
        var roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return LoginDTO.builder()
                .token(jwtTokenUtil.generateLoginToken(userDetails.getUsername(), Map.of("ROLES", roles)))
                .build();
    }

    public void getAuthentication(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
            if (jwtTokenUtil.isTokenUnexpired(token)) {
                var subject = jwtTokenUtil.getSubjectFromToken(token);
                var extraClaims = jwtTokenUtil.getExtraClaims(token);
                log.info("User {}", subject);
                log.info("{}", extraClaims);
                var roles = (List<String>) extraClaims.get("ROLES");
                var authToken = new UsernamePasswordAuthenticationToken(subject, null, roles.stream().map(SimpleGrantedAuthority::new).toList());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                throw new ServiceException("Token expired");
            }
        }
    }
}
