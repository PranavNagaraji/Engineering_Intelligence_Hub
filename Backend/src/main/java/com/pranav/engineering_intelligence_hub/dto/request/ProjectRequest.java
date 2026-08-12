package com.pranav.engineering_intelligence_hub.dto.request;

import jakarta.validation.constraints.NotNull;

public record ProjectRequest(
        @NotNull String name,
        @NotNull String description,
        @NotNull Long teamId
) {
}
