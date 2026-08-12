package com.pranav.engineering_intelligence_hub.dto.response;

import jakarta.validation.constraints.NotNull;

public record DocumentResponse(
        @NotNull Long id,
        @NotNull String title,
        @NotNull String content,
        @NotNull Long projectId,
        @NotNull Long uploadedById
) {}
