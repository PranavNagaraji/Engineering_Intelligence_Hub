package com.pranav.engineering_intelligence_hub.service;

import com.pranav.engineering_intelligence_hub.dto.response.AuditResponse;
import com.pranav.engineering_intelligence_hub.entity.AuditEvent;
import com.pranav.engineering_intelligence_hub.entity.AuditLog;
import com.pranav.engineering_intelligence_hub.mapper.AuditMapper;
import com.pranav.engineering_intelligence_hub.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper;

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

    public List<AuditResponse> getAllAudits(){
        List<AuditLog> auditLogs = auditRepository.findAllByOrderByCreatedAtDesc();
        List<AuditResponse> auditResponses = auditLogs
                .stream()
                .map(audit->auditMapper.toResponse(audit))
                .toList();
        return auditResponses;
    }
}
