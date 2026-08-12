package com.pranav.engineering_intelligence_hub.service;

import com.pranav.engineering_intelligence_hub.dto.request.ProjectRequest;
import com.pranav.engineering_intelligence_hub.dto.response.ProjectResponse;
import com.pranav.engineering_intelligence_hub.entity.*;
import com.pranav.engineering_intelligence_hub.exceptions.AccessDeniedException;
import com.pranav.engineering_intelligence_hub.exceptions.ProjectNotFoundException;
import com.pranav.engineering_intelligence_hub.mapper.ProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.pranav.engineering_intelligence_hub.exceptions.TeamNotFoundException;
import com.pranav.engineering_intelligence_hub.repository.ProjectRepository;
import com.pranav.engineering_intelligence_hub.repository.TeamRepository;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;
    private final UserService userService;
    private final ProjectMapper projectMapper;
    private final AuditService auditService;

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ProjectResponse createProject(ProjectRequest req) {
        Team team=teamRepository.findById(req.teamId()).orElseThrow(()-> new TeamNotFoundException(req.teamId()));
        Project project=projectMapper.toEntity(req, team);
        User currentUser=userService.getCurrentUser();
        if(currentUser.getRole()!=Role.ADMIN && !currentUser.getTeams().contains(team)){
            throw new AccessDeniedException("You are not allowed to perform this action");
        }
        Project createdProject=projectRepository.save(project);
        auditService.log(
                AuditEvent.PROJECT_CREATED,
                currentUser.getUsername(),
                "Created Project: "+createdProject.getName());
        return projectMapper.toResponse(createdProject);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ENGINEER')")
    public Project getProjectEntity(Long projectId) {
        Project project=projectRepository.findById(projectId).orElseThrow(()-> new ProjectNotFoundException(projectId));
        User currentUser=userService.getCurrentUser();
        if(currentUser.getRole()!= Role.ADMIN && !currentUser.getTeams().contains(project.getTeam())){
            throw new AccessDeniedException("You are not allowed to access this project");
        }
        return project;
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ENGINEER')")
    public ProjectResponse getProject(Long projectId) {
        return projectMapper.toResponse(getProjectEntity(projectId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ENGINEER')")
    public List<ProjectResponse> getAllProjectsByTeamId(Long teamId) {
        Team team=teamRepository.findById(teamId).orElseThrow(()-> new TeamNotFoundException(teamId));
        User currentUser=userService.getCurrentUser();
        if(currentUser.getRole()!=Role.ADMIN && !currentUser.getTeams().contains(team)){
            throw new AccessDeniedException("You are not authorized to access this team's Projects");
        }
        return projectRepository.findByTeam(team)
                .stream()
                .map(projectMapper::toResponse)
                .toList();
    }
}
