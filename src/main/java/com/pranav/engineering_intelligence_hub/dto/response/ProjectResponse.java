package com.pranav.engineering_intelligence_hub.dto.response;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        Long teamId
) {
}
