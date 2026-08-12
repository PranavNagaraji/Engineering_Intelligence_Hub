package com.pranav.engineering_intelligence_hub.controller;

import com.pranav.engineering_intelligence_hub.dto.request.AuthRequest;
import com.pranav.engineering_intelligence_hub.dto.request.RegisterRequest;
import com.pranav.engineering_intelligence_hub.dto.response.AuthResponse;
import com.pranav.engineering_intelligence_hub.service.AuthService;
import com.pranav.engineering_intelligence_hub.util.CookieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;
    private final CookieService cookieService;

    @GetMapping("/csrf")
    public ResponseEntity<CsrfToken> csrf(CsrfToken token) {
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest req){
        String token=authService.login(req);
        return ResponseEntity.ok()
                .header("Set-Cookie", cookieService.createCookie(token).toString())
                .body(new AuthResponse("User logged in successfully"));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req){
        String token=authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Set-Cookie", cookieService.createCookie(token).toString())
                .body(new AuthResponse("User registered successfully"));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(){
        return ResponseEntity.ok()
                .header("Set-Cookie", cookieService.clearCookie().toString())
                .body(new AuthResponse("User logged out successfully"));
    }
}
