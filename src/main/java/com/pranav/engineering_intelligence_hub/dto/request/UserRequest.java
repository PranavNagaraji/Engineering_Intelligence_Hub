package com.pranav.engineering_intelligence_hub.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest (
    @NotBlank String username,
    @Email String email,
    @NotNull Long teamId
){}