package com.pranav.engineering_intelligence_hub.controller;

import com.pranav.engineering_intelligence_hub.dto.response.AuditResponse;
import com.pranav.engineering_intelligence_hub.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/audits")
public class AuditController {
    private final AuditService auditService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<AuditResponse> getAudit(){
        return auditService.getAllAudits();
    }
}
