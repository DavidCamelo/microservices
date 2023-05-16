package com.example.logic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Operation(security = @SecurityRequirement(name = "Bearer Authentication"))
    @GetMapping(path = "/secured")
    public ResponseEntity<String> securedEndpoint() {
        return ResponseEntity.ok("Hello from secured endpoint");
    }

    @Operation(security = @SecurityRequirement(name = "Bearer Authentication"))
    @GetMapping(path = "/admin")
    public ResponseEntity<String> securedEndpointAdmin() {
        return ResponseEntity.ok("Hello from secured endpoint ADMIN");
    }

    @Operation(security = @SecurityRequirement(name = "Bearer Authentication"))
    @GetMapping(path = "/user")
    public ResponseEntity<String> securedEndpointUser() {
        return ResponseEntity.ok("Hello from secured endpoint USER");
    }
}
