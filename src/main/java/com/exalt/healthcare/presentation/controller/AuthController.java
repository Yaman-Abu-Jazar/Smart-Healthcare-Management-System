package com.exalt.healthcare.presentation.controller;

import com.exalt.healthcare.common.payload.AuthenticationRequest;
import com.exalt.healthcare.common.payload.AuthenticationResponse;
import com.exalt.healthcare.domain.service.implementations.AuthenticationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationServiceImpl authService;

    public AuthController(AuthenticationServiceImpl authService){
        this.authService = authService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
