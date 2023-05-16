package com.example.logic.service;

import com.example.logic.exception.ServiceException;
import com.example.logic.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthorizationService {
    private final JwtTokenUtil jwtTokenUtil;

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
