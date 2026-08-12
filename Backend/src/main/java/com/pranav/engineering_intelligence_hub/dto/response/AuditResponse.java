package com.pranav.engineering_intelligence_hub.dto.response;

import com.pranav.engineering_intelligence_hub.entity.AuditEvent;

import java.time.LocalDateTime;

public record AuditResponse(
        Long id,
        AuditEvent eventType,
        String username,
        String description,
        LocalDateTime createdAt
) {}
