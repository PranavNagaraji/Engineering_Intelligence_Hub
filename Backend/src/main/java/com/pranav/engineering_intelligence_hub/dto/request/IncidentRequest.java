package com.pranav.engineering_intelligence_hub.dto.request;

import jakarta.validation.constraints.NotNull;

public record IncidentRequest(
        @NotNull String title,
        @NotNull String description,
        @NotNull String assignedEngineerUsername
) {}
