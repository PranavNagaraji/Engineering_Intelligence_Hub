package com.pranav.engineering_intelligence_hub.dto.request;

import jakarta.validation.constraints.NotNull;

public record DocumentRequest(
    @NotNull String title,
    @NotNull String content
){}
