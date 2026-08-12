package com.pranav.engineering_intelligence_hub.mapper;

import com.pranav.engineering_intelligence_hub.dto.request.IncidentRequest;
import com.pranav.engineering_intelligence_hub.dto.response.IncidentResponse;
import com.pranav.engineering_intelligence_hub.entity.Incident;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class IncidentMapper {
    public IncidentResponse toResponse(@Nonnull Incident incident) {
        return new IncidentResponse(
                incident.getId(),
                incident.getTitle(),
                incident.getIncidentStatus(),
                incident.getDescription(),
                incident.getProject().getId(),
                incident.getAssignedEngineer().getId()
        );
    }
    public Incident toEntity(@Nonnull IncidentRequest req) {
        Incident incident = new Incident();
        incident.setTitle(req.title());
        incident.setDescription(req.description());
        return incident;
    }
}
