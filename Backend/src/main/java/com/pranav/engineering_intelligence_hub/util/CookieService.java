package com.pranav.engineering_intelligence_hub.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CookieService {

    @Value("${COOKIE_SECURE}")
    private boolean secure;

    @Value("${COOKIE_SAME_SITE}")
    private String sameSite;

    public ResponseCookie createCookie(String token) {
        return ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build();
    }

    public ResponseCookie clearCookie() {
        return ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path("/")
                .maxAge(Duration.ZERO)
                .build();
    }
}
