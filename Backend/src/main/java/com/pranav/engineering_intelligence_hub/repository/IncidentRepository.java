package com.pranav.engineering_intelligence_hub.repository;

import com.pranav.engineering_intelligence_hub.entity.IncidentStatus;
import com.pranav.engineering_intelligence_hub.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pranav.engineering_intelligence_hub.entity.Incident;

import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long>{
    
    @Modifying(
        clearAutomatically = true,
        flushAutomatically = true
    )
    @Query("""
        DELETE FROM Incident i
        WHERE i.project.id=:projectId
        """)
    int deleteIncidentsByProjectId(@Param("projectId") Long projectId);

    List<Incident> findByProject(Project project);

    long countByIncidentStatus(IncidentStatus incidentStatus);
}