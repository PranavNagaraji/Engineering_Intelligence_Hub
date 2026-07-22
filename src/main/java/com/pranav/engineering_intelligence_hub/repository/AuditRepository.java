package com.pranav.engineering_intelligence_hub.repository;

import com.pranav.engineering_intelligence_hub.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<AuditLog, Long> {
    
}
