package com.pranav.engineering_intelligence_hub.service;

import com.pranav.engineering_intelligence_hub.dto.response.DashboardResponse;
import com.pranav.engineering_intelligence_hub.entity.IncidentStatus;
import com.pranav.engineering_intelligence_hub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class DashboardService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;
    private final DocumentRepository documentRepository;
    private final IncidentRepository incidentRepository;

    public DashboardResponse getDashboard() {

        return new DashboardResponse(
                userRepository.count(),
                teamRepository.count(),
                projectRepository.count(),
                documentRepository.count(),
                incidentRepository.countByIncidentStatus(IncidentStatus.OPEN),
                incidentRepository.countByIncidentStatus(IncidentStatus.IN_PROGRESS),
                incidentRepository.countByIncidentStatus(IncidentStatus.RESOLVED),
                incidentRepository.countByIncidentStatus(IncidentStatus.CLOSED)
        );
    }
}
