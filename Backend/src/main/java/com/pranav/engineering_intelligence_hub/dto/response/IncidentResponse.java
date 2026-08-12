package com.pranav.engineering_intelligence_hub.dto.response;

import com.pranav.engineering_intelligence_hub.entity.IncidentStatus;

public record IncidentResponse(
        Long id,
        String title,
        IncidentStatus status,
        String description,
        Long projectId,
        Long assignedEngineer
) {}
