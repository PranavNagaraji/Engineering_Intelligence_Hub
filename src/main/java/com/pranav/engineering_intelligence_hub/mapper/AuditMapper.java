package com.pranav.engineering_intelligence_hub.mapper;

import com.pranav.engineering_intelligence_hub.dto.response.AuditResponse;
import com.pranav.engineering_intelligence_hub.entity.AuditLog;
import org.springframework.stereotype.Component;

@Component
public class AuditMapper {

    public AuditResponse toResponse(AuditLog auditLog){
        return new AuditResponse(
                auditLog.getId(),
                auditLog.getEventType(),
                auditLog.getUsername(),
                auditLog.getDescription(),
                auditLog.getCreatedAt()
        );
    }
}