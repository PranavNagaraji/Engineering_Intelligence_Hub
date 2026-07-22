package com.pranav.engineering_intelligence_hub.controller;

import com.pranav.engineering_intelligence_hub.dto.request.AuthRequest;
import com.pranav.engineering_intelligence_hub.dto.request.RegisterRequest;
import com.pranav.engineering_intelligence_hub.dto.response.AuthResponse;
import com.pranav.engineering_intelligence_hub.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest req){
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req){
        return ResponseEntity.ok(authService.register(req));
    }
}
