package com.pranav.engineering_intelligence_hub.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest (
    @NotBlank
    String username,

    @NotBlank
    String password
){}