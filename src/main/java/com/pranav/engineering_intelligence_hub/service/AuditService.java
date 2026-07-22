package com.pranav.engineering_intelligence_hub.service;

import com.pranav.engineering_intelligence_hub.entity.AuditEvent;
import com.pranav.engineering_intelligence_hub.entity.AuditLog;
import com.pranav.engineering_intelligence_hub.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditRepository auditRepository;

    public void log(
            AuditEvent auditEvent,
            String username,
            String description
    ){
        AuditLog auditLog = AuditLog.builder()
                .eventType(auditEvent)
                .username(username)
                .description(description)
                .build();
        auditRepository.save(auditLog);
    }
}
