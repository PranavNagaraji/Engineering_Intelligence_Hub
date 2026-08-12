package com.pranav.engineering_intelligence_hub.dto.request;

import jakarta.validation.constraints.NotNull;

public record TeamRequest(
    @NotNull String name
){}
