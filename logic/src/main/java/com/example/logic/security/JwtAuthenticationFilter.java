package com.example.logic.security;

import com.example.logic.service.AuthorizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private final AuthorizationService authorizationService;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   AuthorizationService authorizationService) {
        super(authenticationManager);
        this.authorizationService = authorizationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        var token = request.getHeader("Authorization");
        if (token != null) {
            try {
                authorizationService.getAuthentication(token);
                chain.doFilter(request, response);
            } catch (Exception ex) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setCharacterEncoding("UTF-8");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                objectMapper.writeValue(response.getWriter(), ex.getMessage());
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
