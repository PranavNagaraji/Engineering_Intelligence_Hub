package com.pranav.engineering_intelligence_hub.service;

import com.pranav.engineering_intelligence_hub.dto.request.IncidentRequest;
import com.pranav.engineering_intelligence_hub.dto.response.IncidentResponse;
import com.pranav.engineering_intelligence_hub.entity.*;
import com.pranav.engineering_intelligence_hub.exceptions.*;
import com.pranav.engineering_intelligence_hub.mapper.IncidentMapper;
import com.pranav.engineering_intelligence_hub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.pranav.engineering_intelligence_hub.repository.IncidentRepository;
import com.pranav.engineering_intelligence_hub.repository.ProjectRepository;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentService {
    private final IncidentRepository incidentRepository;    
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ProjectService projectService;
    private final IncidentMapper incidentMapper;

    private Incident getIncident(Long id){
        Incident incident=incidentRepository.findById(id)
                .orElseThrow(()->new IncidentNotFoundException(id));
        return incident;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public IncidentResponse createIncident(Long projectId, IncidentRequest req, String assignedEngineerUsername) {
        Project project=projectRepository.findById(projectId)
            .orElseThrow(()-> new ProjectNotFoundException(projectId));
        User assignedUser=userRepository.findByUsername(assignedEngineerUsername)
                .orElseThrow(()->new UserNotFoundException("User :"+assignedEngineerUsername+",not found"));
        User currentUser=userService.getCurrentUser();
        if(currentUser.getRole()!= Role.ADMIN && !currentUser.getTeams().contains(project.getTeam())){
            throw new AccessDeniedException("You are not authorized to access this project");
        }
        if(!assignedUser.getTeams().contains(project.getTeam())){
            throw new AccessDeniedException("Assigned User is not authorized to access this project");
        }
        Incident incident=incidentMapper.toEntity(req);
        incident.setProject(project);
        incident.setAssignedEngineer(assignedUser);
        return incidentMapper.toResponse(incidentRepository.save(incident));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ENGINEER')")
    public List<IncidentResponse> getIncidentsByProjectId(Long projectId) {
        Project project=projectService.getProjectEntity(projectId);
        return incidentRepository
                .findByProject(project)
                .stream()
                .map(incidentMapper::toResponse)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ENGINEER')")
    public void deleteIncidentById(Long id){
        Incident incident=getIncident(id);
        User currentUser=userService.getCurrentUser();
        if(currentUser.getRole()!= Role.ADMIN && !currentUser.equals(incident.getAssignedEngineer())){
            throw new AccessDeniedException("You are not authorized to delete this incident");
        }
        incidentRepository.deleteById(id);
    }

    private void incidentStatusCheck(Incident incident, IncidentStatus incidentStatus, String message){
        if(incident.getIncidentStatus()!=incidentStatus){
            throw new IncidentStatusInvalidException(message);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ENGINEER')")
    public IncidentResponse startIncident(Long id){
        Incident incident=getIncident(id);
        User currentUser=userService.getCurrentUser();
        if(currentUser.getRole()!= Role.ADMIN && !currentUser.equals(incident.getAssignedEngineer())){
            throw new AccessDeniedException("You are not authorized to start this incident");
        }
        incidentStatusCheck(incident, IncidentStatus.OPEN, "Invalid incident Status");
        incident.setIncidentStatus(IncidentStatus.IN_PROGRESS);
        return incidentMapper.toResponse(incidentRepository.save(incident));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ENGINEER')")
    public IncidentResponse resolveIncident(Long id){
        Incident incident=getIncident(id);
        User currentUser=userService.getCurrentUser();
        if(currentUser.getRole()!= Role.ADMIN && !currentUser.equals(incident.getAssignedEngineer())){
            throw new AccessDeniedException("You are not authorized to resolve this project");
        }
        incidentStatusCheck(incident, IncidentStatus.IN_PROGRESS, "Invalid incident Status");
        incident.setIncidentStatus(IncidentStatus.RESOLVED);
        return incidentMapper.toResponse(incidentRepository.save(incident));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public IncidentResponse closeIncident(Long id){
        Incident incident=getIncident(id);
        User currentUser=userService.getCurrentUser();
        if(currentUser.getRole()!= Role.ADMIN && !currentUser.getTeams().contains(incident.getProject().getTeam())){
            throw new AccessDeniedException("You are not authorized to close this project");
        }
        incidentStatusCheck(incident, IncidentStatus.RESOLVED, "Invalid incident Status");
        incident.setIncidentStatus(IncidentStatus.CLOSED);
        return incidentMapper.toResponse(incidentRepository.save(incident));
    }
}
