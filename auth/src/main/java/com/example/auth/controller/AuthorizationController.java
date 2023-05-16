package com.example.auth.controller;

import com.example.auth.dto.LoginDTO;
import com.example.auth.dto.UserDTO;
import com.example.auth.service.AuthorizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    @PostMapping(path = "/login")
    public ResponseEntity<LoginDTO> login(@Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(authorizationService.login(userDTO), HttpStatus.OK);
    }
}